package com.duckma.popularmovies.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.duckma.popularmovies.Config;
import com.duckma.popularmovies.models.MovieModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 23/07/15.
 */
public class NetworkDetailAsyncTask extends AsyncTask<String, Integer, MovieModel> {


    private HttpURLConnection mUrlConnection = null;
    private BufferedReader mReader = null;

    public NetworkDoneListener networkDoneListener;

    public NetworkDetailAsyncTask(NetworkDoneListener networkDoneListener) {
        this.networkDoneListener = networkDoneListener;
    }

    @Override
    protected MovieModel doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        try {
            /*
             *  CALL TO WS TO RETRIEVE MOVIE. BETTER TO USE VOLLEY OR RETROFIT
             */
            URL url = new URL(Config.BASE_URL + Config.MOVIE_PATH + "/" + params[0] + "?api_key=" + Config.TMDB_API_KEY);
            Log.d("DEBUG", Config.BASE_URL + Config.MOVIE_PATH + "/" + params[0] + "?api_key=" + Config.TMDB_API_KEY);

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
            return null;
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
        MovieModel movieModel = new MovieModel();

        // PARSING, BETTER TO USE GSON, BUT ONLY TO DEMOSTRATE HOW TO PARSE.
        try {
            JSONObject movie = new JSONObject(moviesJsonStr);

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
            movieModel.setRuntime(movie.getInt("runtime"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return movieModel;
    }

    @Override
    protected void onPostExecute(MovieModel movie) {
        if (movie != null)
            networkDoneListener.OnNetworkDone(movie);
    }

    public interface NetworkDoneListener {
        void OnNetworkDone(MovieModel movie);
    }
}



