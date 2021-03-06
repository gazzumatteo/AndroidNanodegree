package com.duckma.popularmovies;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duckma.popularmovies.adapters.MovieDetailAdapter;
import com.duckma.popularmovies.api.DetailModelCall;
import com.duckma.popularmovies.api.DetailService;
import com.duckma.popularmovies.models.DetailModel;
import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.provider.MovieProvider;
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
    public static final String BUNDLE_ITEM_DETAILS = "downloaded_movie_details";
    private int mMovieId;
    TextView tvMovieTitle, tvYear, tvMovieLength, tvMovieScore, tvMovieDescription;
    ImageView ivMoviePoster;
    ImageButton mBtnFavorite;
    TextView mTvFavorites;
    MovieModel mMovie;
    ListView mListView;
    ArrayList<DetailModel> mDetailObjects = new ArrayList<>();
    MovieDetailAdapter mAdapter;
    RestAdapter mRestAdapter;
    DetailModel videoToShareDetail;
    boolean mIsFavorite;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (savedInstanceState == null && getArguments().containsKey(ARG_ITEM_ID)) {
            mMovieId = getArguments().getInt(ARG_ITEM_ID);
            mIsFavorite = isInFavorites();
        } else {
            return;
        }

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.BASE_URL)
                .build();
    }

    private void getMovieFromContentProvider() {
        mMovie = new MovieModel();
        Uri movieUri = Uri.withAppendedPath(MovieProvider.MOVIES_CONTENT_URI, String.valueOf(mMovieId));
        String[] projection = {MovieProvider.Movie.KEY_ID,
                MovieProvider.Movie.KEY_OVERVIEW, MovieProvider.Movie.KEY_RELEASE_DATE,
                MovieProvider.Movie.KEY_POSTER_PATH, MovieProvider.Movie.KEY_TITLE,
                MovieProvider.Movie.KEY_VOTE_AVERAGE, MovieProvider.Movie.KEY_RUNTIME
        };

        // get all movies in db
        Cursor c = getActivity().getContentResolver().query(movieUri, projection, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToNext();
            mMovie.setId(c.getInt(c.getColumnIndex(MovieProvider.Movie.KEY_ID)));
            mMovie.setOverview(c.getString(c.getColumnIndex(MovieProvider.Movie.KEY_OVERVIEW)));
            mMovie.setRelease_date(c.getString(c.getColumnIndex(MovieProvider.Movie.KEY_RELEASE_DATE)));
            mMovie.setPoster_path(c.getString(c.getColumnIndex(MovieProvider.Movie.KEY_POSTER_PATH)));
            mMovie.setTitle(c.getString(c.getColumnIndex(MovieProvider.Movie.KEY_TITLE)));
            mMovie.setVote_average(c.getDouble(c.getColumnIndex(MovieProvider.Movie.KEY_VOTE_AVERAGE)));
            mMovie.setRuntime(c.getInt(c.getColumnIndex(MovieProvider.Movie.KEY_RUNTIME)));
            c.close();
        }

        // get all the details
        String[] detailsProjection = {MovieProvider.MovieDetail.KEY_ID,
                MovieProvider.MovieDetail.KEY_MOVIE_ID,MovieProvider.MovieDetail.KEY_CONTENT_TYPE,
                MovieProvider.MovieDetail.KEY_KEY,MovieProvider.MovieDetail.KEY_NAME,
                MovieProvider.MovieDetail.KEY_SITE,MovieProvider.MovieDetail.KEY_AUTHOR,
                MovieProvider.MovieDetail.KEY_CONTENT
        };

        // get all movie details in db
        c = getActivity().getContentResolver().query(MovieProvider.MOVIE_DETAILS_CONTENT_URI, detailsProjection, MovieProvider.MovieDetail.KEY_MOVIE_ID + "=" + mMovie.getId(), null, null);
        if(c!= null && c.getCount() > 0) {
            DetailModel detailModel;
            mDetailObjects.clear();
            while (c.moveToNext()){
                detailModel = new DetailModel();
                detailModel.setContentType(c.getInt(c.getColumnIndex(MovieProvider.MovieDetail.KEY_CONTENT_TYPE)));
                detailModel.setKey(c.getString(c.getColumnIndex(MovieProvider.MovieDetail.KEY_KEY)));
                detailModel.setName(c.getString(c.getColumnIndex(MovieProvider.MovieDetail.KEY_NAME)));
                detailModel.setSite(c.getString(c.getColumnIndex(MovieProvider.MovieDetail.KEY_SITE)));
                detailModel.setAuthor(c.getString(c.getColumnIndex(MovieProvider.MovieDetail.KEY_AUTHOR)));
                detailModel.setContent(c.getString(c.getColumnIndex(MovieProvider.MovieDetail.KEY_CONTENT)));
                mDetailObjects.add(detailModel);
            }
            c.close();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mIsFavorite) {
            getMovieFromContentProvider();
            populateFields();
        } else {
            new NetworkDetailAsyncTask(this).execute(String.valueOf(mMovieId));
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ITEM)) {
            mMovie = (MovieModel) savedInstanceState.getSerializable(BUNDLE_ITEM);
            populateFields();
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_ITEM_DETAILS)) {
            ArrayList<DetailModel> tmpDetailObjects = (ArrayList<DetailModel>) savedInstanceState.getSerializable(BUNDLE_ITEM_DETAILS);
            if (tmpDetailObjects != null) {
                for (DetailModel model : tmpDetailObjects) {
                    mDetailObjects.add(model);
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (!mIsFavorite)
                getTrailers();
        }
    }

    private void getTrailers() {

        mDetailObjects.clear();

        DetailService trailerService = mRestAdapter.create(DetailService.class);
        // Add Trailers
        trailerService.getTrailers(mMovieId, new Callback<DetailModelCall>() {
            @Override
            public void success(DetailModelCall detailModelCall, Response response) {
                if (detailModelCall.getResults().size() > 0) {
                    videoToShareDetail = detailModelCall.getResults().get(0);
                    // Add Separator
                    addSeparator("Trailers");
                    for (DetailModel trailer : detailModelCall.getResults()) {
                        trailer.setContentType(DetailModel.TYPE_TRAILER);
                        mDetailObjects.add(trailer);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                getReviews();
            }

            @Override
            public void failure(RetrofitError error) {
                getReviews(); // get reviews either it fails
            }
        });

    }

    private void getReviews() {
        DetailService reviewService = mRestAdapter.create(DetailService.class);
        // Add Reviews
        reviewService.getReviews(mMovieId, new Callback<DetailModelCall>() {
            @Override
            public void success(DetailModelCall detailModelCall, Response response) {
                if (detailModelCall.getResults().size() > 0) {
                    // Add Separator
                    addSeparator("Reviews");
                    for (DetailModel review : detailModelCall.getResults()) {
                        review.setContentType(DetailModel.TYPE_REVIEW);
                        mDetailObjects.add(review);
                    }
                    mAdapter.notifyDataSetChanged();
                }
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


        if (mMovieId != -1) {
            View header = getActivity().getLayoutInflater().inflate(R.layout.movie_detail_header, null);
            tvMovieTitle = ((TextView) header.findViewById(R.id.tvMovieTitle));
            ivMoviePoster = ((ImageView) header.findViewById(R.id.ivMoviePoster));
            tvYear = (TextView) header.findViewById(R.id.tvYear);
            tvMovieLength = (TextView) header.findViewById(R.id.tvMovieLength);
            tvMovieScore = (TextView) header.findViewById(R.id.tvMovieScore);
            tvMovieDescription = (TextView) header.findViewById(R.id.tvMovieDescription);
            mBtnFavorite = (ImageButton) header.findViewById(R.id.btnFavorite);
            mTvFavorites = (TextView) header.findViewById(R.id.tvFavorite);
            mBtnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRemoveMovieToFavorites();
                }
            });
            mListView.addHeaderView(header);
        }

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // NOTE: position == position-1 because the first position is the header
                DetailModel itemClicked = mDetailObjects.get(position - 1);
                switch (itemClicked.getContentType()) {
                    case DetailModel.TYPE_SEPARATOR:
                        // do nothing, is a separator
                        break;
                    case DetailModel.TYPE_TRAILER:
                        if (itemClicked.getSite().equals("YouTube")) {
                            Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + itemClicked.getKey()));
                            startActivity(videoIntent);
                        }
                        break;
                    case DetailModel.TYPE_REVIEW:
                        displayReview(itemClicked);
                        break;
                }
            }
        });
        return rootView;
    }

    // TODO Add Details
    private void addRemoveMovieToFavorites() {
        Uri movieUri = Uri.withAppendedPath(MovieProvider.MOVIES_CONTENT_URI, String.valueOf(mMovie.getId()));

        if (mIsFavorite) { // present in db, remove it
            int count = getActivity().getContentResolver().delete(movieUri, null, null);
            if (count > 0) {
                Toast.makeText(getActivity(), "Successfully removed from Favorites", Toast.LENGTH_SHORT).show();
                mTvFavorites.setText(getResources().getString(R.string.btn_favorite));
                mBtnFavorite.setImageResource(android.R.drawable.star_big_off);
                mIsFavorite = false;
            }

            // remove all the details
            count = getActivity().getContentResolver().delete(MovieProvider.MOVIE_DETAILS_CONTENT_URI, MovieProvider.MovieDetail.KEY_MOVIE_ID + "=" + mMovie.getId(), null);
        } else { // insert in db
            ContentValues mMovieValues = new ContentValues();
            mMovieValues.put(MovieProvider.Movie.KEY_ID, mMovie.getId());
            mMovieValues.put(MovieProvider.Movie.KEY_OVERVIEW, mMovie.getOverview());
            mMovieValues.put(MovieProvider.Movie.KEY_TITLE, mMovie.getTitle());
            mMovieValues.put(MovieProvider.Movie.KEY_POSTER_PATH, mMovie.getPoster_path());
            mMovieValues.put(MovieProvider.Movie.KEY_RELEASE_DATE, mMovie.getRelease_date());
            mMovieValues.put(MovieProvider.Movie.KEY_RUNTIME, mMovie.getRuntime());
            mMovieValues.put(MovieProvider.Movie.KEY_VOTE_AVERAGE, mMovie.getVote_average());

            movieUri = getActivity().getContentResolver().insert(MovieProvider.MOVIES_CONTENT_URI, mMovieValues);
            if (movieUri != null) {
                mIsFavorite = true;
                Toast.makeText(getActivity(), "Successfully added to Favorites", Toast.LENGTH_SHORT).show();
                mTvFavorites.setText(getResources().getString(R.string.btn_favorite_remove));
                mBtnFavorite.setImageResource(android.R.drawable.star_big_on);
            }

            ContentValues mMovieDetailValues;
            // Add Movie Details to db
            for (DetailModel detailModel : mDetailObjects) {
                mMovieDetailValues = new ContentValues();
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_MOVIE_ID, mMovieId);
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_CONTENT_TYPE, detailModel.getContentType());
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_KEY, detailModel.getKey());
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_NAME, detailModel.getName());
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_SITE, detailModel.getSite());
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_AUTHOR, detailModel.getAuthor());
                mMovieDetailValues.put(MovieProvider.MovieDetail.KEY_CONTENT, detailModel.getContent());

                getActivity().getContentResolver().insert(MovieProvider.MOVIE_DETAILS_CONTENT_URI, mMovieDetailValues);
            }
        }
    }

    /**
     * check if a movie is in Favorite
     *
     * @return true if is in DB
     */
    private boolean isInFavorites() {
        String[] projection = {MovieProvider.Movie.KEY_ID};
        Uri movieUri = Uri.withAppendedPath(MovieProvider.MOVIES_CONTENT_URI, String.valueOf(mMovieId));
        // check if the movie is in db
        Cursor c = getActivity().getContentResolver().query(movieUri, projection, null, null, null);
        if (c != null && c.getCount() > 0) { // present in db, remove it
            c.close();
            return true;
        }
        return false;
    }

    @Override
    public void OnNetworkDone(MovieModel movie) {
        if (isVisible()) { // populate only if the fragment is visible
            mMovie = movie;
            populateFields();
        }
    }

    private void populateFields() {
        tvMovieTitle.setText(mMovie.getTitle());
        Picasso.with(getActivity()).load(mMovie.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(ivMoviePoster);
        if (mMovie.getRelease_date() != null && !mMovie.getRelease_date().isEmpty())
            tvYear.setText(mMovie.getRelease_date().substring(0, 4));
        if (mMovie.getRuntime() > -1)
            tvMovieLength.setText(mMovie.getRuntime() + " min");
        tvMovieScore.setText(String.valueOf(mMovie.getVote_average()) + "/10");
        tvMovieDescription.setText(mMovie.getOverview());

        if (mIsFavorite) {
            mTvFavorites.setText(getResources().getString(R.string.btn_favorite_remove));
            mBtnFavorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            mTvFavorites.setText(getResources().getString(R.string.btn_favorite));
            mBtnFavorite.setImageResource(android.R.drawable.star_big_off);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putSerializable(BUNDLE_ITEM, mMovie);
        if (mDetailObjects != null)
            outState.putSerializable(BUNDLE_ITEM_DETAILS, mDetailObjects);
    }

    private void displayReview(DetailModel detail) {

        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(getActivity());
        AlertBuilder.setTitle(detail.getAuthor());
        AlertBuilder.setMessage(detail.getContent());
        AlertBuilder.setCancelable(true);
        AlertBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = AlertBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (videoToShareDetail != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + videoToShareDetail.getKey());
                startActivity(Intent.createChooser(sendIntent, "Share URL"));
            }
            return true;
        }
        return false;
    }
}
