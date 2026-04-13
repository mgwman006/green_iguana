package com.maplewood.dto.response;

public record CourseDetailsDto(
  Long id,
  String code,
  String name,
  String description,
  double credits,
  int hoursPerWeek,
  Long specializationId,
  Long prerequisiteId,
  String prerequisiteName,
  String courseType,
  int gradeLevelMin,
  int gradeLevelMax,
  Integer semesterOrder
) {}