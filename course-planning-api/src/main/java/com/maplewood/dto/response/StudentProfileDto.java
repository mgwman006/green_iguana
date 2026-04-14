package com.maplewood.dto.response;

import java.util.List;

public record StudentProfileDto(
  Long id,
  String firstName,
  String lastName,
  int gradeLevel,
  String email,
  double gpa,
  double creditsEarned,
  List<CourseHistoryDto> courseHistory
)
{
}
