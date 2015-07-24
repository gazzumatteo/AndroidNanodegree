package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.utils.NetworkDetailAsyncTask;
import com.squareup.picasso.Picasso;


public class MovieDetailFragment extends Fragment implements NetworkDetailAsyncTask.NetworkDoneListener {
    public static final String ARG_ITEM_ID = "movie_id";
    private int mMovieId;
    TextView tvMovieTitle, tvYear, tvMovieLength, tvMovieScore, tvMovieDescription;
    ImageView ivMoviePoster;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mMovieId = getArguments().getInt(ARG_ITEM_ID);
            new NetworkDetailAsyncTask(this).execute(String.valueOf(mMovieId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (mMovieId != -1) {
            tvMovieTitle = ((TextView) rootView.findViewById(R.id.tvMovieTitle));
            ivMoviePoster = ((ImageView) rootView.findViewById(R.id.ivMoviePoster));
            tvYear = (TextView) rootView.findViewById(R.id.tvYear);
            tvMovieLength = (TextView) rootView.findViewById(R.id.tvMovieLength);
            tvMovieScore = (TextView) rootView.findViewById(R.id.tvMovieScore);
            tvMovieDescription = (TextView) rootView.findViewById(R.id.tvMovieDescription);
        }

        return rootView;
    }


    @Override
    public void OnNetworkDone(MovieModel movie) {

        tvMovieTitle.setText(movie.getTitle());
        Picasso.with(getActivity()).load(movie.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(ivMoviePoster);
        tvYear.setText(movie.getRelease_date().substring(0, 4));
        tvMovieLength.setText(movie.getRuntime() + "min");
        tvMovieScore.setText(String.valueOf(movie.getVote_average()) + "/10");
        tvMovieDescription.setText(movie.getOverview());
    }
}
