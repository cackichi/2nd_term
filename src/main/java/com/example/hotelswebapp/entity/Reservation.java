package com.example.hotelswebapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDate dateOfReservation;
    private LocalDate dateOfEviction;
    @ManyToOne
    private UserEntity userEntity;
    @ManyToOne
    private HotelRoomEntity hotelRoomEntity;
}
