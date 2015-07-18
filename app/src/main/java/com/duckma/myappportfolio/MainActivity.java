package com.duckma.myappportfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main_streamer).setOnClickListener(this);
        findViewById(R.id.btn_main_scores).setOnClickListener(this);
        findViewById(R.id.btn_main_library).setOnClickListener(this);
        findViewById(R.id.btn_main_build_bigger).setOnClickListener(this);
        findViewById(R.id.btn_main_xyz).setOnClickListener(this);
        findViewById(R.id.btn_main_capstone).setOnClickListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        String text = "";
        switch (v.getId()){
            case R.id.btn_main_streamer:
                text = "This button displays Spotify Streamer";
                break;
            case R.id.btn_main_scores:
                text = "This button displays Scores App";
                break;
            case R.id.btn_main_library:
                text = "This button displays Library App";
                break;
            case R.id.btn_main_build_bigger:
                text = "This button displays Build It Bigger";
                break;
            case R.id.btn_main_xyz:
                text = "This button displays XYZ Reader";
                break;
            case R.id.btn_main_capstone:
                text = "This button displays Capstone: My Own App";
                break;
            default:
                break;
        }

        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
