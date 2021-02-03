import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.codehaus.groovy.syntax.Token;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class TMDB {


    private JSONArray array;
    private String searchValue;
    private static Token tokenResponse;

    public static List<Movie> searchTheMovieDB(String searchTerms) throws UnsupportedEncodingException, UnirestException{



        String toSearch = searchTerms.replace(" ", "+");


        String host = "https://movie-database-imdb-alternative.p.rapidapi.com/";
        String charset = "UTF-8";
        // headers for a request
        String x_rapidapi_host = "movie-database-imdb-alternative.p.rapidapi.com";
        String x_rapidapi_key = "a89c534a74msh1b3326ba71fc01dp16c847jsn4622e63aaaa6";
        // Params
        String s = toSearch;
        // Format query for preventing encoding problems
        String query = String.format("s=%s", URLEncoder.encode(s, charset));

        // Json response
        HttpResponse<JsonNode> response = Unirest.get(host + "?" + query)
                .header("x-rapidapi-host", x_rapidapi_host)
                .header("x-rapidapi-key", x_rapidapi_key)
                .asJson();
        // make it pretty
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());

        // string from Json data
        String prettyJsonString = gson.toJson(je);

        // JSONObject from string
        JSONObject jObject = new JSONObject(prettyJsonString);

        // pulls the array of results out
        JSONArray movieArray = jObject.getJSONArray("Search");

        // makes a map of of movie results from search
        HashMap<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject jsonObject = movieArray.getJSONObject(i);
            map.put(i, jsonObject);

        }
        List<Movie> searchResults = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            String type = (String) map.get(i).get("Type");
            String year = (String) map.get(i).get("Year");
            String imdbID = (String) map.get(i).get("imdbID");
            String poster = (String) map.get(i).get("Poster");
            String title = (String) map.get(i).get("Title");
            JsonMovie results = new JsonMovie(type, year, imdbID, poster, title);
            searchResults.add(new Movie(results.getTitle(), results.getYear()));
        }
        return searchResults;
    }
}
