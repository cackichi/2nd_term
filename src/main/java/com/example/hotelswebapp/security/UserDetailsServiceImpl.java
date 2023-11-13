package com.example.hotelswebapp.security;

import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.repos.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepo userRepo;
    public boolean isUsernameUnique(String username) {
        return !userRepo.findByLogin(username).isPresent();
    }
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                "Такой пользователь не найден"));
        return UserDetailsImpl.build(userEntity);
    }
}
