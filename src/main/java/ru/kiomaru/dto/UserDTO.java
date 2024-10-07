package ru.kiomaru.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kiomaru.entity.Role;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;
    private String email;
    private String username;
    private int age;
    private Set<Role> roles;
}
