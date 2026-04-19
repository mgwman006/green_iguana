package com.maplewood.repository;

import com.maplewood.model.Enrollment;
import com.maplewood.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>
{
  List<Enrollment> findByStudent(Student student);
}


