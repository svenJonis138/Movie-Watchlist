import java.sql.SQLException;
import java.util.List;

public class MovieController {
    MovieDB movieDB;

    MovieController(MovieDB movie) {movieDB = movie;}

    // pulls all unseen movies from DB
    protected List<Movie> loadUnseenMoviesFromDataBase() {
        List<Movie> unSeenMovies = movieDB.getAllUnseenMovies();
        return unSeenMovies;
    }
    protected boolean addMovie(Movie newMovie) {
        // Returns true if movie added successfully
        try{
            movieDB.add(newMovie);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    // allows user to update status to seen and rate movie
    protected void updateMovie(Movie movie) { movieDB.updateMovie(movie); }

    // Find and return a list of matching movies. If nothing matches, the list will be empty.
    protected List<Movie> searchByDescription(String searchTerm) {
        List<Movie> matchingMovies = movieDB.searchByDescription(searchTerm);
        return matchingMovies;
    }
    // extra credit grabs all resolved tickets and returns a list to be displayed on a separate form
    protected List<Movie> loadWatchedMovies() {
        List<Movie> resolvedTickets = movieDB.getAllSeenMovies();
        return resolvedTickets;
    }
    // quits program
    protected void quitProgram() {
        Main.quit();
    }

}
