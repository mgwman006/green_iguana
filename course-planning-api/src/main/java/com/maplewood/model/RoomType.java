package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "room_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {
  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String name; // CLASSROOM, SCIENCE_LAB, GYM, etc.

  private String description;
}
