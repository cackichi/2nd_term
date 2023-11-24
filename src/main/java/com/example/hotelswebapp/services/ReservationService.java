package com.example.hotelswebapp.services;

import com.example.hotelswebapp.entity.Reservation;
import com.example.hotelswebapp.repos.ReservationRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepo reservationRepo;
    public List<Reservation> findByUserId(int id){
        return reservationRepo.findByUserEntityId(id);
    }
    public void save(Reservation reservation){
        reservationRepo.save(reservation);
    }
    public List<Reservation> findByRoomId(int id){
        return reservationRepo.findByHotelRoomEntityId(id);
    }
    @Transactional
    public void delete(int id){
        reservationRepo.deleteById(id);
    }
    @Transactional
    public void cleanUpReservations(){
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = findAll();
        for(Reservation reservation: reservations){
            if(reservation.getDateOfEviction().isBefore(today)){
                reservationRepo.delete(reservation);
            }
        }
    }
    public List<Reservation> findAll(){
        return reservationRepo.findAll();
    }
}
