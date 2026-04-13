package com.maplewood.service.section;

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
}
