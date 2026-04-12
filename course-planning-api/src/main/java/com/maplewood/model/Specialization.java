package com.maplewood.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity(name = "specializations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialization {

  @Id
  @GeneratedValue
  Long id;

  @Column(unique = true)
  String name;

  @OneToMany(mappedBy = "specialization")
  List<Teacher> teachers;

  @OneToMany(mappedBy = "specialization")
  List<Course> courses;

  @ManyToOne
  @JoinColumn(name = "room_type_id")
  RoomType roomType;
}
