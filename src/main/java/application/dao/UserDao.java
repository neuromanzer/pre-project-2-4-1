package application.dao;

import application.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByName(String name);

    boolean validateUser(String name);

    void addUser(User user);

    void editUser(User user);

    void deleteUser(Long id);
}
