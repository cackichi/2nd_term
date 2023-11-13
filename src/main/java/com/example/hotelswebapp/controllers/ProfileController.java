package com.example.hotelswebapp.controllers;

import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.services.HotelRoomService;
import com.example.hotelswebapp.services.ReservationService;
import com.example.hotelswebapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProfileController {
    HotelRoomService hotelRoomService;
    ReservationService reservationService;
    UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        userService.addUserInfo(model);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.getAuthUser(auth.getName());
        int fullPrice = hotelRoomService.getFullPriceForUser(user.getId());
        model.addAttribute("userEntity", user);
        model.addAttribute("fullPrice", fullPrice);
        return "profile";
    }

    @PostMapping("/delete-room")
    public String deleteRoomByUser(@RequestParam("roomIdForDelete") int id) {
        hotelRoomService.deleteRoomById(id);
        return "redirect:/profile";
    }

    @PostMapping("/delete-photo")
    public String deletePhoto(@RequestParam("roomId") int id,
                               @RequestParam("photoToDelete") String photo,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("roomIdForEdit",id);
        hotelRoomService.deletePhotoFromRoom(photo, id);
        return "redirect:/edit-room";
    }

    @PostMapping("/delete-reservation")
    public String deleteReservationUser(@RequestParam("reservationIdForDelete") int id) {
        reservationService.delete(id);
        return "redirect:/profile";
    }

    @GetMapping("/edit-room")
    public String editRoomGet(Model model, @RequestParam("roomIdForEdit") int id, RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if(auth.getName().equals(hotelRoomService.findRoomById(id).getUserEntity().getLogin()) || roles.contains("ADMIN")) {
            userService.addUserInfo(model);
            HotelRoomEntity hotelRoomEntity = hotelRoomService.findRoomById(id);
            List<String> services = hotelRoomEntity.getServices();
            String total_services = services.get(0);
            for (int i = 1; i < services.size(); i++) {
                total_services = total_services + "," + services.get(i);
            }
            model.addAttribute("photos", hotelRoomEntity.getPhotos());
            model.addAttribute("total_services", total_services);
            model.addAttribute("room", hotelRoomEntity);
            return "edit";
        } else {
            redirectAttributes.addFlashAttribute("errorOfNotYourRoom","Вы не имеете прав доступа к этой сслыке");
            return "redirect:/";
        }
    }

    @PostMapping("/edit-room-post")
    public String editRoomPost(@ModelAttribute("room") HotelRoomEntity room,
                               @RequestParam("services") String services,
                               @RequestParam(name = "images", required = false) MultipartFile[] files,
                               @RequestParam("roomIdForEdit") int id,
                               Model model, RedirectAttributes redirectAttributes){
        userService.addUserInfo(model);
        HotelRoomEntity editRoom = hotelRoomService.findRoomById(id);
        List<String> photos = editRoom.getPhotos();
        List<String> serviceList = List.of(services.split(","));
        String check = hotelRoomService.checkPosting(serviceList, model, room);
        if (!check.equals("1")) return check;
        room.setServices(serviceList);
        room.setPhotos(hotelRoomService.uploadPhoto(files,photos));
        room.setId(editRoom.getId());
        room.setUserEntity(editRoom.getUserEntity());
        room.setReservations(editRoom.getReservations());
        hotelRoomService.saveHotelRoom(room);

        model.addAttribute("photos", photos);
        model.addAttribute("total_services",services);
        model.addAttribute("room",room);
        redirectAttributes.addAttribute("roomIdForEdit",id);
        return "redirect:/edit-room";
    }
}
