package ru.kiomaru.util;

import ru.kiomaru.dto.UserCreateDTO;
import ru.kiomaru.dto.UserDTO;
import ru.kiomaru.entity.User;

import java.util.ArrayList;
import java.util.List;


public class UserConverter {


    public static UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public static User convertDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setUsername(userDTO.getUsername());
        user.setRoles(userDTO.getRoles());
        return user;
    }

    public static List<UserDTO> convertUserListToDTOList(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(convertUserToDTO(user));
        }
        return userDTOList;
    }

    public static List<User> convertDTOListToUserList(List<UserDTO> userDTOList) {
        List<User> userList = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            userList.add(convertDTOToUser(userDTO));
        }
        return userList;
    }

    public static User convertCreateDTOToUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setEmail(userCreateDTO.getEmail());
        user.setUsername(userCreateDTO.getUsername());
        user.setAge(userCreateDTO.getAge());
        user.setPassword(userCreateDTO.getPassword());
        return user;
    }
}
