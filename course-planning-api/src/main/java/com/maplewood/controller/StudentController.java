package com.maplewood.controller;

import com.maplewood.dto.response.StudentProfileDto;
import com.maplewood.service.student.StudentService;
import com.maplewood.util.ApiResponse;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/students")
public class StudentController
{
  private final StudentService studentService;

  @GetMapping("{id}/profile")
  public ResponseEntity<ApiResponse<StudentProfileDto>> getStudent(@PathVariable Long id)
  {
    try
    {
      Result<StudentProfileDto> result = studentService.getProfile(id);
      if (!result.isSuccess())
      {
        return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.failure(result.getMessage(), 500));
      }

      return  ResponseEntity.ok(ApiResponse.success(result.getData(),200));
    }
    catch (Exception exception)
    {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure(exception.getMessage(), 400));
    }
  }
}
