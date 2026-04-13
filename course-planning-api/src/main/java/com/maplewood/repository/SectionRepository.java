package com.maplewood.repository;

import com.maplewood.model.Classroom;
import com.maplewood.model.Course;
import com.maplewood.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long>
{
  List<Section> findByClassroom(Classroom classroom);

  Section findByCourse(Course newCourse);
}
