package application.dao;

import application.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }


    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByName(String name) {
        TypedQuery<User> user = entityManager
                .createQuery("select u from User u where u.name = :name", User.class);
        user.setParameter("name", name);
        return user.getResultList().stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean validateUser(String name) {
        User user = getUserByName(name);
        return user != null;
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        //entityManager.find(User.class, id);  ????????
        entityManager.remove(getUserById(id));
    }
}
