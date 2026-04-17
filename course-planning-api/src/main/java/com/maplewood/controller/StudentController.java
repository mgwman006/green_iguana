package com.maplewood.controller;

import com.maplewood.dto.response.StudentProfileDto;
import com.maplewood.service.student.StudentService;
import com.maplewood.dto.ApiResponse;
import lombok.AllArgsConstructor;
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
    StudentProfileDto result = studentService.getProfile(id);
    return  ResponseEntity.ok(ApiResponse.success(result,200));
  }
}
