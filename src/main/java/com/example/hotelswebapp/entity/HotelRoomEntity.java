package com.example.hotelswebapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "hotel_room")
public class HotelRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<String> photos;
    private int amountOfSleepers;
    private String description;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<String> services;
    private String name;
    private int pricePerDay;
    @ManyToOne(optional = false)
    private UserEntity userEntity;
    @OneToMany(mappedBy = "hotelRoomEntity", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();
    @OneToMany(mappedBy = "hotelRoomEntity", cascade = CascadeType.REMOVE)
    private List<ReviewOfRoom> reviewOfRooms = new ArrayList<>();
}
