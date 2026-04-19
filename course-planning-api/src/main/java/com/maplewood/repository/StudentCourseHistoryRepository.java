package com.maplewood.repository;

import com.maplewood.model.StudentCourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentCourseHistoryRepository extends JpaRepository<StudentCourseHistory, Long>
{
  @Query("""
    SELECT sch
    FROM student_course_history sch
    WHERE sch.student.id = :studentId
    AND sch.status = 'passed'
  """)
  List<StudentCourseHistory> findPassedHistory(Long studentId);
  List<StudentCourseHistory> findByStudentId(Long studentId);
}