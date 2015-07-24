package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duckma.popularmovies.adapters.MovieAdapter;
import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.utils.NetworkAsyncTask;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements NetworkAsyncTask.NetworkDoneListener {
    private ArrayList<MovieModel> mMovies = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Calculate the grid cell dimension
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int cellHeight = metrics.heightPixels/2;

        // specify an adapter (see also next example)
        mAdapter = new MovieAdapter(mMovies, getActivity());
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void OnNetworkDone(ArrayList<MovieModel> movies) {
        Log.d("TEST", "Downloaded: " + movies.size());
        mMovies.clear();
        mMovies.addAll(movies);
        mAdapter.notifyDataSetChanged();
    }
}
