package com.petsoft.employeemanagement.mvc.service;

import com.petsoft.employeemanagement.domain.User;

import java.util.List;

/**
 * 13.11.2019 14:48
 *
 * @author PetSoft
 */

public interface UserService {
    User findById(Long id);

    List findByName(String name);

    List<User> findAll();

    void save(User user);

    void update(User user);

    void deleteById(Long id);

    void deleteAll();

    boolean exist(User user);
}
