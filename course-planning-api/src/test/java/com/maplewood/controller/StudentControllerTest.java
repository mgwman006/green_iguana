package com.maplewood.controller;

import com.maplewood.dto.response.StudentProfileDto;
import com.maplewood.service.student.StudentService;
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
@WebMvcTest(StudentController.class)
class StudentControllerTest
{
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService studentService;

  @Test
  void shouldGetStudentProfile() throws Exception
  {
    StudentProfileDto dto = new StudentProfileDto(
      1L,
      "",
      "",
      0,
      "",
      1,
      2,
      Collections.emptyList(),
      Collections.emptyList()
    );

    when(studentService.getProfile(1L))
      .thenReturn(dto);

    mockMvc.perform(get("/api/v1/students/1/profile"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200))
      .andExpect(jsonPath("$.data").exists());
  }
}