package com.maplewood.controller;

import com.maplewood.dto.request.EnrollmentRequestDto;
import com.maplewood.dto.response.EnrollmentResponseDto;
import com.maplewood.model.Enrollment;
import com.maplewood.model.Section;
import com.maplewood.model.Student;
import com.maplewood.service.enrollment.EnrollmentService;
import com.maplewood.service.section.SectionService;
import com.maplewood.service.student.StudentService;
import com.maplewood.util.ApiResponse;
import com.maplewood.util.Result;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/enrollment")
public class EnrollmentController
{
  private final StudentService studentService;
  private final SectionService sectionService;
  private final EnrollmentService enrollmentService;


  @PostMapping("/enroll")
  public ResponseEntity<ApiResponse<EnrollmentResponseDto>> enroll(
    @Valid @RequestBody EnrollmentRequestDto request)
  {
    try
    {
      if (request == null || request.studentId() == null || request.sectionId() == null)
      {
        return ResponseEntity.badRequest()
          .body(ApiResponse.failure("Invalid request payload", 400));
      }

      Result<Student> studentResult = studentService.findById(request.studentId());
      if (!studentResult.isSuccess())
      {
        return ResponseEntity.badRequest()
          .body(ApiResponse.failure(studentResult.getMessage(), 400));
      }

      Result<Section> sectionResult = sectionService.findById(request.sectionId());
      if (!sectionResult.isSuccess())
      {
        return ResponseEntity.badRequest()
          .body(ApiResponse.failure(sectionResult.getMessage(), 400));
      }

      Result<Enrollment> enrollmentResult =
        enrollmentService.enroll(
          studentResult.getData(),
          sectionResult.getData()
        );

      if (!enrollmentResult.isSuccess())
      {
        return ResponseEntity.badRequest()
          .body(ApiResponse.failure(enrollmentResult.getMessage(), 400));
      }

      Enrollment enrollment = enrollmentResult.getData();

      return ResponseEntity
        .created(new URI("/api/v1/enrollment/" + enrollment.getId()))
        .body(ApiResponse.success(
          new EnrollmentResponseDto(
            enrollment.getId(),
            enrollment.getSection().getId(),
            enrollment.getStudent().getId(),
            0L,
            enrollment.getStatus().toString()
          ),201
        ));
    }
    catch (Exception e)
    {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure("Internal server error", 500));
    }
  }
}
