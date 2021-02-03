public class Movie {


    enum WatchedStatus{
        SEEN, NOT_SEEN;
    }
    private int movieID;
    private String title;
    private String name;
    private String year;
    private WatchedStatus status;
    private int rating;

    // constructor for a new movie added to the list
    public Movie(String title, String year) {
        this.name = title + " " + year;
        this.title = title;
        this.year = year;
        this.status = WatchedStatus.NOT_SEEN;

    }
    // constructor for movie retrieved from database
    public Movie(int movieID, String name, WatchedStatus status, int rating) {
        this.movieID = movieID;
        this.name = name;
        this.status = status;
        this.rating = rating;
    }
    // getters and setters
    public int getMovieID() { return movieID; }

    public void setMovieID(int movieID) { this.movieID = movieID; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public WatchedStatus getStatus() { return status; }

    public void setStatus(WatchedStatus status) { this.status = status; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    @Override
    public String toString() {
        // formatted message fot toString()
        String out = String.format("Title: %s.", this.name);
        if (status == WatchedStatus.SEEN) {
            out += " Status: " + status.name() + " You rated this film: " + rating;
        }
        return out;
    }
}
