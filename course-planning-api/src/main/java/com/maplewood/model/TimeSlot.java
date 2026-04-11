package com.maplewood.model;

import com.maplewood.util.enums.WeekDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "timeslots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  private WeekDay day;
  private int startHour;
  private int endHour;

  @ManyToOne
  @JoinColumn(name = "section_id")
  Section section;

  public TimeSlot(WeekDay day, int startHour, int endHour)
  {
    this.day = day;
    this.startHour = startHour;
    this.endHour = endHour;
  }
}
