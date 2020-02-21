package com.petsoft.employeemanagement.util;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.Transaction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.Session;

import java.util.function.Function;

/**
 * 11.10.2019 15:41
 *
 * @author PetSoft
 */

public class HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    static {
        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
    }

    private static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        return sessionFactory;
    }

    public static  <R> R callInTransaction(Function<Session, R> fn) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        R result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            result = fn.apply(session);
            transaction.commit();
        } catch (HibernateException e) {
            LOG.error(e.getMessage(), e);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return result;
    }
}
