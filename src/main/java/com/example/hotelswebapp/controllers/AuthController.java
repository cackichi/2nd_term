package com.example.hotelswebapp.controllers;

import com.example.hotelswebapp.entity.ERole;
import com.example.hotelswebapp.entity.UserEntity;
import com.example.hotelswebapp.exception.UsernameAlreadyExistsException;
import com.example.hotelswebapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        userService.addUserInfo(model);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        userService.addUserInfo(model);
        model.addAttribute("userEntity", new UserEntity());
        return "registration";
    }

    @PostMapping("/reg")
    public String reg(@ModelAttribute("userEntity") UserEntity userEntity, HttpServletRequest request, Model model) {
        userService.addUserInfo(model);

        String repeatPassword = request.getParameter("repeatPassword");
        if (!userEntity.getPassword().equals(repeatPassword)) {
            model.addAttribute("errorOfRepeatPassword", "Пароли не совпадают");
            return "registration";
        }
        userEntity.setRole(ERole.USER);
        try {
            userService.saveUser(userEntity);
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("errorOfExist", "Такой логин уже существует");
            return "registration";
        }
    }
}
