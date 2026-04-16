package com.maplewood.dto.response;


public record SemesterDto(
  Long id,
  String name,
  int year,
  int orderInYear,
  String startDate,
  String endDate,
  boolean isActive
)
{
}
