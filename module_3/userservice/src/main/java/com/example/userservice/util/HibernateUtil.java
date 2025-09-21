package com.example.userservice.util;

import com.example.userservice.model.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            String url = System.getProperty("db.url");
            String username = System.getProperty("db.username");
            String password = System.getProperty("db.password");

            if (url != null && username != null && password != null) {
                log.info("Initialization Hibernate in test mode: {}", url);
                configuration.setProperty("hibernate.connection.url", url);
                configuration.setProperty("hibernate.connection.username", username);
                configuration.setProperty("hibernate.connection.password", password);
                configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.show_sql", "true");
            } else {
                log.info("Initialization Hibernate in main mode");
                System.out.println("Hibernate config loaded from: " +
                        HibernateUtil.class.getClassLoader().getResource("hibernate.cfg.xml"));
                configuration.configure("hibernate.cfg.xml");
            }

            configuration.addAnnotatedClass(User.class);

            StandardServiceRegistryBuilder builder =
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());

            return configuration.buildSessionFactory(builder.build());

        } catch (HibernateException ex) {
            log.error("SessionFactory initialization failed", ex);
            throw ex;
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
