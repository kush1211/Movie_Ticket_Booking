package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
// import java.util.List;

public class Showtime {
    // Instance variables
    private Movie movie;
    private LocalDateTime startTime;
    private int availableSeats;
    private int totalSeats;
    private int showTimePrice;
    private int Screen;
    private ArrayList<Integer> bookedseats = new ArrayList<Integer>();
    

    // Constructor
    public Showtime(Movie movie, LocalDateTime startTime, int availableSeats,int Screen,int price) {
        this.movie = movie;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
        this.totalSeats = availableSeats;
        this.Screen=Screen;
        this.showTimePrice=price;
    }

    // Getter methods
    public Movie getMovie() {
        return movie;
    }

    public int getScreen() {
        return Screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public int getTotalSeats() {
        return totalSeats;
    }
    public ArrayList<Integer> getBookedSeats() {
        return bookedseats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getShowTimePrice() {
        return showTimePrice;
    }

    // Setter methods (if needed)
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    // Method to book a seat for this showtime
    public boolean bookSeat() {
        if (availableSeats > 0) {
            return true;
        } else {
            return false; // No available seats
        }
    }

    public String toString() {
        return "Showtime{" +
                "movie=" + movie.getTitle() +
                ", startTime=" + startTime +
                ", availableSeats=" + availableSeats +
                '}';
    }
}

