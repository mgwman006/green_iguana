package com.maplewood.controller;

import com.maplewood.dto.response.CourseDetailsDto;
import com.maplewood.service.course.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(CourseController.class)
class CourseControllerTest
{
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CourseService courseService;

  @Test
  void shouldGetCoursesWithFilters() throws Exception
  {
    when(courseService.getCourses(9, 1))
      .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/v1/courses")
        .param("grade", "9")
        .param("semesterOrder", "1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200))
      .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  void shouldGetCoursesWithoutFilters() throws Exception
  {
    when(courseService.getCourses(null, null))
      .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/v1/courses"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200));
  }

  @Test
  void shouldGetCourseDetails() throws Exception
  {
    when(courseService.getCourseDetails(1L))
      .thenReturn( new CourseDetailsDto(
          1L,
          "",
          "",
          "",
          0.0,
          0,
          0L,
          null,
          "",
          "",
          0,
          0,
          null,
          Collections.emptyList()
        ));


    mockMvc.perform(get("/api/v1/courses/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200))
      .andExpect(jsonPath("$.data").exists());
  }
}