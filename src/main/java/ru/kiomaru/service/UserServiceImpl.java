package ru.kiomaru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kiomaru.entity.User;
import ru.kiomaru.exception.UserNotFoundException;
import ru.kiomaru.repository.UserRepository;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void addUser(User user, Set<Long> roleIds) {
        if(roleIds != null) {
            user.setRoles(roleIds.stream()
                    .map(roleService::findById)
                    .collect(Collectors.toSet()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("couldn't find user with email " + id));
    }


    @Override
    @Transactional
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    @Transactional
    public void update(User updatedUser, Set<Long> roleIds) {
        User user = findById(updatedUser.getId());

        if(roleIds != null) {
            user.setRoles(roleIds.stream()
                    .map(roleService::findById)
                    .collect(Collectors.toSet()));
        }
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id != null && id  > 0) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("couldn't find user with email " + email));
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.getUserByEmail(username).orElseThrow(() -> new UserNotFoundException("couldn't find user with username " + username));
    }
}