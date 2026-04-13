package com.maplewood.dto.request;


import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDto(
  @NotNull(message = "Student ID is required")
  Long studentId,
  @NotNull(message = "Section ID is required")
  Long sectionId

) {}