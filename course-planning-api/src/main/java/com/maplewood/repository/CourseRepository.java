package com.maplewood.repository;

import com.maplewood.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{

  @Query(value = """
    SELECT *
    FROM courses c
    WHERE (:grade IS NULL OR :grade BETWEEN c.grade_level_min AND c.grade_level_max)
    AND (:semesterOrder IS NULL OR c.semester_order = :semesterOrder)
    """, nativeQuery = true)
  List<Course> findCourses(Integer grade, Integer semesterOrder);

}
