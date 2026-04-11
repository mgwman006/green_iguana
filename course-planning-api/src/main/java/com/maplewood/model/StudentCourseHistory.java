package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "student_course_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  @Column(name = "course_id")
  private Long courseId;

  @Column(name = "semester_id")
  private Long semesterId;

  private String status;

  @Column(name = "created_at")
  private LocalDateTime createAt;
}
