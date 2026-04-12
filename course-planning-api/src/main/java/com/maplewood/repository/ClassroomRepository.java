package com.maplewood.repository;

import com.maplewood.model.Classroom;
import com.maplewood.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>
{
  List<Classroom> findByRoomType(RoomType roomType);
}
