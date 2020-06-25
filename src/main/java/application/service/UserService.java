package application.service;

import application.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User getUserByName(String name);

    boolean validateUser(String name);

    void addUser(User user);

    void editUser(User user);

    void deleteUser(Long id);
}
