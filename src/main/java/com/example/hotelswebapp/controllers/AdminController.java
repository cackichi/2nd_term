package com.example.hotelswebapp.controllers;

import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.Reservation;
import com.example.hotelswebapp.entity.ReviewOfRoom;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.services.HotelRoomService;
import com.example.hotelswebapp.services.ReservationService;
import com.example.hotelswebapp.services.ReviewOfRoomService;
import com.example.hotelswebapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    private final UserService userService;
    private final HotelRoomService hotelRoomService;
    private final ReservationService reservationService;
    private final ReviewOfRoomService reviewOfRoomService;

    @GetMapping("/admin")
    public String adminHomePage(Model model) {
        userService.addUserInfo(model);
        List<HotelRoomEntity> rooms = hotelRoomService.findAllRooms();
        List<UserEntity> users = userService.getAllUsers();
        List<Reservation> reservations = reservationService.findAll();
        List<ReviewOfRoom> reviewOfRooms = reviewOfRoomService.findAll();
        model.addAttribute("reviews",reviewOfRooms);
        model.addAttribute("users", users);
        model.addAttribute("rooms", rooms);
        model.addAttribute("reservations", reservations);
        return "admin";
    }

    @PostMapping("/admin/delete-user")
    public String deleteUser(@RequestParam("userIdForDelete") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete-room")
    public String deleteRoomByAdmin(@RequestParam("roomIdForDelete") int id) {
        hotelRoomService.deleteRoomById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete-reservation")
    public String deleteReservationAdmin(@RequestParam("reservationIdForDelete") int id) {
        reservationService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update-role")
    public String updateRole(@RequestParam("userIdForRole") int userId, @RequestParam("newRole") String newRole) {
        userService.updateRole(userId, newRole);
        return "redirect:/admin";
    }

    @PostMapping("/delete-review-admin")
    public String deleteReview(@RequestParam("reviewIdToDelete") int id){
        reviewOfRoomService.deleteReview(id);
        return "redirect:/admin";
    }
}
