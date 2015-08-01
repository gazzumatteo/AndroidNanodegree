package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.duckma.popularmovies.adapters.MovieDetailAdapter;
import com.duckma.popularmovies.api.TrailerService;
import com.duckma.popularmovies.api.VideoModelCall;
import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.models.VideoModel;
import com.duckma.popularmovies.utils.NetworkDetailAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MovieDetailFragment extends Fragment implements NetworkDetailAsyncTask.NetworkDoneListener {
    public static final String ARG_ITEM_ID = "movie_id";
    public static final String BUNDLE_ITEM = "downloaded_movie";
    private int mMovieId;
    TextView tvMovieTitle, tvYear, tvMovieLength, tvMovieScore, tvMovieDescription;
    ImageView ivMoviePoster;
    MovieModel mMovie;
    ListView mListView;
    ArrayList<VideoModel> mVideos = new ArrayList<>();
    MovieDetailAdapter mAdapter;



    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null && getArguments().containsKey(ARG_ITEM_ID)) {
            mMovieId = getArguments().getInt(ARG_ITEM_ID);
            new NetworkDetailAsyncTask(this).execute(String.valueOf(mMovieId));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ITEM)) {
            mMovie = (MovieModel) savedInstanceState.getSerializable(BUNDLE_ITEM);
            populateFields();
        }
        getTrailers();
    }

    private void getTrailers() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.BASE_URL)
                .build();

        TrailerService service = restAdapter.create(TrailerService.class);
        service.getTrailers(mMovieId, new Callback<VideoModelCall>() {
            @Override
            public void success(VideoModelCall videoModelCall, Response response) {
                Log.d("Count", "sixe: " + videoModelCall.getResults().size());
                mVideos.clear();
                for (VideoModel video:videoModelCall.getResults()) {
                    mVideos.add(video);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mListView = (ListView) rootView.findViewById(R.id.lvMovieTrailers);
        mAdapter = new MovieDetailAdapter(getActivity(), mVideos);
        mListView.setAdapter(mAdapter);
        tvMovieTitle = ((TextView) rootView.findViewById(R.id.tvMovieTitle));

        if (mMovieId != -1) {
            View header = getActivity().getLayoutInflater().inflate(R.layout.movie_detail_header, null);
            ivMoviePoster = ((ImageView) header.findViewById(R.id.ivMoviePoster));
            tvYear = (TextView) header.findViewById(R.id.tvYear);
            tvMovieLength = (TextView) header.findViewById(R.id.tvMovieLength);
            tvMovieScore = (TextView) header.findViewById(R.id.tvMovieScore);
            tvMovieDescription = (TextView) header.findViewById(R.id.tvMovieDescription);
            mListView.addHeaderView(header);
        }

        return rootView;
    }


    @Override
    public void OnNetworkDone(MovieModel movie) {
        if (isVisible()) { // populate only if the fragment is visible
            mMovie = movie;
            populateFields();
        }
    }

    private void populateFields() {
        getActivity().setTitle(mMovie.getTitle());
        tvMovieTitle.setText(mMovie.getTitle());
        Picasso.with(getActivity()).load(mMovie.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(ivMoviePoster);
        tvYear.setText(mMovie.getRelease_date().substring(0, 4));
        if (mMovie.getRuntime() > -1)
            tvMovieLength.setText(mMovie.getRuntime() + " min");
        tvMovieScore.setText(String.valueOf(mMovie.getVote_average()) + "/10");
        tvMovieDescription.setText(mMovie.getOverview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putSerializable(BUNDLE_ITEM, mMovie);

    }
}
