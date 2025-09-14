package com.example.userservice;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.HibernateException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final UserDao userDao = new UserDaoImpl();

    public static void main(String[] args) {
        System.out.println("User Service (Hibernate + PostgreSQL, no Spring)");
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = sc.nextLine().trim();
                try {
                    switch (choice) {
                        case "1": createUser(sc); break;
                        case "2": getUserById(sc); break;
                        case "3": listUsers(); break;
                        case "4": updateUser(sc); break;
                        case "5": deleteUser(sc); break;
                        case "0": running = false; break;
                        default: System.out.println("Unknown command");
                    }
                } catch (HibernateException e) {
                    System.out.println("DB error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
        System.out.println("Exit.");
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Create user");
        System.out.println("2) Find by ID");
        System.out.println("3) Get All");
        System.out.println("4) Update user");
        System.out.println("5) Delete user");
        System.out.println("0) Exit");
        System.out.print("Exit: ");
    }

    private static void createUser(Scanner sc) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Age (empty — null): ");
        String ageStr = sc.nextLine().trim();
        Integer age = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);

        User u = new User(name, email, age);
        u = userDao.create(u);
        System.out.println("Created: " + u);
    }

    private static void getUserById(Scanner sc) {
        System.out.print("ID: ");
        Long id = Long.parseLong(sc.nextLine().trim());
        Optional<User> u = userDao.findById(id);
        System.out.println(u.map(Object::toString).orElse("Not found"));
    }

    private static void listUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Empty");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser(Scanner sc) {
        System.out.print("ID: ");
        Long id = Long.parseLong(sc.nextLine().trim());
        Optional<User> existing = userDao.findById(id);
        if (existing.isEmpty()) {
            System.out.println("User not found");
            return;
        }
        User u = existing.get();
        System.out.print("New Name (empty — skip): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) u.setName(name);

        System.out.print("New email (empty — skip): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) u.setEmail(email);

        System.out.print("New age (empty — skip): ");
        String ageStr = sc.nextLine().trim();
        if (!ageStr.isEmpty()) u.setAge(Integer.parseInt(ageStr));

        u = userDao.update(u);
        System.out.println("Updated: " + u);
    }

    private static void deleteUser(Scanner sc) {
        System.out.print("ID: ");
        Long id = Long.parseLong(sc.nextLine().trim());
        boolean ok = userDao.deleteById(id);
        System.out.println(ok ? "Deleted" : "Not Found");
    }
}
