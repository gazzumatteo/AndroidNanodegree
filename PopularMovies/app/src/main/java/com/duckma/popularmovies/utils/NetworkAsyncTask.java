package com.duckma.popularmovies.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.duckma.popularmovies.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 23/07/15.
 */
public class NetworkAsyncTask extends AsyncTask<String, Integer, ArrayList<MovieModel>> {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String DISCOVER_PATH = "discover/movie";
    public static final String IMAGES_BASE_PATH = "http://image.tmdb.org/t/p/w185";

    //    private static final String TMDB_API_KEY = "[REPLACE_WITH_YOUR_TMDB_API_KEY]";
    private static final String TMDB_API_KEY = "faf97e5d5bfa445b8b953a0a7bacd059";

    private HttpURLConnection mUrlConnection = null;
    private BufferedReader mReader = null;


    public NetworkDoneListener networkDoneListener;
    ArrayList<MovieModel> mMovies = new ArrayList<>();

    public NetworkAsyncTask(NetworkDoneListener networkDoneListener) {
        this.networkDoneListener = networkDoneListener;
    }

    @Override
    protected ArrayList<MovieModel> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        try {
            /*
             *  CALL TO WS TO RETRIEVE MOVIES. BETTER TO USE VOLLEY OR RETROFIT
             */
            URL url = new URL(BASE_URL + DISCOVER_PATH + "?" + params[0] + "&api_key=" + TMDB_API_KEY);

            // Create the request to OpenWeatherMap, and open the connection
            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod("GET");
            mUrlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = mUrlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                moviesJsonStr = null;
            }
            mReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = mReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                moviesJsonStr = null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            moviesJsonStr = null;
        } finally {
            if (mUrlConnection != null) {
                mUrlConnection.disconnect();
            }
            if (mReader != null) {
                try {
                    mReader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        if (moviesJsonStr != null) {
            // PARSING, BETTER TO USE GSON, BUT ONLY TO DEMOSTRATE HOW TO PARSE.
            try {
                JSONObject result = new JSONObject(moviesJsonStr);
                JSONArray movies = result.getJSONArray("results");
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject movie = (JSONObject) movies.get(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setAdult(movie.getBoolean("adult"));
                    movieModel.setBackdrop_path(movie.getString("backdrop_path"));
                    movieModel.setId(movie.getInt("id"));
                    movieModel.setOriginal_language(movie.getString("original_language"));
                    movieModel.setOriginal_title(movie.getString("original_title"));
                    movieModel.setOverview(movie.getString("overview"));
                    movieModel.setRelease_date(movie.getString("release_date"));
                    movieModel.setPoster_path(movie.getString("poster_path"));
                    movieModel.setPopularity(movie.getDouble("popularity"));
                    movieModel.setTitle(movie.getString("title"));
                    movieModel.setVideo(movie.getBoolean("video"));
                    movieModel.setVote_average(movie.getDouble("vote_average"));
                    movieModel.setVote_count(movie.getInt("vote_count"));

                    mMovies.add(movieModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieModel> movieModels) {
        networkDoneListener.OnNetworkDone(movieModels);
    }

    public interface NetworkDoneListener {
        void OnNetworkDone(ArrayList<MovieModel> movies);
    }
}



