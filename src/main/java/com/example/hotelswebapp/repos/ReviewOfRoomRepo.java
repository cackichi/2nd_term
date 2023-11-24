package com.example.hotelswebapp.repos;

import com.example.hotelswebapp.entity.ReviewOfRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewOfRoomRepo extends JpaRepository<ReviewOfRoom,Long> {
    Page<ReviewOfRoom> findByHotelRoomEntityId(int id, Pageable pageable);
    List<ReviewOfRoom> findByHotelRoomEntityId(int id);
    void deleteById(int id);
    ReviewOfRoom findById(int id);
}
