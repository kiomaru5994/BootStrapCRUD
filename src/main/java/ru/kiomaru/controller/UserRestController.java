package ru.kiomaru.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kiomaru.dto.UserCreateDTO;
import ru.kiomaru.dto.UserDTO;
import ru.kiomaru.entity.Role;
import ru.kiomaru.entity.User;
import ru.kiomaru.service.RoleService;
import ru.kiomaru.service.UserService;
import ru.kiomaru.util.UserConverter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return UserConverter.convertUserListToDTOList(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            UserDTO userDTO = UserConverter.convertUserToDTO(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/create")
    public ResponseEntity<User> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        User user = UserConverter.convertCreateDTOToUser(userCreateDTO);
        Set<Long> roles = userCreateDTO.getRoles().stream()
                .map(roleService::findIdByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        userService.addUser(user, roles);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserCreateDTO userCreateDTO, @PathVariable String id) {
        User user = UserConverter.convertCreateDTOToUser(userCreateDTO);
        user.setId(Long.parseLong(id));
        Set<Long> roles = userCreateDTO.getRoles().stream()
                .map(roleService::findIdByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        userService.update(user, roles);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            UserDTO userDTO = UserConverter.convertUserToDTO(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
