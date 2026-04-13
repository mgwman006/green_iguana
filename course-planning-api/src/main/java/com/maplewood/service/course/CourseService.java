package com.maplewood.service.course;

import com.maplewood.model.Course;
import com.maplewood.repository.CourseRepository;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseService
{
  private CourseRepository courseRepository;

  public Result<List<Course>> getCourses(Integer grade, Integer semesterOrder)
  {
    try
    {
      List<Course> courses = courseRepository.findCourses(grade, semesterOrder);
      return Result.success(Constant.SUCCESS, courses);
    }
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }
}
