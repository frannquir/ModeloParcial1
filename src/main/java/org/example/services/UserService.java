package org.example.services;

import org.example.model.entities.BookEntity;
import org.example.model.entities.LoanEntity;
import org.example.model.entities.UserEntity;
import org.example.repositories.impl.BookRepository;
import org.example.repositories.impl.LoanRepository;
import org.example.repositories.impl.UserRepository;

import java.awt.print.Book;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserService {
    private static UserService instance;
    public final UserRepository userRepository;
    public final LoanRepository loanRepository;
    public final BookRepository bookRepository;

    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    public UserService() {
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

    public void deleteUserByID(Integer id) {
        try {
            if (userRepository.findByID(id).isEmpty())
                throw new NoSuchElementException("Error: User not found");
            userRepository.deleteByID(id);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public String listAllUsers() {
        try {
            if (userRepository.findAll().isEmpty()) {
                throw new NoSuchElementException("Error: No users found");
            }
            return userRepository.findAll().toString();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String listLoanUsers() {
        try {

            List<Integer> userIdWithActiveLoans = loanRepository.findAll()
                    .stream()
                    .filter(loan -> loan.getDatePaid() == null) // Null means they haven't paid, loan is active.
                    .map(LoanEntity::getUserId)
                    .distinct() // Filter repeated users
                    .toList();

            List<UserEntity> activeLoanUsers = userRepository.findAll()
                    .stream()
                    .filter(user -> userIdWithActiveLoans.contains(user.getId()))
                    .toList();

            if (activeLoanUsers.isEmpty()) {
                throw new NoSuchElementException("No users with active loans found.");
            }
            return activeLoanUsers.toString();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public void saveLoan(LoanEntity loan) {
        try {
            loanRepository.save(loan);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void payLoan(Integer loanId) {
        try {
            Optional<LoanEntity> loanOpt = loanRepository.findByID(loanId);
            if (loanOpt.isPresent()) {
                LoanEntity loan = loanOpt.get();
                if (loan.getDatePaid() == null) {
                    loan.setDatePaid(LocalDate.now());
                    loanRepository.update(loan);
                } else {
                    throw new IllegalStateException("User has already paid this loan.");
                }
            } else {
                throw new NoSuchElementException("User not found");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public String listAllLoans() {
        try {
            List<LoanEntity> loans = loanRepository.findAll();
            if (loans.isEmpty())
                throw new NoSuchElementException("No loans found.");
            return loans.toString();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public String listActiveLoans() {
        try {
            List<LoanEntity> activeLoans = loanRepository.findAll()
                    .stream()
                    .filter(loan -> loan.getDatePaid() == null)
                    .toList();
            if (activeLoans.isEmpty()) {
                throw new NoSuchElementException("No active loans found");
            }
            return activeLoans.toString();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public BookEntity bestBook() {
        try {
            Integer bestBookId = loanRepository.findAll()
                    .stream()
                    .map(LoanEntity::getBookId)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElseThrow(() -> new NoSuchElementException("No loans found"));

            return bookRepository.findByID(bestBookId)
                    .orElseThrow(() -> new NoSuchElementException("Book not found"));
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return new BookEntity();
    }

    public Integer countBooks() {
        try {
            if (bookRepository.findAll().isEmpty()) {
                throw new NoSuchElementException("No books found.");
            }
            return bookRepository.count();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public String listAllBooks() {
        try {
            if (bookRepository.findAll().isEmpty()) {
                throw new NoSuchElementException("No books found.");
            }
            return bookRepository.findAll().toString();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public UserEntity mostLoanedUser() {
        try {
            //
            Map<Integer, Long> userLoans = loanRepository.findAll()
                    .stream()
                    .map(LoanEntity::getUserId)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Integer mostLoanedUserID = userLoans
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElseThrow(() -> new NoSuchElementException("No users found"));

            return userRepository.findByID(mostLoanedUserID)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return new UserEntity();
    }

    public Float averageLoans() {
        try {
            if (loanRepository.findAll().isEmpty()) {
                throw new NoSuchElementException("No loans found");
            }
            long usersWithLoanCount = loanRepository.findAll()
                    .stream()
                    .map(LoanEntity::getUserId)
                    .distinct()
                    .count();
            return (float) loanRepository.findAll().size() / usersWithLoanCount;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return 0.0f;
    }
}
