package com.maplewood.dto.response;

import java.util.List;

public record SectionDto(
  Long id,
  String teacherName,
  String classroomName,
  int capacity,
  int enrolledCount,
  int availableSeats,
  String courseName,
  List<String> timeSlots
) { }
