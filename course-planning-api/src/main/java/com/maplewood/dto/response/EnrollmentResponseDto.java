package com.maplewood.dto.response;

public record EnrollmentResponseDto(
  Long id,   // present on success
  Long sectionId,
  Long studentId,
  Long semesterId,
  String status
) { }
