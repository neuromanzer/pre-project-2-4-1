package application.service;

import application.model.Role;

import java.util.List;

public interface RoleService {
    Role getByName(String name);

    List<Role> getAll();

    void add(Role role);
}
