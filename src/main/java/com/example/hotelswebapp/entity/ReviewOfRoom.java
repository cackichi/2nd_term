package com.example.hotelswebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class ReviewOfRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double rating;
    private String text;
    private LocalDate datePost;
    @ManyToOne
    private UserEntity userEntity;
    @ManyToOne
    private HotelRoomEntity hotelRoomEntity;
}
