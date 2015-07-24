package com.duckma.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.duckma.popularmovies.adapters.MovieAdapter;
import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.utils.NetworkAsyncTask;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements NetworkAsyncTask.NetworkDoneListener {
    private ArrayList<MovieModel> mMovies = new ArrayList<>();
    private MovieAdapter mAdapter;
    private GridView mGridView;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mActivatedPosition = GridView.INVALID_POSITION;

    private ClickCallback mCallbacks = sDummyCallbacks;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        mGridView = (GridView) view.findViewById(R.id.gridview);


//        Calculate the grid cell dimension better screen size support improvement
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int cellHeight = metrics.heightPixels/2;

        mAdapter = new MovieAdapter(getActivity(), mMovies);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallbacks.onItemSelected(mMovies.get(position));
            }
        });
    }

    @Override
    public void OnNetworkDone(ArrayList<MovieModel> movies) {
        Log.d("TEST", "Downloaded: " + movies.size());
        mMovies.clear();
        mMovies.addAll(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ClickCallback)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (ClickCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != -1) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mGridView.setChoiceMode(activateOnItemClick
                ? GridView.CHOICE_MODE_SINGLE
                : GridView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == GridView.INVALID_POSITION) {
            mGridView.setItemChecked(mActivatedPosition, false);
        } else {
            mGridView.setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    private static ClickCallback sDummyCallbacks = new ClickCallback() {
        @Override
        public void onItemSelected(MovieModel movie) {
        }
    };

    public interface ClickCallback {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(MovieModel movie);
    }
}
