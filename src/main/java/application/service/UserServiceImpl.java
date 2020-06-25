package application.service;

import application.dao.RoleDao;
import application.dao.UserDao;
import application.model.Role;
import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public boolean validateUser(String name) {
        return userDao.validateUser(name);
    }

    @Override
    public void addUser(User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userDao.addUser(user.setRoles(updateRoles(user.getRoles())));
    }

    private List<Role> updateRoles(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(roleDao::getByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void editUser(User user) {
        if (!user.getPassword().startsWith("$2a$10$")) {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
        }
        userDao.editUser(user.setRoles(updateRoles(user.getRoles())));
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
