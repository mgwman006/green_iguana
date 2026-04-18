package com.maplewood.model;

import com.maplewood.config.Constant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;

  @Column(name = "grade_level")
  private int gradeLevel;

  @Column(name = "enrollment_year")
  private int enrollmentYear;

  @Column(name = "expected_graduation_year")
  private int expectedGraduationYear;

  private String status;

  @OneToMany(mappedBy = "student")
  Set<Enrollment> enrollments = new HashSet<>();

  @Column(name = "created_at")
  private LocalDateTime createAt;

  public boolean hasReachedCoursesLimit()
  {
    return this.enrollments.size()>=Constant.STUDENT_COURSES_LIMIT;
  }
}