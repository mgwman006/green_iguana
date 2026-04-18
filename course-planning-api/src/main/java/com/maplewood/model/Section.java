package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id", nullable = false)
  private Teacher teacher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classroom_id", nullable = false)
  private Classroom classroom;

  @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
  private Set<TimeSlot> timeSlots = new HashSet<>();

  private int capacity;

  @OneToMany(mappedBy = "section")
  private Set<Enrollment> enrollments = new HashSet<>();

  @Column(nullable = false, unique = true)
  private String signature;

  public String getSignature()
  {
    StringBuilder sb = new StringBuilder(64);

    sb.append(course.getId())
      .append('|')
      .append(teacher.getId())
      .append('|')
      .append(classroom.getId())
      .append('|');

    timeSlots.stream()
      .sorted(Comparator.comparing(TimeSlot::getDay)
        .thenComparing(TimeSlot::getStartHour)
        .thenComparing(TimeSlot::getEndHour))
      .forEach(slot ->
        sb.append(slot.getDay()).append('-')
          .append(slot.getStartHour()).append('-')
          .append(slot.getEndHour()).append(',')
      );

    if (!timeSlots.isEmpty()) {
      sb.setLength(sb.length() - 1);
    }

    return sb.toString();
  }
}
