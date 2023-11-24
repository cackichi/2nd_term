package com.example.hotelswebapp.repos;

import com.example.hotelswebapp.entity.HotelRoomEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRoomRepo extends JpaRepository<HotelRoomEntity, Long> {
    @NonNull
    Page<HotelRoomEntity> findAll(@NonNull Pageable pageable);
    @NonNull
    List<HotelRoomEntity> findAll();
    HotelRoomEntity findById(int id);
    void deleteById(int id);
    @Query("SELECT photos FROM HotelRoomEntity WHERE id = :id")
    List<String> findPhotosById(@Param("id") int id);
}
