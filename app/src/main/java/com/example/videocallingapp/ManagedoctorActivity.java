package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ManagedoctorActivity extends AppCompatActivity {
    private Button buttonAddDoctor,buttonShowDoctor,buttonUpdateDoctor,chatButtonDoctor;
    private static int SIGN_IN_REQUEST_CODE = 1;
    ConstraintLayout activity_managedoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managedoctor);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Manage Doctor");
        activity_managedoctor = (ConstraintLayout) findViewById(R.id.activity_managedoctor);
        buttonAddDoctor=(Button)findViewById(R.id.buttonAddDoctor);
        buttonShowDoctor=(Button)findViewById(R.id.buttonShowDoctor);
        buttonUpdateDoctor=(Button)findViewById(R.id.buttonUpdateDoctor);
        chatButtonDoctor=(Button)findViewById(R.id.chatButtonDoctor);
        chatButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ManagedoctorActivity.this,ChatroomActivity.class));
                startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,ChatmessageActivity.class));
            }
        });
        buttonAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,AdddoctorActivity.class));
            }
        });
        buttonShowDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,DoctorActivity.class));
            }
        });
        buttonUpdateDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,DoctorActivity.class));
            }
        });
        /*
        //Another simpler way of sign in and registration without creating registration and login activities
        //--checking whether signed in or not at the start of the admin activity
        //check if not signed in then navigate Sign in page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_admin, "Welcome" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).show();
        }
        // signed in checking finish
        */
    }
    //for three dot menu------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(activity_managedoctor, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,StartActivity.class));

        }
        if (item.getItemId() == R.id.menu_patient) {
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_doctor) {
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this, com.example.videocallingapp.ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ManagedoctorActivity.this,ChatmessageActivity.class));
        }
        return true;
    }
    //end for three dot menu------------------------------
}

