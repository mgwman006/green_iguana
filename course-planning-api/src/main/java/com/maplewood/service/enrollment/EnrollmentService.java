package com.maplewood.service.enrollment;

import com.maplewood.dto.response.EnrollmentResponseDto;
import com.maplewood.model.*;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.service.section.SectionService;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import com.maplewood.util.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService
{
  /**
   * Variables
   */
  private final EnrollmentRepository enrollmentRepository;
  private final EnrollmentValidationService enrollmentValidationService;
  private final SectionService sectionService;

  public Result<String> deleteEnrollmentById(Long id)
  {
    try
    {
      enrollmentRepository.deleteById(id);
      return Result.success(Constant.SUCCESS,null);
    }
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }

  public Result<EnrollmentResponseDto> enroll(Student student, Section section)
  {
    // validate FIRST
    Result<Boolean> validation = enrollmentValidationService.validate(student, section);
    if (!validation.isSuccess())
    {
      return Result.failure(validation.getMessage());
    }

    // duplicate check
    Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndSectionId(student.getId(), section.getId());
    if (existing.isPresent())
    {
      return Result.failure("Already enrolled");
    }

    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setSection(section);
    enrollment.setStatus(EnrollmentStatus.ACTIVE);
    enrollment.setEnrolledAt(LocalDateTime.now());

    enrollment = enrollmentRepository.save(enrollment);
    student = enrollment.getStudent();
    section = enrollment.getSection();
    return Result.success(
      Constant.SUCCESS,
      map(enrollment,student,section)
    );
  }

  public EnrollmentResponseDto map(Enrollment enrollment, Student student, Section section)
  {
    return
      new EnrollmentResponseDto(
        enrollment.getId(),
        sectionService.map(section),
        student.getId(),
        0L,
        enrollment.getStudent().getStatus()
      );
  }
}
