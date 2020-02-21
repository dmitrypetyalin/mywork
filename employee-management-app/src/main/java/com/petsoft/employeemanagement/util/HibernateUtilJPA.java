package com.petsoft.employeemanagement.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 * 08.11.2019 0:07
 *
 * @author PetSoft
 */

public class HibernateUtilJPA {
    private static final String PERSISTENT_UNIT_NAME = "item-manager-pu";
    @PersistenceContext
    private static final EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENT_UNIT_NAME);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEm() {
        return emf.createEntityManager();
    }
}
