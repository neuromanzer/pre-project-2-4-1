package application.service;

import application.dao.RoleDao;
import application.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role getByName(String name) {
        return roleDao.getByName(name);
    }

    @Override
    public List<Role> getAll() {
        return roleDao.getAll();
    }

    //@Transactional
    @Override
    public void add(Role role) {
        roleDao.add(role);
    }
}
