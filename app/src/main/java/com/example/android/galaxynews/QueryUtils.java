package com.example.android.galaxynews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.example.android.galaxynews.MainActivity.LOG_TAG;



public final class QueryUtils {

    // Key used for the JSON response
    static final String RESPONSE = "response";
    // Key used for the results with the list of news
    static final String RESULTS = "results";
    private static final int SUCCESS_CODE = 200;

    private static final String TAGS = "tags";
    public static final int ZERO = 0;
    private static final String WEB_TITLE="webTitle";

    private QueryUtils() {
    }


    /**
     * Return a list of {@link Galaxy} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Galaxy> extractFeatureFromJson (String galaxyJSON){
        // If the JSON String is empty or null, then return early
        if (TextUtils.isEmpty(galaxyJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding galaxies to
        ArrayList <Galaxy> galaxies = new ArrayList <>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try

        {
            JSONObject jsonRootObject = new JSONObject(galaxyJSON);

            //Get the instance of JSON Features Array that contains JSONObjects
            JSONObject responseObject = jsonRootObject.getJSONObject(RESPONSE);

            // For a given galaxy, extract the JSONObject associated with the
            // key called "properties", which represents a list of all properties
            // for that galaxy.
            JSONArray resultsArray = responseObject.getJSONArray(RESULTS);


            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single galaxy at position i within the list of galaxies
                JSONObject currentGalaxy = resultsArray.getJSONObject(i);


                String title = currentGalaxy.getString("webTitle");
                String date = currentGalaxy.getString("webPublicationDate");
                String section = currentGalaxy.getString("sectionName");
                String url = currentGalaxy.getString("webUrl");
                String author = currentGalaxy.getJSONArray(TAGS).getJSONObject(ZERO).getString(WEB_TITLE);

                Galaxy galaxy = new Galaxy(title, date, section, url, author);
                galaxies.add(galaxy);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the galaxy JSON results", e);
        }

        // Return the list of galaxies
        return galaxies;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == SUCCESS_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG,"IOException took place,Problem retrieving the galaxy JSON results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the Guardian dataset and return a list of {@link Galaxy} objects.
     */
    public static List<Galaxy> fetchGalaxyData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link galaxy)
        List<Galaxy> galaxies = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link galaxy}ies
        return galaxies;
    }
}
