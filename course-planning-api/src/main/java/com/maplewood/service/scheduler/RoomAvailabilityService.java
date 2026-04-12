package com.maplewood.service.scheduler;

import com.maplewood.model.Classroom;
import com.maplewood.model.TimeSlot;
import com.maplewood.repository.ClassroomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service responsible for managing classroom availability during schedule generation.
 *
 * <p>This service maintains an in-memory representation of room occupancy,
 * ensuring that no classroom is double-booked for the same time slot.</p>
 *
 * <p>NOTE:
 * This service is used only during schedule generation (startup phase)
 * and does NOT persist any data.</p>
 */
@Service
@AllArgsConstructor
public class RoomAvailabilityService {

  private final Map<Long, Set<String>> roomSchedule = new HashMap<>();

  /**
   * Initializes room availability state.
   *
   * @param rooms classrooms to initialize state
   */
  public void initialize(List<Classroom> rooms)
  {
    for (Classroom room : rooms)
    {
      roomSchedule.put(room.getId(), new HashSet<>());
    }
  }

  /**
   * Checks if a classroom is available for all given time slots.
   *
   * @param room  classroom to check
   * @param slots requested time slots
   * @return true if room is free for all slots, false otherwise
   */
  public boolean isAvailable(Classroom room, Set<TimeSlot> slots)
  {
    Set<String> occupied = roomSchedule.get(room.getId());

    for (TimeSlot slot : slots)
    {
      if (occupied.contains(slot.getKey())) return false;
    }
    return true;
  }

  /**
   * Reserves a classroom for the given time slots.
   *
   * @param room  classroom to reserve
   * @param slots time slots to occupy
   */
  public void reserve(Classroom room, Set<TimeSlot> slots)
  {
    Set<String> occupied = roomSchedule.get(room.getId());
    for (TimeSlot slot : slots)
    {
      occupied.add(slot.getKey());
    }
  }
}
