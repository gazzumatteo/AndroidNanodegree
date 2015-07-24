package com.duckma.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.duckma.popularmovies.utils.NetworkAsyncTask;

public class MainActivity extends AppCompatActivity {
    MainActivityFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            mMainFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mMainFragment).commit();
        }
        new NetworkAsyncTask(mMainFragment).execute("sort_by=popularity.desc");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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
