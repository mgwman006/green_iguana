package com.maplewood.service.scheduler;


import com.maplewood.model.Teacher;
import com.maplewood.model.TimeSlot;
import com.maplewood.repository.TeacherRepository;
import com.maplewood.util.enums.WeekDay;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for managing teacher availability during schedule generation.
 *
 * <p>Constraints enforced:
 * <ul>
 *     <li>No overlapping time slots</li>
 *     <li>Maximum 4 teaching hours per day</li>
 * </ul>
 *
 * <p>This service uses in-memory structures and is only valid during scheduling.</p>
 */
@Service
@AllArgsConstructor
public class TeacherAvailabilityService {

  /**
   * Map of teacherId → (day → assigned hours).
   */
  private final Map<Long, Map<String, Integer>> teacherDailyHours = new HashMap<>();

  /**
   * Map of teacherId → occupied time slot keys.
   */
  private final Map<Long, Set<String>> teacherSchedule = new HashMap<>();

  /**
   * Initializes teacher availability state.
   *
   * @param teachers teaches to initialize state
   */
  public void initialize(List<Teacher> teachers)
  {
    for (Teacher teacher : teachers)
    {
      Map<String, Integer> daily = new HashMap<>();
      for (WeekDay day : WeekDay.values())
      {
        daily.put(day.name(), 0);
      }

      teacherDailyHours.put(teacher.getId(), daily);
      teacherSchedule.put(teacher.getId(), new HashSet<>());
    }
  }

  /**
   * Checks whether a teacher is available for a given set of time slots.
   *
   * @param teacher teacher to check
   * @param slots   requested time slots
   * @return true if teacher is available, false otherwise
   */
  public boolean isAvailable(Teacher teacher, Set<TimeSlot> slots)
  {

    Map<String, Integer> daily = teacherDailyHours.get(teacher.getId());
    Set<String> occupied = teacherSchedule.get(teacher.getId());

    // Check time slot conflicts
    for (TimeSlot slot : slots)
    {
      if (occupied.contains(slot.getKey()))
      {
        return false;
      }
    }

//    boolean conflict = existingSlots.stream().anyMatch(existing ->
//      existing.getDay().equals(slot.getDay()) &&
//        existing.getStartHour() < slot.getEndHour() &&
//        slot.getStartHour() < existing.getEndHour()
//    );

    // Check daily hour limits (max 4 hours per day)
    Map<String, Long> hoursPerDay = slots.stream()
      .collect(Collectors.groupingBy(
        slot -> slot.getDay().toString(),
        Collectors.counting()
      ));

    for (Map.Entry<String, Long> entry : hoursPerDay.entrySet())
    {
      String day = entry.getKey();
      int newHours = entry.getValue().intValue();

      int current = daily.getOrDefault(day, 0);

      if (current + newHours > 4)
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Reserves a teacher for the given time slots.
   *
   * @param teacher teacher to assign
   * @param slots   assigned time slots
   */
  public void reserve(Teacher teacher, Set<TimeSlot> slots) {

    Map<String, Integer> daily = teacherDailyHours.get(teacher.getId());
    Set<String> occupied = teacherSchedule.get(teacher.getId());

    for (TimeSlot slot : slots)
    {
      occupied.add(slot.getKey());
    }

    Map<String, Long> hoursPerDay = slots.stream()
      .collect(Collectors.groupingBy(
        slot -> slot.getDay().toString(),
        Collectors.counting()
      ));

    for (Map.Entry<String, Long> entry : hoursPerDay.entrySet())
    {
      String day = entry.getKey();
      int add = entry.getValue().intValue();

      daily.put(day, daily.getOrDefault(day, 0) + add);
    }
  }
}
