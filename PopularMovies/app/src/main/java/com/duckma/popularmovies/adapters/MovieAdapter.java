package com.duckma.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.duckma.popularmovies.R;
import com.duckma.popularmovies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 24/07/15.
 */
public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieModel> mMovies = new ArrayList<>();
    private Context mContext;

    public MovieAdapter(Context context, ArrayList<MovieModel> objects) {
        mContext = context;
        mMovies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.movie_view, parent, false);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.mIvMoviePoster = (ImageView) v.findViewById(R.id.ivMoviePoster);
            // associate the holder with the view for later lookup
            v.setTag(holder);
        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }
        MovieModel movie = mMovies.get(position);

//      Decomment to use Picasso
        Picasso.with(mContext).load(movie.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(holder.mIvMoviePoster);

//        Glide.with(mContext)
//                .load(movie.getPoster_path())
//                .placeholder(R.drawable.placeholder)
//                .crossFade()
//                .into(holder.mIvMoviePoster);

        return v;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView mIvMoviePoster;
    }

}