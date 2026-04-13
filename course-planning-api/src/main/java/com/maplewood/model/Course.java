package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String code;
  private String name;
  private String description;
  private double credits;

  @Column(name = "hours_per_week")
  private int hoursPerWeek;

  @ManyToOne
  @JoinColumn(name = "specialization_id")
  private Specialization specialization;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prerequisite_id")
  private Course prerequisite;

  @Column(name = "course_type")
  private String courseType;

  @Column(name = "grade_level_min")
  private int gradeLevelMin;

  @Column(name = "grade_level_max")
  private int gradeLevelMax;

  @Column(name = "semester_order")
  private Integer semesterOrder;

  @Column(name = "created_at")
  private LocalDateTime createAt;
}
