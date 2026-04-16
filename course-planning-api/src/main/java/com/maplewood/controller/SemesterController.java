package com.maplewood.controller;

import com.maplewood.dto.response.SemesterDto;
import com.maplewood.service.semester.SemesterService;
import com.maplewood.util.ApiResponse;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    try
    {
      Result<SemesterDto> result = semesterService.getActiveSemester();
      if (result.isSuccess())
      {
        return ResponseEntity.ok(ApiResponse.success(result.getData(),200));
      }

      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure(result.getMessage(),500));
    }
    catch (Exception exception)
    {
      return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.failure(exception.getMessage(),500));
    }
  }
}
