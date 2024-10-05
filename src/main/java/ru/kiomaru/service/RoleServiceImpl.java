package ru.kiomaru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kiomaru.entity.Role;
import ru.kiomaru.exception.RoleNotFoundException;
import ru.kiomaru.repository.RoleRepository;


import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) throws RoleNotFoundException {
        return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException("Role "+name+" not found"));
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) throws RoleNotFoundException {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role with id "+ id +" not found"));
    }
    @Override
    @Transactional
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
