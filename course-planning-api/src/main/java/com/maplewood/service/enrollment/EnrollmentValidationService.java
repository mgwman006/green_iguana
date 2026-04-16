package com.maplewood.service.enrollment;

import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import com.maplewood.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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


  public Result<Boolean> validate(Student student, Section section)
  {
    try
    {
      Course course = section.getCourse();
      if (ObjectUtils.isEmpty(course))
      {
        return Result.failure("Course not found");
      }

      if (!isValidGradeLevel(student, course))
      {
        return Result.failure("Enrollment blocked - Not Valid GradeLevel");
      }
      if (!hasCapacity(section))
      {
        return Result.failure("Enrollment blocked - maximum courses exceeded");
      }
      if (isTimeConflicts(student, section.getTimeSlots()))
      {
        return Result.failure("Enrollment blocked - schedule conflict");
      }

      if (!hasValidPrerequisites_v2(student, course))
      {
        return Result.failure("Enrollment blocked - missing prerequisite");
      }

      return Result.success("Enrollment succeeds",Boolean.TRUE);
    }//try
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }

  private boolean hasCapacity(Section section)
  {
    long enrolled = section.getEnrollments().size();
    return enrolled < section.getClassroom().getCapacity();
  }

  private boolean isValidGradeLevel(Student student, Course course)
  {
    return student.getGradeLevel() <= course.getGradeLevelMax() &&
      student.getGradeLevel() >= course.getGradeLevelMin();
  }

  private boolean isTimeConflicts(Student student, Set<TimeSlot> newSlots)
  {
    List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

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

  private boolean hasValidPrerequisites(Student student, Course course)
  {
    if (course.getPrerequisite() == null)
    {
      return true;
    }

    Set<Long> completedCourseIds =
      studentCourseHistoryRepository.findPassedHistory(student.getId())
        .stream()
        .map(StudentCourseHistory::getCourseId)
        .collect(Collectors.toSet());

    return completedCourseIds.contains(course.getPrerequisite().getId());
  }

  public boolean hasValidPrerequisites_v2(Student student, Course course)
  {
    Set<Long> passed =
      studentCourseHistoryRepository.findPassedHistory(student.getId())
        .stream()
        .map(StudentCourseHistory::getCourseId)
        .collect(Collectors.toSet());
    Set<Long> required = prerequisiteClosure.getOrDefault(course.getId(), Set.of());
    return passed.containsAll(required);
  }

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