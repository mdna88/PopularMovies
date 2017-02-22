package com.narvin.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
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
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Class responsible for connecting to the MoviesDB API, parsing the data
 * and returning a loader containing the requested data
 */
public class MoviesLoader extends AsyncTaskLoader<ArrayList<Movies>> {

    private Context mContext;

    public MoviesLoader(Context context) {
        super(context);
        mContext = context;

    }

    /**
     * @param content JSON formatted string to be parsed from moviesDB API
     * @return MoviesList to be displayed in the gridView
     */
    public static ArrayList<Movies> parseJsonFeed(String content) {

        ArrayList<Movies> moviesList = new ArrayList<>();

        //Begin JSON parsing, retrieves article data and constructs the articles in a Array list
        try {
            JSONObject jsonObject = new JSONObject(content);

            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentItem = jsonArray.getJSONObject(i);

                String title = currentItem.getString("title");

                String overview = currentItem.getString("overview");

                String releaseDate = currentItem.getString("release_date");

                final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

                Uri builtUri = Uri.parse(IMAGE_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath("w185/")
                        .appendEncodedPath(currentItem.getString("poster_path"))
                        .build();

                double rating = currentItem.getDouble("vote_average");

                moviesList.add(new Movies(title, overview, releaseDate, builtUri.toString(), rating));

            }
            return moviesList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Checks user movie sorting preference (Popular or top rated) and builds URL based on that preference
     */
    public URL generateUrl() {

        // Get user shared preferences
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        // Gets the stored movie sorting preference
        String sortPreference = sharedPrefs.getString(
                mContext.getString(R.string.pref_units_key),
                mContext.getString(R.string.pref_sort_popular));

        String BASE_URL = "";
        switch (sortPreference) {
            case "popular":
                BASE_URL = mContext.getString(R.string.base_url_popular);
                break;

            case "rating":
                BASE_URL = mContext.getString(R.string.base_url_rating);
                break;

        }

        final String LANGUAGE_PARAM = "language";
        final String PAGE_PARAM = "page";
        final String KEY_PARAM = "api_key";

        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(KEY_PARAM, BuildConfig.API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter(PAGE_PARAM, "1")
                .build();

        try {
            URL url = new URL(builtUri.toString());

            return url;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public ArrayList<Movies> loadInBackground() {

        if (isOnline()) {
            return parseJsonFeed(makeHttpRequest(generateUrl()));
        }
        return null;
    }

    /**
     * @param url movieDB APIs set Url to retrieve the data from
     * @return JSON formatted String
     */
    private String makeHttpRequest(URL url) {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream stream = null;

        //Attempt to make connection to the API
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            stream = urlConnection.getInputStream();
            jsonResponse = readFromStream(stream);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;

    }

    /**
     * Helper method that extracts the data line by line and composes a final JSON String
     *
     * @param inputStream total output from HTTP Request
     * @return JSON formatted String
     */
    private String readFromStream(InputStream inputStream) {
        StringBuilder result = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    // Checks internet connectivity.
    protected boolean isOnline() {

        ConnectivityManager connectManager = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}