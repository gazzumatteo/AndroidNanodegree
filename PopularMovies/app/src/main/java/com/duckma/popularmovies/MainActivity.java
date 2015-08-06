package com.duckma.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.duckma.popularmovies.utils.NetworkListAsyncTask;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.ClickCallback {
    MainActivityFragment mMainFragment;

    private boolean mTwoPane;
    private boolean mInFavorite;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (savedInstanceState == null) {
            new NetworkListAsyncTask(mMainFragment).execute("sort_by=popularity.desc");
        }

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
    public void onItemSelected(int movieId) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(MovieDetailFragment.ARG_ITEM_ID, movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, movieId);
            startActivity(detailIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_filter_popular) {
            new NetworkListAsyncTask(mMainFragment).execute("sort_by=popularity.desc");
            mInFavorite = false;
            return true;
        }
        if (id == R.id.action_filter_rated) {
            mInFavorite = false;
            new NetworkListAsyncTask(mMainFragment).execute("sort_by=vote_average.desc");
            return true;
        }

        if (id == R.id.action_filter_favorites) {
            if (mInFavorite) {
                onOptionsItemSelected(mMenu.findItem(R.id.action_filter_popular));
            } else {
                if (mMainFragment.isVisible()) {
                    mInFavorite = true;
                    mMainFragment.loadFavorites();
                }
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
