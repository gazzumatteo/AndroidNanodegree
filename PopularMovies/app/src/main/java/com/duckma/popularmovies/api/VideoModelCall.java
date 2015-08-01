package com.duckma.popularmovies.api;

import com.duckma.popularmovies.models.VideoModel;

import java.util.List;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 01/08/15.
 */
public class VideoModelCall {
    int id;
    List<VideoModel> results;

    public List<VideoModel> getResults() {
        return results;
    }

    public void setResults(List<VideoModel> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
