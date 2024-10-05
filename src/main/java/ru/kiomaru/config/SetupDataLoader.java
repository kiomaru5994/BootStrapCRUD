package ru.kiomaru.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kiomaru.entity.Role;
import ru.kiomaru.entity.User;
import ru.kiomaru.exception.RoleNotFoundException;
import ru.kiomaru.exception.UserNotFoundException;
import ru.kiomaru.service.RoleService;
import ru.kiomaru.service.UserService;


import java.util.List;
import java.util.stream.Stream;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final RoleService roleService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Role createRoleIfNotFound(String roleStr) {
        try {
            return roleService.findByName(roleStr);
        } catch (RoleNotFoundException e) {
            Role newRole = new Role();
            newRole.setName(roleStr);
            roleService.save(newRole);
            return newRole;
        }
    }

    @Transactional
    public User createUserIfNotFound(String userStr, List<Role> roles) {
        try {
            return userService.findByUsername(userStr);
        } catch (UserNotFoundException e) {
            User newUser = new User(userStr);
            newUser.setPassword(passwordEncoder.encode(userStr));
            if (userStr.equals("admin")) {
                newUser.addRole(roles.get(0));
                newUser.addRole(roles.get(1));
                newUser.setAge(20);
                newUser.setEmail("admin@admin.com");
            } else if (userStr.equals("user")) {
                newUser.addRole(roles.get(1));
                newUser.setEmail("user@user.com");
                newUser.setAge(20);
            }
            return newUser;
        }
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        if (alreadySetup) {
            return;
        }

        List<Role> roles = Stream.of("ROLE_ADMIN", "ROLE_USER").map(this::createRoleIfNotFound).toList();
        List<User> users = Stream.of("admin", "user").map(userStr -> createUserIfNotFound(userStr, roles)).toList();

        userService.saveAll(users);

        alreadySetup = true;
    }

}
