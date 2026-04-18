package com.maplewood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maplewood.dto.request.EnrollmentRequestDto;
import com.maplewood.dto.response.EnrollmentResponseDto;
import com.maplewood.dto.response.SectionDto;
import com.maplewood.enums.EnrollmentStatus;
import com.maplewood.service.enrollment.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest
{
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private EnrollmentService enrollmentService;

  @Test
  void shouldEnrollSuccessfully() throws Exception
  {
    EnrollmentRequestDto request = new EnrollmentRequestDto(
      1L,
      2L
    );

    EnrollmentResponseDto response = new EnrollmentResponseDto(
      1L,
      new SectionDto(
        1L,
        "Maneno",
        "Lab 1",
        9,
        1,
        2,
        "Math",
        new ArrayList<>()
      ),
      1L,
      EnrollmentStatus.ACTIVE.toString()
    );

    when(enrollmentService.enroll(any())).thenReturn(response);

    mockMvc.perform(post("/api/v1/enrollments/enroll")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(201));
  }

  @Test
  void shouldDeleteEnrollmentSuccessfully() throws Exception
  {
    doNothing().when(enrollmentService).deleteEnrollmentById(5L);

    mockMvc.perform(delete("/api/v1/enrollments/5/deregister"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.statusCode").value(200));
  }
}