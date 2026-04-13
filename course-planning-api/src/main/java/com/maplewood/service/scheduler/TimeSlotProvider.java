package com.maplewood.service.scheduler;


import com.maplewood.model.TimeSlot;
import com.maplewood.util.enums.WeekDay;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the builder class that run during the app initialization and populated a predefined schedule pattern of Time slots
 * This can be more configurable in the future depending on course workload configuration
 */
public class TimeSlotProvider
{

  private TimeSlotProvider()
  {
    throw new RuntimeException("Can not be initiated");
  }

  public static List<Set<TimeSlot>> getPatterns(int hours)
  {
    List<Set<TimeSlot>> patterns = new ArrayList<>();

    if (hours == 2)
    {
      patterns.add(getTimeSlotsForTwoHoursWorkLoad());
      return patterns;
    }

    if (hours == 3)
    {
      patterns.add(getTimeSlotsForThreeHoursWorkLoad());
      return patterns;
    }

    if (hours == 4)
    {
      patterns.add(getTimeSlotsForFourHoursWorkLoad());
      return patterns;
    }

    if (hours == 5)
    {
      patterns.add(getTimeSlotsForFiveHoursWorkLoad());
      return patterns;
    }

    if (hours == 6)
    {
      patterns.add(getTimeSlotsForSixHoursWorkLoad());
      return patterns;
    }

    return List.of();
  }

  private static Set<TimeSlot> getTimeSlotsForSixHoursWorkLoad()
  {
    Set<TimeSlot> slots = new HashSet<>();

    // First Pattern
    TimeSlot timeSlot1 = new TimeSlot(WeekDay.MON, 9, 10);
    slots.add(timeSlot1);
    TimeSlot timeSlot2 = new TimeSlot(WeekDay.TUE, 9, 10);
    slots.add(timeSlot2);
    TimeSlot timeSlot3 = new TimeSlot(WeekDay.WED, 9, 10);
    slots.add(timeSlot3);
    TimeSlot timeSlot4 = new TimeSlot(WeekDay.TUE, 10, 11);
    slots.add(timeSlot4);
    TimeSlot timeSlot5 = new TimeSlot(WeekDay.FRI, 10, 12);
    slots.add(timeSlot5);
    return slots;
  }

  private static Set<TimeSlot> getTimeSlotsForThreeHoursWorkLoad()
  {
    Set<TimeSlot> slots = new HashSet<>();
    // First Pattern
    TimeSlot timeSlot1 = new TimeSlot(WeekDay.MON, 9, 10);
    slots.add(timeSlot1);
    TimeSlot timeSlot2 = new TimeSlot(WeekDay.WED, 9, 10);
    slots.add(timeSlot2);
    TimeSlot timeSlot3 = new TimeSlot(WeekDay.FRI, 9, 10);
    slots.add(timeSlot3);


    return slots;
  }

  private static Set<TimeSlot> getTimeSlotsForTwoHoursWorkLoad()
  {
    Set<TimeSlot> slots = new HashSet<>();
    // First Pattern
    TimeSlot timeSlot1 = new TimeSlot(WeekDay.TUE, 10, 11);
    slots.add(timeSlot1);
    TimeSlot timeSlot2 = new TimeSlot(WeekDay.THU, 10, 11);
    slots.add(timeSlot2);

    return slots;
  }

  private static Set<TimeSlot> getTimeSlotsForFourHoursWorkLoad()
  {
    Set<TimeSlot> slots = new HashSet<>();

    // Pattern 1 (morning spread)
    slots.add(new TimeSlot(WeekDay.MON, 9, 10));
    slots.add(new TimeSlot(WeekDay.TUE, 9, 10));
    slots.add(new TimeSlot(WeekDay.WED, 9, 10));
    slots.add(new TimeSlot(WeekDay.THU, 9, 10));

    return slots;
  }

  private static Set<TimeSlot> getTimeSlotsForFiveHoursWorkLoad()
  {
    Set<TimeSlot> slots = new HashSet<>();

    slots.add(new TimeSlot(WeekDay.MON, 9, 10));
    slots.add(new TimeSlot(WeekDay.TUE, 9, 10));
    slots.add(new TimeSlot(WeekDay.WED, 9, 10));
    slots.add(new TimeSlot(WeekDay.THU, 9, 10));
    slots.add(new TimeSlot(WeekDay.FRI, 9, 10));

    return slots;
  }
}
