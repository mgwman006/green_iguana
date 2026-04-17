package com.maplewood.controller;

import com.maplewood.dto.response.SemesterDto;
import com.maplewood.service.semester.SemesterService;
import com.maplewood.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/semesters")
public class SemesterController
{
  private final SemesterService semesterService;

  @GetMapping()
  public ResponseEntity<ApiResponse<SemesterDto>> getActiveSemester()
  {
    SemesterDto semesterDto = semesterService.getActiveSemester();
    return ResponseEntity.ok(ApiResponse.success(semesterDto,200));
  }
}
