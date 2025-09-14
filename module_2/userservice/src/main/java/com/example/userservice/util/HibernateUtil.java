package com.example.userservice.util;

import com.example.userservice.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            System.out.println("Hibernate config loaded from: " +
                    HibernateUtil.class.getClassLoader().getResource("hibernate.cfg.xml"));
            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();
        } catch (HibernateException ex) {
            // Без SessionFactory приложение не имеет смысла
            log.error("SessionFactory initialization failed", ex);
            throw ex;
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
