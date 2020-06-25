package application.dao;

import application.model.Role;

import java.util.List;

public interface RoleDao {
    Role getByName(String name);

    List<Role> getAll();

    void add(Role role);
}
