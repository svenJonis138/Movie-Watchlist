import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieDB {

    private final String dbURI;

    MovieDB(String databaseURI) {
        this.dbURI = databaseURI;

        try(Connection conn = DriverManager.getConnection(dbURI);
            Statement statement = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                    "movies (movieID INTEGER PRIMARY KEY, " +
                    "name TEXT NOT NULL UNIQUE, " +
                    "status TEXT NOT NULL CHECK(status = 'SEEN' OR status = 'NOT_SEEN')," +
                    "rating INTEGER)";
            statement.execute(createTableSQL);

        } catch (SQLException sqle) {
            System.err.println("Error creating table because " + sqle);
        }
    }
    public List<Movie> getAllUnseenMovies() {


        List<Movie> allRecords = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbURI);
             Statement statement = conn.createStatement()) {

            String selectAllSQL = "SELECT * FROM movies WHERE status = 'NOT_SEEN' ORDER BY name ASC";
            ResultSet rsAll = statement.executeQuery(selectAllSQL);
            if (rsAll != null) {
                while (rsAll.next()) {
                    int iD = rsAll.getInt("movieID");
                    String name = rsAll.getString("name");
                    String status = rsAll.getString("status");
                    int rating = rsAll.getInt("rating");

                    Movie unseenMovie = new Movie(iD, name, Movie.WatchedStatus.NOT_SEEN, rating);
                    allRecords.add(unseenMovie);
                } return allRecords;
            } return Collections.emptyList();

        } catch (SQLException sqle) {
            System.err.println("Error fetching all unseen Movies because: " + sqle);
            return null;
        }
    }
    public void add(Movie newMovie) throws SQLException {


        String addPlaceSQL = "INSERT INTO movies (name, status, rating)" +
                " VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement addTicketPS = conn.prepareStatement(addPlaceSQL)) {

            addTicketPS.setString(1, newMovie.getName());
            addTicketPS.setString(2, newMovie.getStatus().name());
            addTicketPS.setInt(3, newMovie.getRating());

            addTicketPS.executeUpdate();
            ResultSet keys = addTicketPS.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);
            newMovie.setMovieID(id);


        } catch (SQLException sqle) {

            System.err.println("Error adding to table because: " + sqle);
            throw sqle; // fixed
        }
    }

    public Movie getMovieById(int id) {

        String searchSQL = " SELECT * FROM movies WHERE movieID = ? ORDER BY name ASC";

        try (Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement searchPS = conn.prepareStatement(searchSQL)) {

            searchPS.setInt(1, id);

            ResultSet searchRS = searchPS.executeQuery();

            if (searchRS.next()) {
                int movieID = searchRS.getInt("movieID");
                String title = searchRS.getString("title");
                String status = searchRS.getString("status");
                int rating = searchRS.getInt("rating");
                Movie foundMovie = new Movie(movieID, title, Movie.WatchedStatus.valueOf(status), rating);

                return foundMovie;
            } else {
                return null;
            }

        } catch (SQLException sqle) {
            System.err.println("Error finding movie " + id + " because: " + sqle);
            return null;
        }
    }


    public void updateMovie(Movie movie) {

        // prepared statement to update
        String searchSQL = "UPDATE movies " +
                "SET name = ?, " +
                "status = ?, " +
                "rating = ? " +
                "WHERE movieID = ?";
        try (Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement updateMoviePS = conn.prepareStatement(searchSQL)) {
            // sets all columns
            updateMoviePS.setString(1, movie.getName());
            updateMoviePS.setString(2, movie.getStatus().name());
            updateMoviePS.setInt(3, movie.getRating());
            updateMoviePS.setInt(4, movie.getMovieID());
            updateMoviePS.execute();

            // catch exception
        } catch (SQLException sqle) {
            System.err.println("Error updating movie " + movie.getMovieID() + " because " + sqle);
        }
    }
    public List<Movie> searchByDescription(String searchTerm) {
        // Searches database for descriptions that match given string
        // Catches any SQLException errors. If any exception is thrown, uses System.err.print to print an error message and return null.

        String searchSQL = " SELECT * FROM movies WHERE UPPER(name) LIKE UPPER(?) ORDER BY name ASC";

        try (Connection conn = DriverManager.getConnection(dbURI);
             PreparedStatement searchPS = conn.prepareStatement(searchSQL)) {
            List<Movie> searchResults = new ArrayList<>();
            // if the given search term is null, returns an empty string
            if (searchTerm == null || searchTerm.isEmpty()) {
                return Collections.emptyList();
            } else {
                // sets search term into prepared statement and searches for given letters within description
                // ignoring case
                searchPS.setString(1, "%" + searchTerm + "%");
                ResultSet searchRS = searchPS.executeQuery();
                // if there are results fills list with result tickets
                while (searchRS.next()) {
                    int movieID = searchRS.getInt("movieID");
                    String name = searchRS.getString("name");
                    String status = searchRS.getString("status");
                    int rating = searchRS.getInt("rating");
                    Movie foundMovie = new Movie(movieID, name, Movie.WatchedStatus.valueOf(status), rating);
                    searchResults.add(foundMovie);
                    // if no results, returns an empty list
                } if (searchResults.isEmpty()) {
                    return Collections.emptyList();
                }
                // if there are results, returns the list of results
            }return searchResults;
            // catches SQLException and returns an empty list
        } catch(SQLException sqle){
            System.err.println("Error finding movie " + searchTerm + " because: " + sqle);
            return Collections.emptyList();
        }
    }

    public List<Movie> getAllSeenMovies() {

        List<Movie> allRecords = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbURI);
             Statement statement = conn.createStatement()) {
            // same as open Movies, but checks for resolved status instead
            String selectAllSQL = "SELECT * FROM movies WHERE status = 'SEEN' ORDER BY '' ASC";
            ResultSet rsAll = statement.executeQuery(selectAllSQL);
            // while there are results, adds to list
            if (rsAll != null) {
                while (rsAll.next()) {
                    int movieID = rsAll.getInt("movieID");
                    String name = rsAll.getString("name");
                    String status = rsAll.getString("status");
                    int rating = rsAll.getInt("rating");
                    Movie seenMovie = new Movie(movieID, name, Movie.WatchedStatus.valueOf(status), rating);
                    allRecords.add(seenMovie);
                } return allRecords;
                // if results are null, returns an empty list
            } return Collections.emptyList();
            // catches exception and returns null
        } catch (SQLException sqle) {
            System.err.println("Error fetching all watched Movies because: " + sqle);
            return Collections.emptyList();
        }
    }


}
