package com.example.hotelswebapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String login;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private ERole role;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
    private List<HotelRoomEntity> hotelRoomEntities;
}