package com.maplewood.service.enrollment;


import com.maplewood.enums.WeekDay;
import com.maplewood.exception.EnrollmentValidationException;
import com.maplewood.model.*;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.StudentCourseHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class EnrollmentValidationServiceTest
{
  @Mock
  private CourseRepository courseRepository;

  @Mock
  private EnrollmentRepository enrollmentRepository;

  @Mock
  private StudentCourseHistoryRepository studentCourseHistoryRepository;

  @InjectMocks
  private EnrollmentValidationService enrollmentValidationService;

  private Student student;
  private Course course;
  private Section section;

  @BeforeEach
  void setUp()
  {
    student = new Student();
    student.setId(1L);
    student.setGradeLevel(10);

    course = new Course();
    course.setId(100L);
    course.setGradeLevelMin(9);
    course.setGradeLevelMax(12);

    section = new Section();
    section.setId(50L);
    section.setCourse(course);
    section.setTimeSlots(Set.of());

    when(enrollmentRepository.findByStudent(student))
      .thenReturn(List.of());

  }

  @Test
  void shouldPassValidationWhenEnrollmentIsValid()
  {
    assertDoesNotThrow(() ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldThrowWhenGradeLevelIsInvalid()
  {
    student.setGradeLevel(8);

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }
  @Test
  void shouldThrowWhenGradeLevelIsInvalidGreater()
  {
    student.setGradeLevel(13);

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldThrowWhenDuplicateSectionExists()
  {
    Enrollment existing = new Enrollment();
    existing.setSection(section);

    when(enrollmentRepository.findByStudent(student))
      .thenReturn(List.of(existing));

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldThrowWhenDuplicateCourseExists()
  {
    Section existingSection = new Section();
    existingSection.setId(999L);
    existingSection.setCourse(course);

    Enrollment existing = new Enrollment();
    existing.setSection(existingSection);

    when(enrollmentRepository.findByStudent(student))
      .thenReturn(List.of(existing));

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldThrowWhenPrerequisiteMissing()
  {
    Course prerequisite = new Course();
    prerequisite.setId(200L);

    course.setPrerequisite(prerequisite);

    when(courseRepository.findAll())
      .thenReturn(List.of(course));

    enrollmentValidationService.buildClosure();

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldPassWhenPrerequisiteCompleted()
  {
    Course prerequisite = new Course();
    prerequisite.setId(200L);

    course.setPrerequisite(prerequisite);

    StudentCourseHistory history = new StudentCourseHistory();
    history.setCourseId(200L);

    when(courseRepository.findAll())
      .thenReturn(List.of(course));

    when(studentCourseHistoryRepository.findPassedHistory(1L))
      .thenReturn(List.of(history));

    enrollmentValidationService.buildClosure();

    assertDoesNotThrow(() ->
      enrollmentValidationService.validateEnrollment(student, section)
    );
  }

  @Test
  void shouldThrowWhenTimeSlotsConflict_realTimeSlots() {

    // existing course + section
    Course existingCourse = new Course();
    existingCourse.setId(100L);
    existingCourse.setGradeLevelMin(9);
    existingCourse.setGradeLevelMax(12);

    TimeSlot existingSlot = new TimeSlot(WeekDay.MON, 10, 12);

    Section existingSection = new Section();
    existingSection.setId(999L);
    existingSection.setCourse(existingCourse);
    existingSection.setTimeSlots(Set.of(existingSlot));

    Enrollment existing = new Enrollment();
    existing.setSection(existingSection);

    when(enrollmentRepository.findByStudent(student))
      .thenReturn(List.of(existing));

    Course newCourse = new Course();
    newCourse.setId(200L);

    newCourse.setGradeLevelMin(9);
    newCourse.setGradeLevelMax(12);

    TimeSlot newSlot = new TimeSlot(WeekDay.MON, 11, 13);

    Section newSection = new Section();
    newSection.setId(50L);
    newSection.setCourse(newCourse);
    newSection.setTimeSlots(Set.of(newSlot));

    assertThrows(EnrollmentValidationException.class, () ->
      enrollmentValidationService.validateEnrollment(student, newSection)
    );
  }


}
