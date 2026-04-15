package com.maplewood.service.student;

import com.maplewood.dto.response.*;
import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import com.maplewood.repository.StudentRepository;
import com.maplewood.service.enrollment.EnrollmentService;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService
{
  private final StudentRepository studentRepository;
  private final StudentCourseHistoryRepository historyRepository;
  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final EnrollmentService enrollmentService;

  public Result<Student> findById(Long studentId)
  {
    try
    {
      Optional<Student> optionalStudent = studentRepository.findById(studentId);
      return optionalStudent
        .map(student -> Result.success("success", student)).orElseGet(() -> Result.failure("Student not found"));

    }//try
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage()) ;
    }
  }

  public Result<StudentProfileDto> getProfile(Long studentId)
  {
    try
    {
      Optional<Student> optionalStudent = studentRepository.findById(studentId);
      if(optionalStudent.isEmpty())
      {
        return Result.failure("Student not found");
      }
      Student student = optionalStudent.get();

      //Get Course Histories
      List<StudentCourseHistory> courseHistories = historyRepository.findByStudentId(studentId);
      List<CourseHistoryDto> courseHistoryDtoList = new ArrayList<>();
      for (StudentCourseHistory history : courseHistories)
      {
        Course course = courseRepository.findById(history.getCourseId()).orElse(null);
        CourseHistoryDto courseHistoryDto = new CourseHistoryDto(
          history.getId(),
          history.getCourseId(),
          course != null? course.getName() : String.valueOf(history.getCourseId()),
          history.getSemesterId(),
          history.getStatus()
        );
        courseHistoryDtoList.add(courseHistoryDto);
      }

      double gpa = calculateGpa(courseHistories);
      double credits = calculateCredits(courseHistories);

      //Get Enrollments
      List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
      List<EnrollmentResponseDto> enrollmentResponseDtoList = new ArrayList<>();
      for (Enrollment enrollment : enrollments)
      {
        Section section = enrollment.getSection();
        EnrollmentResponseDto enrollmentResponseDto = enrollmentService.map(enrollment,student,section);
        enrollmentResponseDtoList.add(enrollmentResponseDto);
      }


      return Result
        .success(
          Constant.SUCCESS,
          new StudentProfileDto(
            student.getId(),
            student.getFirstName(),
            student.getLastName(),
            student.getGradeLevel(),
            student.getEmail(),
            gpa,
            credits,
            courseHistoryDtoList,
            enrollmentResponseDtoList
          ));
    }
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage()) ;
    }
  }

  private double calculateGpa(List<StudentCourseHistory> history)
  {
    long passed = history.stream()
      .filter(h -> "passed".equalsIgnoreCase(h.getStatus()))
      .count();

    long total = history.size();

    if (total == 0) return 0.0;

    return (double) passed / total * 4.0;
  }
  private double calculateCredits(List<StudentCourseHistory> history)
  {
    return history.stream()
      .filter(h -> "passed".equalsIgnoreCase(h.getStatus()))
      .mapToDouble(h -> {
        Course course = courseRepository.findById(h.getCourseId()).orElse(null);
        return course != null ? course.getCredits() : 0;
      })
      .sum();
  }


}
