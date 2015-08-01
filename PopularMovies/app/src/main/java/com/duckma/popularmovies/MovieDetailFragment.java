package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.duckma.popularmovies.adapters.MovieDetailAdapter;
import com.duckma.popularmovies.api.DetailModelCall;
import com.duckma.popularmovies.api.DetailService;
import com.duckma.popularmovies.models.DetailModel;
import com.duckma.popularmovies.models.MovieModel;
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
    ArrayList<DetailModel> mDetailObjects = new ArrayList<>();
    MovieDetailAdapter mAdapter;
    RestAdapter mRestAdapter;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null && getArguments().containsKey(ARG_ITEM_ID)) {
            mMovieId = getArguments().getInt(ARG_ITEM_ID);
            new NetworkDetailAsyncTask(this).execute(String.valueOf(mMovieId));
        }

        mRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(Config.BASE_URL)
                .build();
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

        mDetailObjects.clear();

        DetailService trailerService = mRestAdapter.create(DetailService.class);
        // Add Trailers
        trailerService.getTrailers(mMovieId, new Callback<DetailModelCall>() {
            @Override
            public void success(DetailModelCall detailModelCall, Response response) {
                // Add Separator
                addSeparator("Trailers");

                for (DetailModel trailer : detailModelCall.getResults()) {
                    trailer.setContentType(DetailModel.TYPE_TRAILER);
                    mDetailObjects.add(trailer);
                }
                mAdapter.notifyDataSetChanged();
                getReviews();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void getReviews() {
        DetailService reviewService = mRestAdapter.create(DetailService.class);
        // Add Reviews
        reviewService.getReviews(mMovieId, new Callback<DetailModelCall>() {
            @Override
            public void success(DetailModelCall detailModelCall, Response response) {
                // Add Separator
                addSeparator("Reviews");
                for (DetailModel review : detailModelCall.getResults()) {
                    Log.d("TEST", review.getAuthor());
                    review.setContentType(DetailModel.TYPE_REVIEW);
                    mDetailObjects.add(review);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void addSeparator(String title) {
        DetailModel separatorModel = new DetailModel();
        separatorModel.setContentType(DetailModel.TYPE_SEPARATOR);
        separatorModel.setName(title);
        mDetailObjects.add(separatorModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mListView = (ListView) rootView.findViewById(R.id.lvMovieTrailers);
        mAdapter = new MovieDetailAdapter(getActivity(), mDetailObjects);
        mListView.setAdapter(mAdapter);

        if (mMovieId != -1) {
            View header = getActivity().getLayoutInflater().inflate(R.layout.movie_detail_header, null);
            tvMovieTitle = ((TextView) header.findViewById(R.id.tvMovieTitle));
            ivMoviePoster = ((ImageView) header.findViewById(R.id.ivMoviePoster));
            tvYear = (TextView) header.findViewById(R.id.tvYear);
            tvMovieLength = (TextView) header.findViewById(R.id.tvMovieLength);
            tvMovieScore = (TextView) header.findViewById(R.id.tvMovieScore);
            tvMovieDescription = (TextView) header.findViewById(R.id.tvMovieDescription);
            mListView.addHeaderView(header);
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailModel itemClicked = mDetailObjects.get(position);
                switch (itemClicked.getContentType()) {
                    case DetailModel.TYPE_SEPARATOR:
                        break;
                    case DetailModel.TYPE_TRAILER:

                        break;
                    case DetailModel.TYPE_REVIEW:

                        break;
                }
            }

        });
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
