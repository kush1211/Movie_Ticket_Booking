package service;

import model.Showtime;
import model.Ticket;
import model.User;
import model.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingService {
    // This method books a ticket for a user for a specific showtime
    public Ticket bookTicket(Showtime showtime, User user, ArrayList<Integer> seatNumber,int totalPrice) {
        if (showtime.getAvailableSeats() > 0) {
            Payment payment = new Payment(totalPrice, "Credit Card");
            Ticket ticket = new Ticket(showtime.getMovie(), user, LocalDateTime.now(), seatNumber, payment,showtime);
            // showtime.bookSeat();
            return ticket;
        } else {
            return null;
        }
    }
}


