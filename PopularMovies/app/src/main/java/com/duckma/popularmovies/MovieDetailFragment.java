package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duckma.popularmovies.models.MovieModel;
import com.squareup.picasso.Picasso;


public class MovieDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private MovieModel mItem;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (MovieModel) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.tvMovieTitle)).setText(mItem.getTitle());
            ImageView ivMoviePoster = ((ImageView) rootView.findViewById(R.id.ivMoviePoster));
            Picasso.with(getActivity()).load(mItem.getPoster_path())
                    .placeholder(R.drawable.placeholder)
                    .into(ivMoviePoster);
            ((TextView) rootView.findViewById(R.id.tvYear)).setText(mItem.getRelease_date().substring(0, 4));
//            ((TextView) rootView.findViewById(R.id.tvMovieLength)).setText(mItem.get);
        }

        return rootView;
    }
}
