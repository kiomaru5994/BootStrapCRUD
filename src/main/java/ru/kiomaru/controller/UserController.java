package ru.kiomaru.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kiomaru.entity.User;
import ru.kiomaru.service.UserService;


import java.security.Principal;


@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userPage(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "user";
    }
}
