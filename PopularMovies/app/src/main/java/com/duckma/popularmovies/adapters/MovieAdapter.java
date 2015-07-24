package com.duckma.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.duckma.popularmovies.R;
import com.duckma.popularmovies.models.MovieModel;

import java.util.ArrayList;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 24/07/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<MovieModel> mMovies = new ArrayList<>();
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvMoviePoster;


        public ViewHolder(View v) {
            super(v);
            mIvMoviePoster = (ImageView) v.findViewById(R.id.ivMoviePoster);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieAdapter(ArrayList<MovieModel> movies, Context context) {
        this.mMovies = movies;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MovieModel movie = mMovies.get(position);
        Log.d("ADAPTER", movie.getPoster_path());
        Glide.with(mContext)
                .load(movie.getPoster_path())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(holder.mIvMoviePoster);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}