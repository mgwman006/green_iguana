package com.maplewood.service.section;

import com.maplewood.dto.response.SectionDto;
import com.maplewood.model.Section;
import com.maplewood.repository.SectionRepository;
import com.maplewood.util.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class SectionService
{
  private final SectionRepository sectionRepository;

  public Result<Section> findById(Long aLong)
  {
    try
    {
      Optional<Section> optionalSection = sectionRepository.findById(aLong);
      if (optionalSection.isEmpty())
      {
        return Result.failure("Section not found");
      }
      Section section = optionalSection.get();
      return Result.success("Section found", section);
    }//try
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }

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
        .map(timeSlot -> timeSlot.toString())
        .toList()
    );
  }
}
