package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @ManyToOne
  @JoinColumn(name = "room_type_id")
  private RoomType roomType;

  private int capacity;

  private String equipment;

  private int floor;

  @Column(name = "created_at")
  LocalDateTime createdAt;
}