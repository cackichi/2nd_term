package com.example.hotelswebapp.services;

import com.example.hotelswebapp.entity.ERole;
import com.example.hotelswebapp.entity.HotelRoomEntity;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.exception.UsernameAlreadyExistsException;
import com.example.hotelswebapp.repos.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final HotelRoomService hotelRoomService;

    public Optional<UserEntity> findByLogin(String login){
        return userRepo.findByLogin(login);
    }

    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public void saveUser(UserEntity userEntity) throws UsernameAlreadyExistsException {
        if (isUsernameUnique(userEntity.getLogin())) {
            userEntity.setPassword(encoder().encode(userEntity.getPassword()));
            userRepo.save(userEntity);
        } else {
            throw new UsernameAlreadyExistsException("Такой логин уже существует");
        }
    }

    public boolean isUsernameUnique(String username) {
        return !userRepo.findByLogin(username).isPresent();
    }

    public UserEntity getAuthUser(String login){
        return userRepo.findByLogin(login).get();
    }

    public List<UserEntity> getAllUsers() {
        Sort sort = Sort.by("id");
        return userRepo.findAll(sort);
    }

    public void addUserInfo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        model.addAttribute("roles", roles);
    }

    public void updateRole(int userId, String newRole){
        Optional<UserEntity> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setRole(ERole.valueOf(newRole));
            userRepo.save(user);
        }
    }
    @Transactional
    public void deleteUser(int id){
        UserEntity userToDelete = userRepo.findById(id).get();
        UserEntity adminUser = userRepo.findByLogin("admin").get();
        List<HotelRoomEntity> hotelRooms = userToDelete.getHotelRoomEntities();
        for (HotelRoomEntity room : hotelRooms) {
            room.setUserEntity(adminUser);
            hotelRoomService.saveHotelRoom(room);
        }
        userRepo.delete(userToDelete);
    }
}
