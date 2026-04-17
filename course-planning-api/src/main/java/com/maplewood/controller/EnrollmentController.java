package com.maplewood.controller;

import com.maplewood.dto.request.EnrollmentRequestDto;
import com.maplewood.dto.response.EnrollmentResponseDto;
import com.maplewood.service.enrollment.EnrollmentService;
import com.maplewood.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/enrollments")
public class EnrollmentController
{
  private final EnrollmentService enrollmentService;


  @PostMapping("/enroll")
  public ResponseEntity<ApiResponse<EnrollmentResponseDto>> enroll(@Valid @RequestBody EnrollmentRequestDto request)
  {
    EnrollmentResponseDto enrollment = enrollmentService.enroll(request);
    return ResponseEntity.status(201)
      .body(ApiResponse.success(enrollment,201));
  }

  @DeleteMapping("/{id}/deregister")
  public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id)
  {
    enrollmentService.deleteEnrollmentById(id);
    return ResponseEntity.ok(ApiResponse.success(null,200));
  }
}
