package com.maplewood.service.semester;

import com.maplewood.dto.response.SemesterDto;
import com.maplewood.exception.ResourceNotFoundException;
import com.maplewood.model.Semester;
import com.maplewood.repository.SemesterRepository;
import com.maplewood.config.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService
{
  /**
   * Variables
   */
  private final SemesterRepository semesterRepository;

  public SemesterDto getActiveSemester()
  {
    Semester semester = semesterRepository.findActiveSemester()
      .orElseThrow(() -> new ResourceNotFoundException(Constant.NO_SEMESTER_FOUND));
    return map(semester);
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
