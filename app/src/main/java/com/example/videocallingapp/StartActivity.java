package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    Button buttonStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Health Consultant");
        buttonStart=(Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.StartActivity.this,LoginActivity.class));
            }
        });
      }
}
