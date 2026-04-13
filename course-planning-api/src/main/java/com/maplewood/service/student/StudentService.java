package com.maplewood.service.student;

import com.maplewood.model.Student;
import com.maplewood.repository.StudentRepository;
import com.maplewood.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService
{
  private final StudentRepository studentRepository;

  public Result<Student> findById(Long studentId)
  {
    try
    {
      Optional<Student> optionalStudent = studentRepository.findById(studentId);
      return optionalStudent
        .map(student -> Result.success("success", student)).orElseGet(() -> Result.failure("Student not found"));

    }//try
    catch (Exception exception)
    {
      return Result.failure(exception.getMessage()) ;
    }

  }



}
