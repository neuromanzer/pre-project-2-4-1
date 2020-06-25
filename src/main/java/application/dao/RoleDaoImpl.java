package application.dao;

import application.model.Role;
import application.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    /*@Autowired
    private SessionFactory sessionFactory;*/

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getByName(String name) {
        TypedQuery<Role> role = entityManager
                .createQuery("select r from Role r where r.name = :name", Role.class);
        role.setParameter("name", name);
        return role.getResultList().stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Role> getAll() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public void add(Role role) {
        entityManager.persist(role);
    }
}
