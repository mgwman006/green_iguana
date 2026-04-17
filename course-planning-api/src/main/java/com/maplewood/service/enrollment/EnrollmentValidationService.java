package com.maplewood.service.enrollment;

import com.maplewood.exception.EnrollmentValidationException;
import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import com.maplewood.exception.error.EnrollmentValidationError;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EnrollmentValidationService
{
  private final Map<Long, Set<Long>> prerequisiteClosure = new HashMap<>();
  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final StudentCourseHistoryRepository studentCourseHistoryRepository;


  public void validateEnrollment(Student student, Section section)
  {
    Course course = section.getCourse();
    List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

    if (isDuplicate(enrollments,section,course))
    {
      throw new EnrollmentValidationException(EnrollmentValidationError.DUPLICATE);
    }
    if (isInValidGradeLevel(student, course))
    {
      throw new EnrollmentValidationException(EnrollmentValidationError.INVALID_GRADE_LEVEL);
    }
    if (student.hasReachedCoursesLimit())
    {
      throw new EnrollmentValidationException(EnrollmentValidationError.MAXIMUM_COURSE_LIMIT_REACHED);
    }
    if (isTimeConflicting(enrollments, section.getTimeSlots()))
    {
      throw new EnrollmentValidationException(EnrollmentValidationError.SCHEDULE_CONFLICT);
    }
    if (!hasMetPreRequisites(student, course))
    {
      throw new EnrollmentValidationException(EnrollmentValidationError.MISSING_PREREQUISITE);
    }
  }

  private boolean isDuplicate(List<Enrollment> enrollments, Section section, Course course)
  {
    for (Enrollment enrollment : enrollments)
    {
      Section currentSection = enrollment.getSection();

      if (currentSection.getId().equals(section.getId()))
      {
        return true;
      }

      if (currentSection.getCourse().getId().equals(course.getId()))
      {
        return true;
      }
    }
    return false;
  }

  private boolean isInValidGradeLevel(Student student, Course course)
  {
    return student.getGradeLevel() > course.getGradeLevelMax() ||
      student.getGradeLevel() < course.getGradeLevelMin();
  }

  private boolean isTimeConflicting(List<Enrollment> enrollments, Set<TimeSlot> newSlots)
  {
    for (Enrollment enrollment : enrollments)
    {
      Set<TimeSlot> currentSlots = enrollment.getSection().getTimeSlots();
      for (TimeSlot newSlot : newSlots)
      {
        for (TimeSlot existing : currentSlots)
        {
          if (newSlot.isOverlap(existing))
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  private boolean hasMetPreRequisites(Student student, Course course)
  {
    Set<Long> passed = studentCourseHistoryRepository.findPassedHistory(student.getId())
        .stream()
        .map(StudentCourseHistory::getCourseId)
        .collect(Collectors.toSet());
    Set<Long> required = prerequisiteClosure.getOrDefault(course.getId(), Set.of());
    return passed.containsAll(required);
  }

  @PostConstruct
  public void buildClosure()
  {
    List<Course> courses = courseRepository.findAll();

    for (Course course : courses)
    {
      Set<Long> closure = new HashSet<>();

      Course current = course.getPrerequisite();

      while (current != null)
      {
        if (!closure.add(current.getId()))
        {
          throw new IllegalStateException("Cycle detected at course " + course.getId());
        }
        current = current.getPrerequisite();
      }

      prerequisiteClosure.put(course.getId(), closure);
    }
  }

}