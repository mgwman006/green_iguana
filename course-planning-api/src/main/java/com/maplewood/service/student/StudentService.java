package com.maplewood.service.student;

import com.maplewood.dto.response.*;
import com.maplewood.exception.ResourceNotFoundException;
import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import com.maplewood.repository.StudentRepository;
import com.maplewood.service.enrollment.EnrollmentService;
import com.maplewood.config.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService
{
  private final StudentRepository studentRepository;
  private final StudentCourseHistoryRepository historyRepository;
  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;
  private final EnrollmentService enrollmentService;

  @Transactional
  public StudentProfileDto getProfile(Long studentId)
  {
    Student student = studentRepository.findById(studentId)
      .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + Constant.DOES_NOT_EXIST));

    //Get Course Histories
    List<StudentCourseHistory> courseHistories = historyRepository.findByStudentId(studentId);
    Map<Long, Course> courseMap = buildCourseLookupMap(courseHistories);

    List<CourseHistoryDto> courseHistoryDtoList = new ArrayList<>();
    for (StudentCourseHistory history : courseHistories)
    {
      courseHistoryDtoList.add(map(history,courseMap));
    }

    double gpa = calculateGpa(courseHistories, courseMap);
    double credits = calculateEarnedCredits(courseHistories,courseMap);

    //Get Enrollments
    List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
    List<EnrollmentResponseDto> enrollmentResponseDtoList = new ArrayList<>();
    for (Enrollment enrollment : enrollments)
    {
      Section section = enrollment.getSection();
      EnrollmentResponseDto enrollmentResponseDto = enrollmentService.map(enrollment,student,section);
      enrollmentResponseDtoList.add(enrollmentResponseDto);
    }


    return new StudentProfileDto(
      student.getId(),
      student.getFirstName(),
      student.getLastName(),
      student.getGradeLevel(),
      student.getEmail(),
      gpa,
      credits,
      courseHistoryDtoList,
      enrollmentResponseDtoList
    );
  }

  private CourseHistoryDto map(StudentCourseHistory history,Map<Long, Course> courseMap)
  {
    Course course = courseMap.get(history.getCourseId());
    return new CourseHistoryDto(
      history.getId(),
      history.getCourseId(),
      course != null? course.getName() : String.valueOf(history.getCourseId()),
      history.getSemesterId(),
      history.getStatus()
    );
  }

  private double calculateGpa(List<StudentCourseHistory> history,Map<Long, Course> courseMap)
  {
    if (history == null || history.isEmpty())
    {
      return 0.0;
    }

    double passedCredits = calculateEarnedCredits(history, courseMap);

    double totalCredits = history.stream()
      .mapToDouble(h -> getCredits(courseMap, h.getCourseId()))
      .sum();

    if (totalCredits == 0)
    {
      return 0.0;
    }
    return Math.round((passedCredits / totalCredits * Constant.GPA_SCALE_4) * 100.0) / 100.0;
  }

  private Map<Long, Course> buildCourseLookupMap(List<StudentCourseHistory> history)
  {
    Set<Long> courseIds = history.stream()
      .map(StudentCourseHistory::getCourseId)
      .collect(Collectors.toSet());

    return courseRepository.findAllById(courseIds)
      .stream()
      .collect(Collectors.toMap(Course::getId, course -> course));
  }

  private double getCredits(Map<Long, Course> courseMap, Long courseId)
  {
    Course course = courseMap.get(courseId);
    return course != null ? course.getCredits() : 0.0;
  }

  private double calculateEarnedCredits(List<StudentCourseHistory> history,Map<Long, Course> courseMap)
  {
    if (history == null || history.isEmpty())
    {
      return 0.0;
    }

    return history.stream()
      .filter(h -> Constant.COURSE_STATUS_PASSED.equalsIgnoreCase(h.getStatus()))
      .mapToDouble(h -> getCredits(courseMap, h.getCourseId()))
      .sum();
  }

}
