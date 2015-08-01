package com.duckma.popularmovies.api;

import com.duckma.popularmovies.models.DetailModel;

import java.util.List;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 01/08/15.
 */
public class DetailModelCall {
    int id;
    List<DetailModel> results;

    public List<DetailModel> getResults() {
        return results;
    }

    public void setResults(List<DetailModel> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
