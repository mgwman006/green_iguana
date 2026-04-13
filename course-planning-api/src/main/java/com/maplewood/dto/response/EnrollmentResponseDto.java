package com.maplewood.dto.response;

public record EnrollmentResponseDto(
  Long enrollmentId,   // present on success
  Long sectionId,
  Long studentId
)
{
}
