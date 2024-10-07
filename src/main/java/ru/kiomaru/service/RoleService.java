package ru.kiomaru.service;


import ru.kiomaru.entity.Role;
import ru.kiomaru.exception.RoleNotFoundException;

import java.util.List;

public interface RoleService {

    Role findByName(String name) throws RoleNotFoundException;

    Role save(Role role);

    List<Role> findAll();

    Role findById(Long id);

    void delete(Long id);

    Long findIdByName(String name);
}
