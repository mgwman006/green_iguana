package com.maplewood.service.section;

import com.maplewood.dto.response.SectionDto;
import com.maplewood.model.Section;
import com.maplewood.model.TimeSlot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class SectionService
{
  public SectionDto map(Section section)
  {
    return  new SectionDto(
      section.getId(),
      section.getTeacher().getFirstName() + " " + section.getTeacher().getLastName(),
      section.getClassroom().getName(),
      section.getCapacity(),
      section.getEnrollments().size(),
      section.getCapacity()-section.getEnrollments().size(),
      section.getCourse().getName(),
      section.getTimeSlots()
        .stream()
        .map(TimeSlot::toString)
        .toList()
    );
  }
}
