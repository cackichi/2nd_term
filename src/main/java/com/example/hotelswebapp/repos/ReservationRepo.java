package com.example.hotelswebapp.repos;

import com.example.hotelswebapp.entity.Reservation;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserEntityId(int userEntityId);
    List<Reservation> findByHotelRoomEntityId(int id);
    @NonNull
    List<Reservation> findAll();
    void deleteById(int id);
}
