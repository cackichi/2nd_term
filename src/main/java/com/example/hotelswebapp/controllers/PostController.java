package com.example.hotelswebapp.controllers;

import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.Reservation;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.services.HotelRoomService;
import com.example.hotelswebapp.services.ReservationService;
import com.example.hotelswebapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class PostController {
    UserService userService;
    ReservationService reservationService;
    HotelRoomService hotelRoomService;

    @GetMapping("/posting")
    public String postingAdvertisement(Model model) {
        userService.addUserInfo(model);
        model.addAttribute("hotelRoomEntity", new HotelRoomEntity());
        return "posting";
    }

    @PostMapping("/posting")
    public String postingAdvertisement(
            @ModelAttribute("hotelRoomEntity") HotelRoomEntity hotelRoomEntity
            , @RequestParam("services") String services,
            @RequestParam("images") MultipartFile[] files, Model model) {
        userService.addUserInfo(model);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.getAuthUser(auth.getName());
        List<String> photos = new ArrayList<>();
        List<String> serviceList = List.of(services.split(","));
        String check = hotelRoomService.checkPosting(serviceList, model, hotelRoomEntity);
        if (!check.equals("1")) return check;
        hotelRoomEntity.setServices(serviceList);
        hotelRoomEntity.setPhotos(hotelRoomService.uploadPhoto(files,photos));
        hotelRoomEntity.setUserEntity(userEntity);
        hotelRoomService.saveHotelRoom(hotelRoomEntity);
        return "redirect:/posting";
    }

    @GetMapping("/post/{id}")
    public String postById(Model model, @PathVariable int id) {
        HotelRoomEntity hotelRoomEntity = hotelRoomService.findRoomById(id);
        model.addAttribute("room", hotelRoomEntity);
        List<Reservation> reservations = reservationService.findByRoomId(id);
        model.addAttribute("reservations", reservations);
        userService.addUserInfo(model);
        return "post";
    }

    @PostMapping("/post/{id}")
    public String reservation(Model model, @PathVariable int id,
                              @RequestParam(name = "checkInDate", required = false) LocalDate checkInDate,
                              @RequestParam(name = "checkOutDate", required = false) LocalDate checkOutDate) {
        userService.addUserInfo(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HotelRoomEntity room = hotelRoomService.findRoomById(id);
        model.addAttribute("room", room);
        List<Reservation> reservations = reservationService.findByRoomId(id);
        model.addAttribute("reservations", reservations);
        if (checkInDate != null && checkOutDate != null) {
            if (checkOutDate.isBefore(checkInDate) || checkOutDate.equals(checkInDate)) {
                model.addAttribute("errorOfIncorrectDate", "Выберите дату правильно");
                return "post";
            }
            for (Reservation reservation : room.getReservations()) {
                if (!((checkInDate.isBefore(reservation.getDateOfReservation()) && checkOutDate.isBefore(reservation.getDateOfReservation())) ||
                        (checkInDate.isAfter(reservation.getDateOfEviction()) && checkOutDate.isAfter(reservation.getDateOfEviction())))) {
                    model.addAttribute("errorOfExistReservation", "На эти даты уже забронировано");
                    return "post";
                }
            }
            UserEntity userEntity = userService.getAuthUser(auth.getName());
            Reservation newReservation = new Reservation();
            newReservation.setDateOfReservation(checkInDate);
            newReservation.setDateOfEviction(checkOutDate);
            newReservation.setUserEntity(userEntity);
            newReservation.setHotelRoomEntity(room);
            reservationService.save(newReservation);
        }
        return "post";
    }
}
