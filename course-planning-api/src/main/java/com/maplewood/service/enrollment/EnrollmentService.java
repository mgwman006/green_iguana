package com.maplewood.service.enrollment;

import com.maplewood.dto.request.EnrollmentRequestDto;
import com.maplewood.dto.response.EnrollmentResponseDto;
import com.maplewood.exception.ResourceNotFoundException;
import com.maplewood.model.*;
import com.maplewood.repository.EnrollmentRepository;
import com.maplewood.repository.SectionRepository;
import com.maplewood.repository.StudentRepository;
import com.maplewood.service.section.SectionService;
import com.maplewood.config.Constant;
import com.maplewood.enums.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

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
  private final StudentRepository studentRepository;
  private final SectionRepository sectionRepository;

  @Transactional
  public void deleteEnrollmentById(Long id)
  {
    Enrollment enrollment = enrollmentRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Enrollment with id " + id + Constant.DOES_NOT_EXIST));
    enrollmentRepository.delete(enrollment);
  }

  @Transactional
  public EnrollmentResponseDto enroll(EnrollmentRequestDto request)
  {
    Student student = studentRepository.findById(request.studentId())
      .orElseThrow(() -> new ResourceNotFoundException("Student with id " + request.studentId() + " does not exist"));

    Section section = sectionRepository.findById(request.sectionId())
      .orElseThrow(() -> new ResourceNotFoundException("Section with id " + request.sectionId() + " does not exist"));


    //Validation
    enrollmentValidationService.validateEnrollment(student, section);

    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setSection(section);
    enrollment.setStatus(EnrollmentStatus.ACTIVE);
    enrollment.setEnrolledAt(LocalDateTime.now());

    enrollment = enrollmentRepository.save(enrollment);
    return  map(enrollment, student, section);
  }

  public EnrollmentResponseDto map(Enrollment enrollment, Student student, Section section)
  {
    return
      new EnrollmentResponseDto(
        enrollment.getId(),
        sectionService.map(section),
        student.getId(),
        enrollment.getStudent().getStatus()
      );
  }
}
