package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ManagevideoActivity extends AppCompatActivity {
    private Button buttonAddVideo,buttonShowVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managevideo);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Manage Video");
        buttonAddVideo=(Button)findViewById(R.id.buttonAddVideo);
        buttonShowVideo=(Button)findViewById(R.id.buttonShowVideo);
        buttonShowVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagevideoActivity.this,ShowvideoActivity.class));
            }
        });

        buttonAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagevideoActivity.this,AddvideoActivity.class));
            }
        });
    }
}

