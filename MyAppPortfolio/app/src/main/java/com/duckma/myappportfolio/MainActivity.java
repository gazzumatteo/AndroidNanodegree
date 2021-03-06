package com.duckma.myappportfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Toast mToast;

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
    public void onClick(View v) {
        if(mToast != null ) mToast.cancel();

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

        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
