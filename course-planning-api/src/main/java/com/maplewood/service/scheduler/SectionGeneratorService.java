package com.maplewood.service.scheduler;

import com.maplewood.exception.initialization.InitializationException;
import com.maplewood.model.*;
import com.maplewood.repository.*;
import com.maplewood.service.TimeSlotProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Core scheduling engine responsible for generating course sections.
 *
 * <p>This service orchestrates:
 * <ul>
 *     <li>Room allocation</li>
 *     <li>Teacher assignment</li>
 *     <li>Time slot pattern application</li>
 * </ul>
 *
 * <p>All scheduling constraints are enforced during generation.</p>
 */
@Service
@RequiredArgsConstructor
public class SectionGeneratorService {

  private final SectionRepository sectionRepository;
  private final SemesterRepository semesterRepository;
  private final ClassroomRepository classroomRepository;
  private final TeacherRepository teacherRepository;
  private final CourseRepository courseRepository;
  private final RoomAvailabilityService roomAvailabilityService;
  private final TeacherAvailabilityService teacherAvailabilityService;

  private static final Logger log = LoggerFactory.getLogger(SectionGeneratorService.class);

  @Transactional
  public void resetSchedule()
  {
    sectionRepository.deleteAll();
  }

  /**
   * Entry point for generating all sections at startup.
   */
  public void generateAllSections()
  {
    //Get the Active Semester
    Optional<Semester> optionalSemester = semesterRepository.findActiveSemester();
    if (optionalSemester.isEmpty())
    {
      throw new InitializationException("No active semester found");
    }

    Semester semester = optionalSemester.get();

    List<Classroom> rooms = classroomRepository.findAll();
    List<Teacher> teachers = teacherRepository.findAll();
    List<Course> courses = courseRepository.findAll();

    log.debug("Size={}", courses.size());
    // Initialize availability state
    roomAvailabilityService.initialize(rooms);
    teacherAvailabilityService.initialize(teachers);
    for (Course course : courses)
    {
      generateSectionsForCourse(semester,course, teachers, rooms);
    }
  }

  /**
   * Generates sections for a single course.
   */
  private void generateSectionsForCourse(Semester semester,Course course, List<Teacher> teachers, List<Classroom> rooms)
  {
    //Get all possible weekly Schedule
    List<Set<TimeSlot>> patterns = TimeSlotProvider.getPatterns(course.getHoursPerWeek());

    //For Each weekly Schedule we need one teacher and one classroom
    for (Set<TimeSlot> slots : patterns)
    {
      //Find First Available Teacher
      Teacher selectedTeacher = null;
      for (Teacher t : teachers)
      {
        boolean isSameSpecialization = t.getSpecialization().getId().equals(course.getSpecialization().getId());
        boolean isTeacherAvailable = teacherAvailabilityService.isAvailable(t, slots);

        if ( isSameSpecialization && isTeacherAvailable)
        {
          selectedTeacher = t;
          break;
        }
      }

      if (selectedTeacher == null)
      {
        log.debug("No available teacher for course={}", course.getName());
        continue;
      }

      //Find First Available Room
      Classroom selectedRoom = null;
      for (Classroom r : rooms)
      {
        boolean isSameRoomType = r.getRoomType().getId().equals(course.getSpecialization().getRoomType().getId());
        boolean isRoomAvailable = roomAvailabilityService.isAvailable(r, slots);
        if ( isSameRoomType && isRoomAvailable)
        {
          selectedRoom = r;
          break;
        }
      }
      if (selectedRoom == null)
      {
        log.debug("No available room for course={}", course.getName());
        continue;
      }

      log.debug("Found assignment: course={} teacher={}, room={}", course.getName(),selectedTeacher.getId(), selectedRoom.getId());

      teacherAvailabilityService.reserve(selectedTeacher, slots);
      roomAvailabilityService.reserve(selectedRoom, slots);

      Section section = new Section();
      section.setCourse(course);
      section.setTeacher(selectedTeacher);
      section.setClassroom(selectedRoom);
      section.setSemester(semester);

      for (TimeSlot timeSlot : slots)
      {
        timeSlot.setSection(section);
      }

      section.setTimeSlots(slots);
      sectionRepository.save(section);
      log.info("Section created for course {} with teacher {} in room {}", course.getName(), selectedTeacher.getId(), selectedRoom.getId());
    }
  }
}