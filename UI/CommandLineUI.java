package UI;

import model.Movie;
import model.Showtime;
import model.Ticket;
import model.User;
import service.BookingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// import java.sql.Date;
import java.time.format.DateTimeFormatter;

public class CommandLineUI {
    // Instance variables
    private BookingService bookingService;
    private User LoggedInUser;
    Scanner scanner = new Scanner(System.in);

    // Constructor
    public CommandLineUI() {
        this.bookingService = new BookingService();
    }

    // Methods

    // Validate email
    private boolean Validate_Email(String email) {
        if (InputValidator.isValidEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    // Validate password
    private boolean Validate_Password(String password) {
        if (InputValidator.isValidPassword(password)) {
            return true;
        } else {
            return false;
        }
    }

    // Check if username or email is unique, here pos is 0 for username and 1 for
    // email
    private boolean isUnique(String credential, int pos) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/Users.csv"))) {
            String line;
            boolean isUnique = true;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[pos].equals(credential)) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading user information.");
            return false;
        }
    }

    // Login method
    private boolean login() {
        System.out.println();
        System.out.println("Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/Users.csv"))) {
            String line;
            boolean userFound = false;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username) && userData[2].equals(password)) {
                    userFound = true;
                    LoggedInUser = new User(userData[0], userData[1], userData[2]);
                    break;
                }
            }

            if (userFound) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Incorrect username or password. Please try again.");
                return false;
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading user information.");
            return false;
        }
    }

    // Register method
    private boolean register() {
        boolean valid_user = false;
        boolean valid_email = false;
        boolean valid_password = false;
        System.out.println("Register");

        String username, email, password, confirmPassword;

        // Get user information

        // getting username and checking if it is unique and not empty
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (!username.isEmpty() && isUnique(username, 0)) {
                valid_user = true;
            } else {
                System.out.println("username cannot be empty and should be unique. please try again.");
            }

        } while (!valid_user);

        // getting email and checking if it is valid and not empty
        do {

            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (Validate_Email(email) && !email.isEmpty()) {
                valid_email = true;
            } else {
                System.out.println("Invalid email. Please try again.");
            }
        } while (!valid_email);

        // getting password and checking if it is valid and not empty
        // getting confirm password and checking if it matches the password
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (Validate_Password(password) && !password.isEmpty()) {
                System.out.print("Confirm password: ");
                confirmPassword = scanner.nextLine();

                if (!password.equals(confirmPassword)) {
                    System.out.println("Passwords do not match. Please try again.");
                } else {
                    valid_password = true;
                }

            } else {
                System.out.println(
                        "Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character, and be at least 8 characters long");
            }
        } while (!valid_password);

        User user = new User(username, email, password);

        // Save user information to a csv file
        try (FileWriter writer = new FileWriter("data/Users.csv", true)) {
            writer.append(user.getUsername()).append(",").append(user.getEmail()).append(",").append(password)
                    .append("\n");
            System.out.println("Registration successful. User information saved to Users.csv.");
            LoggedInUser = user;
            return true;

        } catch (IOException e) {
            System.out.println("An error occurred while saving user information.");
            return false;
        }
    }

    // Write bill details to logs.csv
    private void writeBillToLogFile(Ticket bookedTicket) {
        String fileName = "data/logs.csv";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.append(bookedTicket.getUser().getUsername())
                    .append(",").append(bookedTicket.getMovie().getTitle())
                    .append(",").append(bookedTicket.getStartTime().toString())
                    .append(",").append(String.valueOf(bookedTicket.getSeatNumber()))
                    .append(",").append(bookedTicket.getPayment().getPaymentMethod())
                    .append(",").append(String.valueOf(bookedTicket.getPayment().getAmount()))
                    .append(",").append(String.valueOf(bookedTicket.getScreen()))
                    .append("\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing bill details to logs.csv.");
        }
    }

    // Start the command line UI
    public void start(List<Movie> movies, List<Showtime> showtimes) {

        System.out.println("Welcome to the Movie System!");

        boolean isLoggedIn = false;
        boolean isRegistered = false;
        String userChoice;
        ArrayList<Integer> seatNumbers = new ArrayList<Integer>();

        // Main menu

        // login or register
        do {
            System.out.println();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("Enter your choice:");
            userChoice = scanner.nextLine();

            switch (userChoice) {
                case "1":
                    System.out.println("Logging in...");
                    isLoggedIn = login();
                    break;

                case "2":
                    isRegistered = register();
                    if (isRegistered) {
                        isLoggedIn = true;
                    }
                    break;

                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            // Movie selection if user is logged in
            if (isLoggedIn) {

                // Movie selection loop
                do {
                    seatNumbers.clear();
                    System.out.println();
                    System.out.println("Choose a movie:");

                    // Display available movies
                    for (int i = 0; i < movies.size(); i++) {
                        System.out.println((i + 1) + ". " + movies.get(i).getTitle());
                    }
                    System.out.println((movies.size() + 1) + ". Exit");

                    System.out.print("Enter your choice: ");
                    userChoice = scanner.nextLine();

                    if (isInteger(userChoice)) {
                        int choice = Integer.parseInt(userChoice);

                        // Show showtimes for selected movie
                        if (choice >= 1 && choice <= movies.size()) {

                            Movie selectedMovie = movies.get(choice - 1);

                            System.out.println();
                            System.out.println("Available Showtimes for " + selectedMovie.getTitle() + " :");
                            System.out.println();

                            int showtimeChoiceNumber = 1;

                            //making a time formatter for the time in the showtime like 12:00 PM
                            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("hh:mm a");

                            //making a date formatter for the date in the showtime like 21st july 2021
                            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                            //printing showtime and price
                            System.out.printf("%18s  %25s%n", "Show Time", "Price");

                            for (Showtime showtime : showtimes) {
                                if (showtime.getMovie().equals(selectedMovie)) {
                                    String formattedTime = showtime.getStartTime().format(formatterTime);
                                    String formattedDate = showtime.getStartTime().format(formatterDate);
                                    System.out.printf(showtimeChoiceNumber + ". " + "%s  %14s%n", formattedTime + " on "+formattedDate,
                                            showtime.getShowTimePrice());
                                    showtimeChoiceNumber++;
                                }
                            }

                            if (showtimeChoiceNumber == 1) {

                                System.out.println("Currently, we don't have any Shows of " + selectedMovie.getTitle()
                                        + ". Shows will be out soon.");

                            } else {

                                System.out.println((showtimeChoiceNumber) + ". Exit");
                                System.out.print("Enter your choice of showtime: ");
                                String showtimeChoice = scanner.nextLine();

                                if (isInteger(showtimeChoice)) {

                                    int selectedShowtimeIndex = Integer.parseInt(showtimeChoice);

                                    if (selectedShowtimeIndex == showtimeChoiceNumber) {

                                        System.out.println("Exiting...");

                                    } else if (selectedShowtimeIndex >= 1 && selectedShowtimeIndex < showtimeChoiceNumber) {

                                        Showtime selectedShowtime = null;
                                        int currentShowtimeIndex = 1;

                                        // Find the selected showtime
                                        for (Showtime showtime : showtimes) {
                                            if (showtime.getMovie().equals(selectedMovie)) {
                                                if (currentShowtimeIndex == selectedShowtimeIndex) {
                                                    selectedShowtime = showtime;
                                                    break;
                                                }
                                                currentShowtimeIndex++;
                                            }
                                        }

                                        // Display available seats
                                        try (BufferedReader logReader = new BufferedReader(new FileReader("data/logs.csv"))) {

                                            String logLine;

                                            while ((logLine = logReader.readLine()) != null) {

                                                String[] logParts = logLine.split(",");

                                                if (logParts[1].equals(selectedMovie.getTitle()) && logParts[2].equals(selectedShowtime.getStartTime().toString())) {

                                                    String seatNumbersString = logLine.substring(logLine.indexOf('[') + 1,logLine.indexOf(']'));
                                                    String[] bookedSeatNumbers = seatNumbersString.split(", ");
                                                    // for (int i = 0; i < bookedSeatNumbers.length; i++) {
                                                    // System.out.println(bookedSeatNumbers[i] + " ");
                                                    // }
                                                    for (String seatNumber : bookedSeatNumbers) {

                                                        if (!seatNumber.isEmpty()) {

                                                            selectedShowtime.getBookedSeats().add(Integer.parseInt(seatNumber));
                                                            selectedShowtime.setAvailableSeats(selectedShowtime.getAvailableSeats() - 1);

                                                        }

                                                    }
                                                }
                                            }
                                            logReader.close();

                                        } catch (IOException e) {

                                            System.out.println("An error occurred while reading user information.");

                                        }

                                        System.out.println();
                                        System.out.println("Available Seats: " + selectedShowtime.getAvailableSeats());

                                        // Display seat layout with booked seats marked with '*'
                                        for (int i = 1; i <= selectedShowtime.getTotalSeats(); i++) {
                                            if (selectedShowtime.getBookedSeats().contains(i)) {
                                                System.out.printf("%-4s", "*");
                                            } else {
                                                System.out.printf("%-4d", i);
                                            }
                                            if (i % 10 == 0) {
                                                System.out.println();
                                            }
                                        }

                                        System.out.println();

                                        //get the number of seats to book
                                        int numSeats;

                                        do {
                                            System.out.println("Enter the number of seats you want to book:");
                                            while (!scanner.hasNextInt()) {
                                                System.out.println("Invalid input. Please enter an integer:");
                                                scanner.next(); 
                                                System.out.println();

                                            }
                                            numSeats = scanner.nextInt();
                                            scanner.nextLine(); 
                                            if (numSeats >= selectedShowtime.getTotalSeats()) {
                                                System.out.println(
                                                        "Invalid input. Number of seats exceeds available seats.");
                                                System.out.println();

                                            }
                                        } while (numSeats >= selectedShowtime.getTotalSeats());

                                        //get the valid seat numbers
                                        System.out.println("Enter seat nos");
                                        boolean continueBooking = true;

                                        for (int i = 0; i < numSeats && continueBooking; i++) {

                                            System.out.println("Seat " + (i + 1) + ": ");
                                            String seat = scanner.nextLine();
                                            int seatNumber = 0;

                                            if (seat.equalsIgnoreCase("exit")) {

                                                continueBooking = false;

                                            } else {

                                                if (!isInteger(seat)) {

                                                    System.out.println("Invalid seat choice: " + seat + ". Please enter a number.");
                                                    i--;

                                                } else {

                                                    seatNumber = Integer.parseInt(seat);
                                                    if (seatNumber < 1 || seatNumber > selectedShowtime.getTotalSeats() || selectedShowtime.getBookedSeats().contains(seatNumber)) {

                                                        System.out.println("Invalid seat choice: " + seatNumber + ". Please try again.");
                                                        i--;

                                                    } else {

                                                        seatNumbers.add(seatNumber);
                                                        selectedShowtime.setAvailableSeats(selectedShowtime.getAvailableSeats() - 1);
                                                        selectedShowtime.getBookedSeats().add(seatNumber);

                                                    }
                                                }

                                            }

                                        }

                                        //exit if no seats are booked
                                        if (seatNumbers.size() == 0 && continueBooking==false) {

                                            System.out.println("No seats booked.");
                                        }
                                        else{Ticket BookedTickets = null;
                                            Ticket ticket = bookingService.bookTicket(selectedShowtime,LoggedInUser, seatNumbers,selectedShowtime.getShowTimePrice() * seatNumbers.size());
                                            
                                        // Check if all tickets are booked
                                        if (ticket != null) {

                                            BookedTickets = ticket;

                                        } else {

                                            System.out.println("Sorry, no available seats for this showtime.");

                                        }

                                        System.out.println("Total amount: " + selectedShowtime.getShowTimePrice() * seatNumbers.size());
                                        String paymentMethod = "";


                                        // Payment method selection
                                        do {
                                            System.out.println();
                                            System.out.println("select payment method: ");
                                            System.out.println("1. Credit Card");
                                            System.out.println("2. Debit Card");
                                            System.out.println("3. Cash");
                                            System.out.println("Enter your choice: ");
                                            String paymentChoice = scanner.nextLine();
                                            // scanner.nextLine();

                                            switch (paymentChoice) {
                                                case "1":
                                                    paymentMethod = "Credit Card";
                                                    break;
                                                case "2":
                                                    paymentMethod = "Debit Card";
                                                    break;
                                                case "3":
                                                    paymentMethod = "Cash";
                                                    break;
                                                default:
                                                    System.out.println("Invalid choice. Payment failed.");
                                                    break;
                                            }
                                        } while (paymentMethod.equals(""));

                                        BookedTickets.getPayment().setPaymentMethod(paymentMethod);

                                        boolean allPaymentsSuccessful = true;
                                        boolean paymentStatus = BookedTickets.getPayment().processPayment();
                                        if (!paymentStatus) {
                                            allPaymentsSuccessful = false;
                                            break;
                                        }

                                        if (allPaymentsSuccessful) {

                                            System.out.println();
                                            System.out.println("All tickets booked successfully and payment successful!");

                                        } else {

                                            System.out.println("Sorry, payment failed for one or more tickets. Please try again.");

                                        }

                                        if (BookedTickets.getPayment().getAmount() > 0) {
                                            System.out.println();
                                            System.out.println("Bill: ");
                                            System.out.printf("%-20s: %s%n", "User", BookedTickets.getUser().getUsername());
                                            System.out.printf("%-20s: %s%n", "Movie", BookedTickets.getMovie().getTitle());
                                            System.out.printf("%-20s: %s%n", "Showtime", BookedTickets.getStartTime().format(formatterTime)+" on "+BookedTickets.getStartTime().format(formatterDate));
                                            System.out.printf("%-20s: %s%n", "Screen", BookedTickets.getScreen());
                                            System.out.printf("%-20s: %s%n", "Seat Number", BookedTickets.getSeatNumber());
                                            System.out.printf("%-20s: %s%n", "Payment Method", BookedTickets.getPayment().getPaymentMethod());
                                            System.out.printf("%-20s: %s%n", "Amount", BookedTickets.getPayment().getAmount());
                                            System.out.println();
                                        }

                                        writeBillToLogFile(BookedTickets);
                                        }
                                        

                                    } else {

                                        System.out.println("Invalid showtime choice. Please try again.");
                                    }

                                } else {

                                    System.out.println("Invalid showtime choice. Please try again.");
                                }

                            }

                        } else if (choice == movies.size() + 1) {

                            System.out.println("Exiting from the System");
                            System.exit(0);
                            

                        } else {

                            System.out.println("Invalid choice. Please try again.");

                        }

                    } else {

                        System.out.println("Invalid choice. Please try again.");

                    }

                } while (isLoggedIn);

            }

        } while (!isLoggedIn);

        scanner.close();
    }

    // Check if a string is an integer
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}