package com.maplewood.service.semester;

import com.maplewood.dto.response.SemesterDto;
import com.maplewood.model.Semester;
import com.maplewood.repository.SemesterRepository;
import com.maplewood.util.Constant;
import com.maplewood.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SemesterService
{
  /**
   * Variables
   */
  private final SemesterRepository semesterRepository;

  public Result<SemesterDto> getActiveSemester()
  {
    try
    {
      Optional<Semester> optionalSemester = semesterRepository.findActiveSemester();
      if (optionalSemester.isEmpty())
      {
        return Result.failure(Constant.NO_SEMESTER_FOUND);
      }
      SemesterDto semesterDto = map(optionalSemester.get());
      return Result.success(Constant.SUCCESS, semesterDto);
    }
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage());
    }
  }

  public SemesterDto map(Semester semester)
  {
    return new SemesterDto(
      semester.getId(),
      semester.getName(),
      semester.getYear(),
      semester.getOrderInYear(),
      semester.getStartDate(),
      semester.getEndDate(),
      semester.isActive()
    );
  }
}
