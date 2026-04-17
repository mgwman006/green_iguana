package com.maplewood.controller;

import com.maplewood.dto.request.EnrollmentRequestDto;
import com.maplewood.dto.response.EnrollmentResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/enrollments")
public class EnrollmentController
{
  private final StudentService studentService;
  private final SectionService sectionService;
  private final EnrollmentService enrollmentService;


  @PostMapping("/enroll")
  public ResponseEntity<ApiResponse<EnrollmentResponseDto>> enroll(@Valid @RequestBody EnrollmentRequestDto request)
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

      Result<EnrollmentResponseDto> enrollmentResult =
        enrollmentService.enroll(
          studentResult.getData(),
          sectionResult.getData()
        );

      if (!enrollmentResult.isSuccess())
      {
        return ResponseEntity.badRequest()
          .body(ApiResponse.failure(enrollmentResult.getMessage(), 400));
      }

      EnrollmentResponseDto enrollment = enrollmentResult.getData();

      return ResponseEntity
        .created(new URI("/api/v1/enrollment/" + enrollment.id()))
        .body(ApiResponse.success(enrollment,201
        ));
    }
    catch (Exception e)
    {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure("Internal server error", 500));
    }
  }

  @DeleteMapping("/{id}/deregister")
  public ResponseEntity<ApiResponse<String>> deleteEnrollment(@PathVariable Long id)
  {
    try
    {
      Result<String> result = enrollmentService.deleteEnrollmentById(id);
      if (result.isSuccess())
      {
        return ResponseEntity.ok(ApiResponse.success(result.getData(), 200));
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    catch (Exception exception)
    {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure(exception.getMessage(), 500));
    }
  }
}
