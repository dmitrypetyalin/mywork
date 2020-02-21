package com.petsoft.employeemanagement.mvc.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.petsoft.employeemanagement.util.HibernateUtil;
import com.petsoft.employeemanagement.domain.Role;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

import static com.petsoft.employeemanagement.util.HibernateUtil.callInTransaction;

/**
 * 29.11.2019 13:47
 *
 * @author PetSoft
 */

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public List<Role> findAll() {
        return HibernateUtil.callInTransaction(session -> session
                .createQuery("from Role", Role.class)
                .list());
    }

    @Override
    public Role findById(Long id) {
        return callInTransaction(session -> session
                .createQuery("from Role u where u.id = :id", Role.class)
                .setParameter("id", id)
                .uniqueResult());
    }
}
