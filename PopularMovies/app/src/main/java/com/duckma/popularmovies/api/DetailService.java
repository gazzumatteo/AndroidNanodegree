package com.duckma.popularmovies.api;

import com.duckma.popularmovies.Config;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 01/08/15.
 */
public interface DetailService {
    @GET(Config.TRAILER_PATH)
    void getTrailers(@Path("id") int movieId, Callback<DetailModelCall> response);

    @GET(Config.REVIEWS_PATH)
    void getReviews(@Path("id") int movieId, Callback<DetailModelCall> response);
}
