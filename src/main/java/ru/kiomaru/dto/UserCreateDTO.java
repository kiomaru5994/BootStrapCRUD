package ru.kiomaru.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class UserCreateDTO {
    private String email;
    private String username;
    private int age;
    private Set<String> roles;
    private String password;
}
