package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;

  @Column(name = "max_daily_hours")
  private int maxDailyHours;

  @ManyToOne
  @JoinColumn(name = "specialization_id")
  private Specialization specialization;

  @Column(name = "created_at")
  private LocalDateTime createAt;
}
