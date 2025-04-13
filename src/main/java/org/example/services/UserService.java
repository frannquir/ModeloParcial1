package org.example.services;

import org.example.model.entities.UserEntity;
import org.example.repositories.impl.BookRepository;
import org.example.repositories.impl.LoanRepository;
import org.example.repositories.impl.UserRepository;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService {
    private static UserService instance;
    public final UserRepository userRepository;
    public final LoanRepository loanRepository;
    public final BookRepository bookRepository;

    public static UserService getInstance () {
        if(instance == null) instance = new UserService();
        return instance;
    }

    public UserService () {
        userRepository = UserRepository.getInstance();
        loanRepository = LoanRepository.getInstance();
        bookRepository = BookRepository.getInstance();
    }

    public void saveUser(UserEntity user) {
        try {
            userRepository.save(user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteUserByID (Integer id) {
        try {
            if(userRepository.findByID(id).isEmpty())
                throw new NoSuchElementException("Error: User not found");
            userRepository.deleteByID(id);
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
