package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final String MOVIES_FILE_PATH = "data/movies.csv";
    private static final String SHOWTIMES_FILE_PATH = "data/showtimes.csv";

    // Load movies from the movies.csv file
    public static List<Movie> loadMovies() throws IOException {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String genre = parts[1];
                    int duration = Integer.parseInt(parts[2]);
                    Movie movie = new Movie(title, genre, duration);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    // Load showtimes from the showtimes.csv file
    public static List<Showtime> loadShowtimes(List<Movie> movies) throws IOException {
        List<Showtime> showtimes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHOWTIMES_FILE_PATH))) {
            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) { // Ensure there are at least 5 columns
                    String movieTitle = parts[0];
                    LocalDateTime startTime = LocalDateTime.parse(parts[1]);
                    int availableSeats = Integer.parseInt(parts[2]);
                    int screen = Integer.parseInt(parts[3]);
                    int price = Integer.parseInt(parts[4]);
                    Movie movie = findMovieByTitle(movies, movieTitle);
                    if (movie != null) {
                        Showtime showtime = new Showtime(movie, startTime, availableSeats, screen, price);
                        showtimes.add(showtime);
                    }
                }
            }
        }
        return showtimes;
    }

    //method to find a movie by its title
    private static Movie findMovieByTitle(List<Movie> movies, String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    // Test the DataLoader class
    // public static void main(String[] args) {
    //     try {
    //         List<Movie> movies = loadMovies();
    //         List<Showtime> showtimes = loadShowtimes(movies);
    //         for (Showtime showtime : showtimes) {
    //             System.out.println(showtime);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
