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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "section_id")
  Section section;

  public TimeSlot(WeekDay day, int startHour, int endHour)
  {
    this.day = day;
    this.startHour = startHour;
    this.endHour = endHour;
  }

  public String getKey()
  {
    return this.getDay() + "-" + this.getStartHour();
  }

  public boolean isOverlap(TimeSlot object)
  {
    if (!this.getDay().equals(object.getDay()))
    {
      return false;
    }
    return this.getStartHour() < object.getEndHour() && object.getStartHour() < this.getEndHour();
  }
}
