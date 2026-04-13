package com.maplewood.controller;

import com.maplewood.dto.response.CourseDetailsDto;
import com.maplewood.model.Course;
import com.maplewood.service.course.CourseService;
import com.maplewood.util.ApiResponse;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/course")
public class CourseController
{
  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CourseDetailsDto>>> getCourses(
    @RequestParam(required = false) Integer grade,
    @RequestParam(required = false) Integer semesterOrder)
  {
    try
    {
      Result<List<Course>> result = courseService.getCourses(grade, semesterOrder);
      if(!result.isSuccess())
      {
        return new ResponseEntity<>(
          ApiResponse.failure(Constant.BAD_REQUEST_MESSAGE,400),
          HttpStatus.BAD_REQUEST);
      }

      return ResponseEntity
        .ok()
        .body(ApiResponse.success(
          result
            .getData()
            .stream()
            .map(course -> new CourseDetailsDto(
              course.getId(),
              course.getCode(),
              course.getName(),
              course.getDescription(),
              course.getCredits(),
              course.getHoursPerWeek(),
              course.getSpecialization().getId(),
              course.getPrerequisite() != null ? course.getPrerequisite().getId():null,
              course.getPrerequisite() != null ? course.getPrerequisite().getName():null,
              course.getCourseType(),
              course.getGradeLevelMin(),
              course.getGradeLevelMax(),
              course.getSemesterOrder()
            )).toList(),200
        ));
    }
    catch (Exception exception)
    {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure(Constant.INTERNAL_SERVER_ERROR_MESSAGE,500));
    }

  }
}
