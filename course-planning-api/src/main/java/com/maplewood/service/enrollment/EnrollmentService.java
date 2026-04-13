package com.maplewood.service.enrollment;

import com.maplewood.model.*;
import com.maplewood.repository.EnrollmentRepository;
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
  private final EnrollmentRepository enrollmentRepository;
  private final EnrollmentValidationService enrollmentValidationService;


  public Result<Enrollment> enroll(Student student, Section section)
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

    Enrollment saved = enrollmentRepository.save(enrollment);
    return Result.success(Constant.SUCCESS,saved);
  }
}
