package com.petsoft.employeemanagement.mvc.service;

import com.petsoft.employeemanagement.domain.Role;

import java.util.List;

/**
 * 29.11.2019 13:18
 *
 * @author PetSoft
 */

public interface RoleService {
    List<Role> findAll();

    Role findById(Long id);
}
