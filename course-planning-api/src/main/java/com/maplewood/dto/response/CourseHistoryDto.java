package com.maplewood.dto.response;

public record CourseHistoryDto(
  Long id,
  Long courseId,
  String courseName,
  Long semesterId,
  String status
) {}
