package com.maplewood.dto.response;

import java.util.List;

public record SectionDto(
  Long id,
  String teacherName,
  String classroomName,
  int capacity,
  int enrolledCount,
  int availableSeats,
  List<String> timeSlots
) {}
