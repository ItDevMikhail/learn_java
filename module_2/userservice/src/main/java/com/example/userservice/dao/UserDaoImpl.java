package com.example.userservice.dao;

import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User create(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.info("Created user id={}", user.getId());
            return user;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Create failed", e);
            throw e;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        } catch (HibernateException e) {
            log.error("FindById failed", e);
            throw e;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> q = session.createQuery("FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            return q.uniqueResultOptional();
        } catch (HibernateException e) {
            log.error("FindByEmail failed", e);
            throw e;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            log.error("FindAll failed", e);
            throw e;
        }
    }

    @Override
    public User update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.info("Updated user id={}", user.getId());
            return user;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Update failed", e);
            throw e;
        }
    }

    @Override
    public void delete(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(user) ? user : session.merge(user));
            tx.commit();
            log.info("Deleted user id={}", user.getId());
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("Delete failed", e);
            throw e;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User u = session.get(User.class, id);
            if (u == null) {
                tx.rollback();
                return false;
            }
            session.remove(u);
            tx.commit();
            log.info("Deleted user id={}", id);
            return true;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            log.error("DeleteById failed", e);
            throw e;
        }
    }
}
