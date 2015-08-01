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
public interface TrailerService {
    @GET(Config.TRAILER_PATH)
    void getTrailers(@Path("id") int movieId, Callback<VideoModelCall> response);
}
