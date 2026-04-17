package com.maplewood.service.course;

import com.maplewood.dto.response.CourseDetailsDto;
import com.maplewood.dto.response.SectionDto;
import com.maplewood.exception.ResourceNotFoundException;
import com.maplewood.model.Course;
import com.maplewood.model.Section;
import com.maplewood.repository.CourseRepository;
import com.maplewood.repository.SectionRepository;
import com.maplewood.service.section.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseService
{
  private CourseRepository courseRepository;
  private SectionRepository sectionRepository;
  private SectionService sectionService;

  public List<CourseDetailsDto> getCourses(Integer grade, Integer semesterOrder)
  {
    List<Course> courses = courseRepository.findCourses(grade, semesterOrder);

    return courses.stream()
          .map(course -> map(course, new ArrayList<>())).toList();
  }

  public CourseDetailsDto getCourseDetails(Long id)
  {
    Course course = courseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));

    List<Section> sections = sectionRepository.findByCourseId(course.getId());
    List<SectionDto> sectionDtoList = sections.stream()
      .map(s -> sectionService.map(s))
      .toList();

    return map(course,sectionDtoList);
  }

  private CourseDetailsDto map(Course course,List<SectionDto>  sectionDtoList)
  {
    return new CourseDetailsDto(
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
  }
}
