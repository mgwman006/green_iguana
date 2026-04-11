package com.maplewood.util;


import com.maplewood.model.TimeSlot;
import com.maplewood.util.enums.WeekDay;
import java.util.List;

/**
 * This is the builder class that run during the app initialization and populated a predefined schedule pattern of Time slots
 * This can be more configurable in the future depending on course workload configuration
 */
public class SchedulePatternProvider {

  public static List<List<TimeSlot>> generateOptions(int hours) {

    if (hours == 2)
    {
      return List.of(
        List.of(new TimeSlot(WeekDay.TUE, 10, 11),
          new TimeSlot(WeekDay.THU, 10, 11)),
        List.of(new TimeSlot(WeekDay.TUE, 14, 15),
          new TimeSlot(WeekDay.THU, 14, 15))
      );
    }

    if (hours == 3)
    {
      return List.of(
        List.of(new TimeSlot(WeekDay.MON, 9, 10),
          new TimeSlot(WeekDay.WED, 9, 10),
          new TimeSlot(WeekDay.FRI, 9, 10)),
        List.of(new TimeSlot(WeekDay.MON, 13, 14),
          new TimeSlot(WeekDay.WED, 13, 14),
          new TimeSlot(WeekDay.FRI, 13, 14))
      );
    }

    if (hours == 6)
    {
      return List.of(
        List.of(
          new TimeSlot(WeekDay.MON, 9, 10),
          new TimeSlot(WeekDay.WED, 9, 10),
          new TimeSlot(WeekDay.FRI, 9, 10),
          new TimeSlot(WeekDay.TUE, 10, 11),
          new TimeSlot(WeekDay.THU, 10, 11),
          new TimeSlot(WeekDay.MON, 11, 12)
        ),
        List.of(
          new TimeSlot(WeekDay.MON, 13, 14),
          new TimeSlot(WeekDay.WED, 13, 14),
          new TimeSlot(WeekDay.FRI, 13, 14),
          new TimeSlot(WeekDay.TUE, 14, 15),
          new TimeSlot(WeekDay.THU, 14, 15),
          new TimeSlot(WeekDay.MON, 9, 10)
        )
      );
    }

    return List.of();
  }
}
