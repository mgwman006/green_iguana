package com.maplewood.repository;

import com.maplewood.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long>
{

  @Query("SELECT s FROM semesters s WHERE s.isActive = true")
  Optional<Semester> findActiveSemester();
}
