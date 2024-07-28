package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
// import java.util.Arrays;

public class Ticket {
    // Instance variables
    private Movie movie;
    private User user;
    private LocalDateTime bookingTime;
    private ArrayList<Integer> seatNumber;
    private Showtime showtime;
    private Payment payment;
    private int screen;

    // Constructor
    public Ticket(Movie movie, User user, LocalDateTime bookingTime, ArrayList<Integer> seatNumber, Payment payment,Showtime showtime) {
        this.movie = movie;
        this.user = user;
        this.bookingTime = bookingTime;
        this.seatNumber = seatNumber;
        this.payment = payment;
        this.showtime = showtime;
        this.screen = showtime.getScreen();
    }

    // Getter methods
    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }
    public int getScreen() {
        return screen;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public ArrayList<Integer> getSeatNumber() {
        return seatNumber;
    }

    public Payment getPayment() { // Add getPayment method
        return payment;
    }
    // Setter methods (if needed)
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setSeatNumber(ArrayList<Integer> seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setPayment(Payment payment) { // Add setPayment method
        this.payment = payment;
    }

    public String toString() {
        return "Ticket{" +
                "movie=" + movie.getTitle() +
                ", user=" + user.getUsername() +
                ", bookingTime=" + bookingTime +
                ", seatNumber=" + seatNumber +
                ", payment=" + payment.getAmount() +
                '}';
    }

    public LocalDateTime getStartTime() {
        return showtime.getStartTime();
    }
}
