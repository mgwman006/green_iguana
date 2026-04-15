package com.maplewood.dto.response;

public record EnrollmentResponseDto(
  Long id,
  SectionDto section,
  Long studentId,
  Long semesterId,
  String status
) { }
