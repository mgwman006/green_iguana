package com.maplewood.controller;

import com.maplewood.dto.response.SemesterDto;
import com.maplewood.service.semester.SemesterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(SemesterController.class)
class SemesterControllerTest
{
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SemesterService semesterService;

  @Test
  void shouldGetActiveSemester() throws Exception
  {
    SemesterDto dto = new SemesterDto(
      1L,
      "",
      1,
      1,
      "18/04/2026",
      "20/04/2026",
      true
    );

    when(semesterService.getActiveSemester())
      .thenReturn(dto);

    mockMvc.perform(get("/api/v1/semesters"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200))
      .andExpect(jsonPath("$.data").exists());
  }
}