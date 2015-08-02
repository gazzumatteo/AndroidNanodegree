package com.duckma.popularmovies.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duckma.popularmovies.R;
import com.duckma.popularmovies.models.DetailModel;

import java.util.ArrayList;

/**
 * Copyright © 2015 DuckMa S.r.l. - http://duckma.com
 * <p/>
 * Created by Matteo Gazzurelli on 01/08/15.
 */
public class MovieDetailAdapter extends BaseAdapter {
    private ArrayList<DetailModel> mDetailObj = new ArrayList<>();
    private Context mContext;

    public MovieDetailAdapter(Context context, ArrayList<DetailModel> objects) {
        mContext = context;
        mDetailObj = objects;
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
            holder.rlContainer = (RelativeLayout) v.findViewById(R.id.rlContainer);
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            holder.tvDescription = (TextView) v.findViewById(R.id.tvTitle);

            // associate the holder with the view for later lookup
            v.setTag(holder);
        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }

        DetailModel detailObj = mDetailObj.get(position);
        float height = mContext.getResources().getDimension(R.dimen.detail_item_height);
        switch (detailObj.getContentType()) {
            case DetailModel.TYPE_SEPARATOR:
                height = mContext.getResources().getDimension(R.dimen.separator_height);
                holder.rlContainer.setMinimumHeight(Math.round(height));
                holder.ivIcon.setVisibility(View.GONE);
                holder.tvDescription.setTypeface(Typeface.DEFAULT_BOLD);
                holder.tvDescription.setText(detailObj.getName());
                break;
            case DetailModel.TYPE_TRAILER:
                holder.rlContainer.setMinimumHeight(Math.round(height));
                holder.tvDescription.setText(detailObj.getName());
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.playbutton));
                holder.tvDescription.setTypeface(Typeface.DEFAULT);
                break;
            case DetailModel.TYPE_REVIEW:
                holder.rlContainer.setMinimumHeight(Math.round(height));
                String label = (detailObj.getContent().length() >= 50) ? detailObj.getContent().substring(0, 50) : detailObj.getContent();
                holder.tvDescription.setText(detailObj.getAuthor() + " - " + label);
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.reviewsbutton));
                holder.tvDescription.setTypeface(Typeface.DEFAULT);
                break;
        }

        return v;
    }

    @Override
    public int getCount() {
        return mDetailObj.size();
    }

    @Override
    public Object getItem(int position) {
        return mDetailObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        RelativeLayout rlContainer;
        TextView tvDescription;
        ImageView ivIcon;

    }

}
