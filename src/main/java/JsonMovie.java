import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JsonMovie {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("imdbID")
    @Expose
    private String imdbID;
    @SerializedName("Poster")
    @Expose
    private String poster;
    @SerializedName("Title")
    @Expose
    private String title;

    /**
     * No args constructor for use in serialization
     *
     */
    public JsonMovie() {
    }

    /**
     *
     * @param year
     * @param imdbID
     * @param type
     * @param title
     * @param poster
     */
    public JsonMovie(String type, String year, String imdbID, String poster, String title) {
        super();
        this.type = type;
        this.year = year;
        this.imdbID = imdbID;
        this.poster = poster;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
