package ru.kiomaru.service;


import ru.kiomaru.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();

    void addUser(User user, Set<Long> roleIds);

    User findById(Long id);

    void saveAll(List<User> users);

    void update(User updatedUser, Set<Long> roleIds);

    void delete(Long id);

    User findByUsername(String username);
}
