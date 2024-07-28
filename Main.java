import model.*;

import java.io.IOException;
import java.util.List;

import UI.CommandLineUI;

public class Main {
    public static void main(String[] args) throws IOException {

        // Load movies and showtimes from CSV files
        List<Movie> movies = model.DataLoader.loadMovies();
        List<Showtime> showtimes = model.DataLoader.loadShowtimes(movies);

        // Start the command line UI
        CommandLineUI commandLineUI = new CommandLineUI();
        commandLineUI.start(movies, showtimes);
    }
}