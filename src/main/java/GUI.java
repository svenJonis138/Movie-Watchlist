
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class GUI extends JFrame{
    private JPanel mainPanel;

    // search field
    private JTextField searchTextField;

    // displays results
    private JList<Movie> resultList;

    // displays watch List
    private JButton displayWatchListButton;

    // displays seen movies
    private JButton displaySeenMoviesButton;

    // add a movie to Watch List
    private JButton addToWatchListButton;

    // Changes watched status
    private JButton markAsSeenButton;

    // Search buttons
    private JButton searchTheMovieDBButton;
    private JButton searchWatchListButton;

    // Quit Button
    private JButton quitButton;

    // Shows status to user
    private JLabel statusDescription;

    // Strings for messages
    static final String ALL_SEEN_MOVIES = "Showing all seen movies";
    static final String ALL_UNSEEN_MOVIES = "Showing all unseen movies";
    static final String MOVIES_MATCHING_SEARCH_DESCRIPTION = "Showing movies matching search description";
    static final String NO_MOVIES_FOUND = "No matching movies";

    // default model
    protected DefaultListModel<Movie> movieListModel;

    //Controller class to control Database methods
    private MovieController controller;
    private TMDB tmdb;
    GUI(MovieController controller){

        this.controller = controller;
        this.tmdb = tmdb;
        setTitle("Movie Watch List");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(1500, 500));
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Configures JList
        movieListModel = new DefaultListModel<>();
        resultList.setModel(movieListModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // when GUI opens, all unseen movies displayed in list
        displayUnseen();
        // adds action listeners
        addListeners();

    }
    private void displayUnseen(){
        // clears list
        movieListModel.removeAllElements();
        List<Movie> unseenMovies = controller.loadUnseenMoviesFromDataBase();
        // adds each unseen movie to list
        if (!unseenMovies.isEmpty()) {


            for (Movie movie : unseenMovies) {
                movieListModel.addElement(movie);
            }
            searchTextField.setText("");
        } else {
            for (Movie movie : unseenMovies) {
                movieListModel.addElement(movie);
            }
        }
    }
    private void addListeners(){
        resultList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        addToWatchListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // gets value of selected movie
                Movie selectedMovie = resultList.getSelectedValue();
                if (!resultList.isSelectionEmpty() && selectedMovie.getStatus() != Movie.WatchedStatus.SEEN) {
                    // if value is not empty movie added to watchlist
                    controller.addMovie(selectedMovie);
                    // displays watchlist
                    displayUnseen();
                } else {
                    // no movie is selected this message will show
                    String message = "Please select a movie if you would like to add it to the Watch List";
                    showMessageDialog(message);
                }

            }
        });
        markAsSeenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // makes sure something is selected
                if (! resultList.isSelectionEmpty()) {
                    // gets value of selected movie
                    Movie selectedMovie = resultList.getSelectedValue();
                    // checks to make sure movie isnt already marked as seen
                    if (selectedMovie.getStatus().equals(Movie.WatchedStatus.SEEN)) {
                        String message = "This movie is already marked as 'Seen'";
                        showMessageDialog(message);
                    } else {
                        // prompts user to rate movie
                        String question = "What would you rate this film, in terms of quality 1-5? " +
                                "1 = worst, 5 = best";
                        String getRating = showInputDialog(question);
                        // parses response to int
                        int rating = parseTheInt(getRating);
                        // ensures a valid rating
                        while (rating <= 0 || rating > 5) {
                            showMessageDialog("Please enter a rating between 1-5 ");
                            getRating = showInputDialog(question);
                            rating = parseTheInt(getRating);
                        }
                        // now that we have a proper rating, updates DB with new values
                        if (rating > 0 && rating <= 5) {
                            selectedMovie.setRating(rating);
                            selectedMovie.setStatus(Movie.WatchedStatus.SEEN);
                            // lets user know we changed status successfully
                            String confirmation = "Movie has been marked as 'Seen";
                            Movie updatedMovie = new Movie(selectedMovie.getMovieID(), selectedMovie.getName(),
                                    Movie.WatchedStatus.SEEN, rating );
                            controller.updateMovie(updatedMovie);
                            showMessageDialog(confirmation);
                            // displays unseen movies
                            displayUnseen();
                        }
                    }
                } else {
                    // if nothing was selected when button pressed, this will run
                    statusDescription.setText(NO_MOVIES_FOUND);
                    String selectSomething = "Please select the movie you watched";
                    showMessageDialog(selectSomething);
                }
            }
        });
        searchTheMovieDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // checks if there is anything to search for
                if (searchTextField.getText().isEmpty()) {
                    String message = "You must enter a search term to search the Movie Database";
                    showMessageDialog(message);

                } else if (! searchTextField.getText().isEmpty() || searchTextField.getText() != null){
                    String searchTerms = searchTextField.getText();
                    try {
                        List<Movie> searchReturns = TMDB.searchTheMovieDB(searchTerms);
                        statusDescription.setText(MOVIES_MATCHING_SEARCH_DESCRIPTION);
                        // clear search text box
                        searchTextField.setText("");
                        //clears list
                        movieListModel.removeAllElements();
                        for(Movie movie : searchReturns) {
                            movieListModel.addElement(movie);
                        }
                        // catches API related exceptions
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    } catch (UnirestException unirestException) {
                        unirestException.printStackTrace();
                    } catch (JSONException Je) {
                        Je.printStackTrace();
                    }
                } else {
                    statusDescription.setText(NO_MOVIES_FOUND);
                    // message if there is nothing to search for
                    showMessageDialog("Please enter a movie title to search for");
                }
            }




        });
        searchWatchListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchWatchlist = searchTextField.getText();
                if (searchWatchlist == null || searchWatchlist.isEmpty()){
                    // clears list
                    movieListModel.removeAllElements();
                    // shows status of no results
                    statusDescription.setText(NO_MOVIES_FOUND);
                } else {
                    movieListModel.removeAllElements();
                    // if search field is not blank, clear list and make a list to hold results
                    List<Movie> searchResults = controller.searchByDescription(searchWatchlist);
                    // checks if there were matching movies
                    if (searchResults.size() == 0){
                        statusDescription.setText(NO_MOVIES_FOUND);
                    } else {
                        // if there were matches run through list and display results
                        statusDescription.setText(MOVIES_MATCHING_SEARCH_DESCRIPTION);
                        for (Movie movie : searchResults) {
                            movieListModel.addElement(movie);
                            // clears out search text field
                            searchTextField.setText("");
                        }
                    }
                }
            }
        });
        displayWatchListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // shows unseen movie and displays status message
                statusDescription.setText(ALL_UNSEEN_MOVIES);
                displayUnseen();
            }
        });
        displaySeenMoviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new list to hold movies with status set to "seen"
                List<Movie> watchedMovies = controller.loadWatchedMovies();
                // checks for results
                if (watchedMovies.size() == 0) {
                    // if no movies, clears the list and breaks the bad news
                    movieListModel.removeAllElements();
                    statusDescription.setText(NO_MOVIES_FOUND);
                } else {
                    statusDescription.setText(ALL_SEEN_MOVIES);
                    // clears list
                    movieListModel.removeAllElements();
                    for (Movie movie : watchedMovies) {
                        movieListModel.addElement(movie);
                    }
                }
            }
        });
        // quits program
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.quitProgram();
            }
        });
    }
    // method to parse integer from string
    public int parseTheInt(String string){
        // new int to hold the value of parsed int
        int newInt = 0;
        // try/catch to catch numFormatException
        try {
            newInt = Integer.parseInt(string);
            // if no such exception, returns the value of the int in the string
            return newInt;
        } catch (NumberFormatException nfe) {
            // if the exception gets caught, returns an in with the value of 0
            return newInt;
        }
    }
    protected void quitProgram() {
        controller.quitProgram();    // Ask the controller to quit the program.
    }
    protected void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    protected String showInputDialog(String question) {
        return JOptionPane.showInputDialog(this, question);
    }


}

