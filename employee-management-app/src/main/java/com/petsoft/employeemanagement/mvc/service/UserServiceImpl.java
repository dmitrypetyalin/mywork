package com.petsoft.employeemanagement.mvc.service;

import com.petsoft.employeemanagement.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.petsoft.employeemanagement.util.HibernateUtil.callInTransaction;

/**
 * 14.11.2019 9:44
 *
 * @author PetSoft
 */

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Long id) {
        return callInTransaction(session -> session
                .createQuery("from User u where u.id = :id", User.class)
                .setParameter("id", id))
                .uniqueResult();
    }

    @Override
    public List<User> findByName(String name) {
        return callInTransaction(session -> session
                .createQuery("from User u where u.name = :name", User.class)
                .setParameter("name", name)
                .list());
    }

    @Override
    public List<User> findAll() {
        return callInTransaction(session -> session
                .createQuery("from User order by id desc", User.class)
                .list());
    }

    @Override
    public void save(User user) {
        callInTransaction(session -> session.save(user));
    }

    @Override
    public void update(User user) {
    }

    @Override
    public void deleteById(Long id) {
        callInTransaction(session -> {
            session.delete(session.load(User.class, id));
            return null;
        });
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public boolean exist(User user) {
        return findByName(user.getName()) != null;
    }
}
