package com.maplewood.service.student;


import com.maplewood.dto.response.StudentProfileDto;
import com.maplewood.exception.ResourceNotFoundException;
import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import com.maplewood.repository.StudentRepository;
import com.maplewood.service.enrollment.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private StudentCourseHistoryRepository historyRepository;

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private EnrollmentRepository enrollmentRepository;

  @Mock
  private EnrollmentService enrollmentService;

  @InjectMocks
  private StudentService studentService;

  private Student student;

  @BeforeEach
  void setup() {
    student = new Student();
    student.setId(1L);
    student.setFirstName("John");
    student.setLastName("Doe");
    student.setGradeLevel(10);
    student.setEmail("john@test.com");
  }

  @Test
  void shouldThrowWhenStudentNotFound() {
    when(studentRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    assertThrows(ResourceNotFoundException.class,
      () -> studentService.getProfile(1L));
  }

  @Test
  void shouldReturnEmptyGpaAndCreditsWhenNoHistory() {

    when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));
    when(historyRepository.findByStudentId(1L)).thenReturn(List.of());
    when(enrollmentRepository.findByStudent(student)).thenReturn(List.of());
    when(courseRepository.findAllById(any())).thenReturn(List.of());

    StudentProfileDto result = studentService.getProfile(1L);

    assertEquals(0.0, result.gpa());
    assertEquals(0.0, result.creditsEarned());
    assertTrue(result.courseHistory().isEmpty());
    assertTrue(result.enrollments().isEmpty());
  }

  @Test
  void shouldCalculateGpaAndCreditsCorrectly() {

    StudentCourseHistory history = new StudentCourseHistory();
    history.setId(1L);
    history.setCourseId(100L);
    history.setStatus("PASSED");

    Course course = new Course();
    course.setId(100L);
    course.setName("Math");
    course.setCredits(3.0);

    Enrollment enrollment = new Enrollment();
    Section section = new Section();
    section.setCourse(course);
    enrollment.setSection(section);

    when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));
    when(historyRepository.findByStudentId(1L)).thenReturn(List.of(history));
    when(courseRepository.findAllById(any())).thenReturn(List.of(course));
    when(enrollmentRepository.findByStudent(student)).thenReturn(List.of(enrollment));
    when(enrollmentService.map(any(), any(), any())).thenReturn(null);

    StudentProfileDto result = studentService.getProfile(1L);

    assertEquals(3.0, result.creditsEarned());
    assertEquals(4.0, result.gpa()); // GPA_SCALE_4 applied
  }

  @Test
  void shouldHandleMissingCourseInMapGracefully() {

    StudentCourseHistory history = new StudentCourseHistory();
    history.setId(1L);
    history.setCourseId(999L);
    history.setStatus("FAILED");

    when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));
    when(historyRepository.findByStudentId(1L)).thenReturn(List.of(history));
    when(courseRepository.findAllById(any())).thenReturn(List.of()); // missing course
    when(enrollmentRepository.findByStudent(student)).thenReturn(List.of());

    StudentProfileDto result = studentService.getProfile(1L);

    assertNotNull(result);
    assertEquals(0.0, result.gpa());
    assertEquals(0.0, result.creditsEarned());
  }
}