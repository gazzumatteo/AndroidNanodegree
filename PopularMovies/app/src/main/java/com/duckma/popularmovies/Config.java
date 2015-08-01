package com.duckma.popularmovies;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 24/07/15.
 */
public class Config {
        private static final String TMDB_API_KEY = "[REPLACE_WITH_YOUR_TMDB_API_KEY]";

    public static final String BASE_URL = "http://api.themoviedb.org/3";
    public static final String DISCOVER_PATH = "/discover/movie";
    public static final String MOVIE_PATH = "/movie";
    public static final String TRAILER_PATH = "/movie/{id}/videos?api_key=" + TMDB_API_KEY;
    public static final String REVIEWS_PATH = "/movie/{id}/reviews?api_key=" + TMDB_API_KEY;
    public static final String IMAGES_BASE_PATH = "http://image.tmdb.org/t/p/w185";

}
