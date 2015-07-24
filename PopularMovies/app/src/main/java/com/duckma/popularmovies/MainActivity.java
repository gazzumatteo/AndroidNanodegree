package com.duckma.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.duckma.popularmovies.models.MovieModel;
import com.duckma.popularmovies.utils.NetworkAsyncTask;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.ClickCallback {
    MainActivityFragment mMainFragment;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        new NetworkAsyncTask(mMainFragment).execute("sort_by=popularity.desc");

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            mMainFragment.setActivateOnItemClick(true);
            mMainFragment.setTwoPaneMode();
        }
    }

    /**
     * Callback method from {@link MainActivityFragment.ClickCallback}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(MovieModel movie) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(MovieDetailFragment.ARG_ITEM_ID, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, movie);
            startActivity(detailIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_filter_popular) {
            new NetworkAsyncTask(mMainFragment).execute("sort_by=popularity.desc");
            return true;
        }
        if (id == R.id.action_filter_rated) {
            new NetworkAsyncTask(mMainFragment).execute("sort_by=vote_average.desc");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
