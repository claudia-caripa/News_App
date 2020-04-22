package com.example.mynewsapplication;

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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    /**Tag foe the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**Create a private constructor
     *
     */
    private QueryUtils(){
    }

    /**
     * Query theguardian data set and return a list of {@link MyNews} objects
     */
    public static List<MyNews> fetchMyNewsData (String requestUrl){
        //Create URL object
        URL url = createUrl(requestUrl);

        //// Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        System.out.println("JSON answer: " + jsonResponse);

        // Extract relevant fields from the JSON response and create a list of {@link MyNews}
        List<MyNews> myNews = extractFeatureFromJson(jsonResponse);

        return myNews;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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
     * Returns a list of {@link MyNews} objects that has been built up from
     * parsing a JSON response
     */
    private static List<MyNews> extractFeatureFromJson(String myNewsJSON){

        // If the JSON string is empty or null, then return early.
        if (myNewsJSON == null || myNewsJSON.isEmpty()){
            Log.e(LOG_TAG, "myNewsJSON is null");
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<MyNews> myNews = new ArrayList<>();

        try{
            //Create a JSONObject from SAMPLE_JSON_RESPONSE
            JSONObject baseJsonResponse = new JSONObject(myNewsJSON);

            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            //Extract the JSONArray called"features"
            JSONArray newsArray = responseObject.getJSONArray("results");

            //For each news in the Array, create an object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String sectionName = currentNews.getString("sectionName");

                // Extract the value for the key called "webTitle"
                String webTitle = currentNews.getString("webTitle");

                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentNews.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String webUrl = currentNews.getString("webUrl");

                /**
                 * OJO
                 */

                JSONArray tagsArray = currentNews.getJSONArray("tags");
                String authorName = null;
                for (int j = 0; j < tagsArray.length(); j++){
                    JSONObject currentTag = tagsArray.getJSONObject(j);
                    String type = currentTag.getString("type");
                    if (!type.equals("contributor")) {
                        continue;
                    }
                    authorName = currentTag.getString("webTitle");
                    break;
                }

                //Create a new MyNews object
                MyNews myNews1 = new MyNews(sectionName, webTitle, webPublicationDate, webUrl, authorName);

                //Add the new MyNews object to the list of news
                myNews.add(myNews1);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return myNews;
    }


}
