package com.maplewood.service.course;

import com.maplewood.dto.response.CourseDetailsDto;
import com.maplewood.dto.response.SectionDto;
import com.maplewood.model.Course;
import com.maplewood.model.Section;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.SectionRepository;
import com.maplewood.service.section.SectionService;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService
{
  private CourseRepository courseRepository;
  private SectionRepository sectionRepository;
  private SectionService sectionService;

  public Result<List<CourseDetailsDto>> getCourses(Integer grade, Integer semesterOrder)
  {
    try
    {
      List<Course> courses = courseRepository.findCourses(grade, semesterOrder);
      return Result.success(
        Constant.SUCCESS,
        courses.stream()
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
            course.getSemesterOrder(),
            new ArrayList<>()
          )).toList()
      );
    }
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }

  public Result<CourseDetailsDto> getCourseDetails(Long id)
  {
    Optional<Course> optionalCourse = courseRepository.findById(id);
    if (optionalCourse.isEmpty())
    {
      return Result.failure("Course not found");
    }
    Course course = optionalCourse.get();

    List<Section> sections = sectionRepository.findByCourseId(course.getId());
    List<SectionDto> sectionDtoList = sections.stream()
      .map(s -> sectionService.map(s))
      .toList();

    CourseDetailsDto dto = new CourseDetailsDto(
      course.getId(),
      course.getCode(),
      course.getName(),
      course.getDescription(),
      course.getCredits(),
      course.getHoursPerWeek(),
      course.getSpecialization() != null ? course.getSpecialization().getId() : null,
      course.getPrerequisite() != null ? course.getPrerequisite().getId() : null,
      course.getPrerequisite() != null ? course.getPrerequisite().getName() : null,
      course.getCourseType(),
      course.getGradeLevelMin(),
      course.getGradeLevelMax(),
      course.getSemesterOrder(),
      sectionDtoList
    );

    return Result.success(Constant.SUCCESS,dto);

  }
}
