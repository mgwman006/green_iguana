package com.maplewood.controller;

import com.maplewood.dto.response.CourseDetailsDto;
import com.maplewood.service.course.CourseService;
import com.maplewood.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/courses")
public class CourseController
{
  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CourseDetailsDto>>> getCourses(
    @RequestParam(required = false) Integer grade,
    @RequestParam(required = false) Integer semesterOrder)
  {
    List<CourseDetailsDto> result = courseService.getCourses(grade, semesterOrder);
    return ResponseEntity
      .ok()
      .body(ApiResponse.success(result,200));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CourseDetailsDto>> getCourseDetails(@PathVariable Long id)
  {
    CourseDetailsDto result = courseService.getCourseDetails(id);
    return ResponseEntity.ok(
      ApiResponse.success(result, 200)
    );
  }
}
