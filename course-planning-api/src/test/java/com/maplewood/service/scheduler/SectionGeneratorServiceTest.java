package com.maplewood.service.scheduler;

import com.maplewood.model.*;
import com.maplewood.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionGeneratorServiceTest
{
  @Mock SectionRepository sectionRepository;
  @Mock SemesterRepository semesterRepository;
  @Mock ClassroomRepository classroomRepository;
  @Mock TeacherRepository teacherRepository;
  @Mock CourseRepository courseRepository;
  private SectionGeneratorService service;

  Semester semester;
  Course scienceCourse;
  Teacher teacher;
  Classroom labRoom;

  @BeforeEach
  void setUp()
  {
    RoomAvailabilityService roomAvailabilityService = new RoomAvailabilityService();
    TeacherAvailabilityService teacherAvailabilityService = new TeacherAvailabilityService();
    service = new SectionGeneratorService(
      sectionRepository,
      semesterRepository,
      classroomRepository,
      teacherRepository,
      courseRepository,
      roomAvailabilityService,
      teacherAvailabilityService
    );
    semester = new Semester();
    semester.setOrderInYear(1);

    when(semesterRepository.findActiveSemester())
      .thenReturn(java.util.Optional.of(semester));

    // COURSE (Science → matches your dataset SCI201 / SCI301 etc.)
    scienceCourse = new Course();
    scienceCourse.setId(1L);
    scienceCourse.setName("Chemistry I");
    scienceCourse.setSemesterOrder(1);
    scienceCourse.setHoursPerWeek(5);

    // ROOM (use LAB from your dataset)
    RoomType labType = new RoomType();
    labType.setId(2L); // science_lab

    Specialization scienceSpec = new Specialization();
    scienceSpec.setId(2L); // Science in your dataset
    scienceSpec.setRoomType(labType);
    scienceCourse.setSpecialization(scienceSpec);

    // TEACHER
    teacher = new Teacher();
    teacher.setId(3L);
    Specialization tSpec = new Specialization();
    tSpec.setId(2L);
    teacher.setSpecialization(tSpec);



    labRoom = new Classroom();
    labRoom.setId(31L);
    labRoom.setCapacity(30);
    labRoom.setRoomType(labType);

    when(courseRepository.findAll())
      .thenReturn(List.of(scienceCourse));

    when(teacherRepository.findAll())
      .thenReturn(List.of(teacher));

    when(classroomRepository.findAll())
      .thenReturn(List.of(labRoom));


  }

  @Test
  void shouldGenerateSectionSuccessfully()
  {
    when(sectionRepository.existsBySignature(anyString()))
      .thenReturn(false);

    service.generateAllSections();

    verify(sectionRepository, atLeastOnce()).save(any(Section.class));
  }

  @Test
  void shouldSkipWhenNoTeacherAvailable()
  {
    scienceCourse.setHoursPerWeek(6);
    Specialization tSpec = new Specialization();
    tSpec.setId(3L);
    teacher.setSpecialization(tSpec);
    service.generateAllSections();
    verify(sectionRepository, never()).save(any());
  }

  @Test
  void shouldSkipWhenNoRoomAvailable()
  {
    scienceCourse.setHoursPerWeek(4);
    RoomType labTypeNew = new RoomType();
    labTypeNew.setId(3L);
    labRoom.setRoomType(labTypeNew);
    service.generateAllSections();
    verify(sectionRepository, never()).save(any());
  }

  @Test
  void shouldSkipWhenDuplicateSignatureExists()
  {
    scienceCourse.setHoursPerWeek(3);
    when(sectionRepository.existsBySignature(anyString()))
      .thenReturn(true);
    service.generateAllSections();
    verify(sectionRepository, never()).save(any());
  }
}
