package org.example;

import org.example.model.entities.BookEntity;
import org.example.model.entities.LoanEntity;
import org.example.model.entities.UserEntity;
import org.example.services.UserService;

import java.time.LocalDate;
import java.util.Scanner;

public class UI {
    private static final UserService userService = UserService.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void run () {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int option = getIntInput("Enter your choice: ");

            switch (option) {
                case 1:
                    createUser();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    System.out.println(userService.listAllUsers());
                    break;
                case 4:
                    System.out.println(userService.listLoanUsers());
                    break;
                case 5:
                    createLoan();
                    break;
                case 6:
                    payLoan();
                    break;
                case 7:
                    System.out.println(userService.listAllLoans());
                    break;
                case 8:
                    System.out.println(userService.listActiveLoans());
                    break;
                case 9:
                    BookEntity bestBook = userService.bestBook();
                    System.out.println("Best book: " + bestBook);
                    break;
                case 10:
                    System.out.println("Total books: " + userService.countBooks());
                    break;
                case 11:
                    System.out.println(userService.listAllBooks());
                    break;
                case 12:
                    UserEntity mostLoanedUser = userService.mostLoanedUser();
                    System.out.println("Most loaned user: " + mostLoanedUser);
                    break;
                case 13:
                    System.out.println("Average loans per user: " + userService.averageLoans());
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (!exit) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Create User");
        System.out.println("2. Delete User");
        System.out.println("3. List All Users");
        System.out.println("4. List Users with Active Loans");
        System.out.println("5. Create Loan");
        System.out.println("6. Pay Loan");
        System.out.println("7. List All Loans");
        System.out.println("8. List Active Loans");
        System.out.println("9. Get Most Popular Book");
        System.out.println("10. Count Books");
        System.out.println("11. List All Books");
        System.out.println("12. Get User with Most Loans");
        System.out.println("13. Get Average Loans per User");
        System.out.println("0. Exit");
        System.out.println("===================================");
    }

    private static void createUser() {
        System.out.println("\n----- Create User -----");
        String name = getStringInput("Enter user name: ");
        String email = getStringInput("Enter user email: ");

        UserEntity user = UserEntity.builder()
                .name(name)
                .email(email)
                .build();

        userService.saveUser(user);
        System.out.println("User created successfully!");
    }

    private static void deleteUser() {
        System.out.println("\n----- Delete User -----");
        int id = getIntInput("Enter user ID to delete: ");
        userService.deleteUserByID(id);
    }

    private static void createLoan() {
        System.out.println("\n----- Create Loan -----");
        int userId = getIntInput("Enter user ID: ");
        int bookId = getIntInput("Enter book ID: ");

        LoanEntity loan = LoanEntity.builder()
                .userId(userId)
                .bookId(bookId)
                .dateLoan(LocalDate.now())
                .datePaid(null)
                .build();

        userService.saveLoan(loan);
        System.out.println("Loan created successfully!");
    }

    private static void payLoan() {
        System.out.println("\n----- Pay Loan -----");
        int loanId = getIntInput("Enter loan ID to pay: ");
        userService.payLoan(loanId);
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}