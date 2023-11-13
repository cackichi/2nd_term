package com.example.hotelswebapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewOfRoom {
    private int id;
    private double rating;
    private String text;
    private UserEntity userEntity;
    private HotelRoomEntity hotelRoomEntity;
}
