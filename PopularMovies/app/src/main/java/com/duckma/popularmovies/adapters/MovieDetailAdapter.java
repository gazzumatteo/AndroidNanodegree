package com.duckma.popularmovies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duckma.popularmovies.R;
import com.duckma.popularmovies.models.VideoModel;

import java.util.ArrayList;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 01/08/15.
 */
public class MovieDetailAdapter extends BaseAdapter {
    private ArrayList<VideoModel> mVideos = new ArrayList<>();
    private Context mContext;

    public MovieDetailAdapter(Context context, ArrayList<VideoModel> objects) {
        mContext = context;
        mVideos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.movie_detail_item, parent, false);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.mTvDescription = (TextView) v.findViewById(R.id.tvTitle);
            // associate the holder with the view for later lookup
            v.setTag(holder);
        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }
        VideoModel video = mVideos.get(position);
        holder.mTvDescription.setText(video.getName());
        if (v.isSelected()) {
            Log.d("View", "selected " + video.getName());
        }

        return v;
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public Object getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        TextView mTvDescription;
    }

}
