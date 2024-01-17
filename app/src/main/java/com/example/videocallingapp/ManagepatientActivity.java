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

public class ManagepatientActivity extends AppCompatActivity {
    private Button buttonAddPatient,buttonShowPatient,buttonUpdatePatient,chatButtonPatient;
    private static int SIGN_IN_REQUEST_CODE = 1;
    ConstraintLayout activity_managepatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managepatient);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Manage Patient");
        activity_managepatient = (ConstraintLayout) findViewById(R.id.activity_managepatient);
        buttonAddPatient=(Button)findViewById(R.id.buttonAddPatient);
        buttonShowPatient=(Button)findViewById(R.id.buttonShowPatient);
        buttonUpdatePatient=(Button)findViewById(R.id.buttonUpdatePatient);
        chatButtonPatient=(Button)findViewById(R.id.chatButtonPatient);
        chatButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ManagepatientActivity.this,ChatroomActivity.class));
                startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ChatmessageActivity.class));
            }
        });
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,AddpatientActivity.class));
            }
        });
        buttonShowPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ShowpatientActivity.class));
            }
        });
        buttonUpdatePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ShowpatientActivity.class));
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
            Snackbar.make(activity_managepatient, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,StartActivity.class));

        }
        if (item.getItemId() == R.id.menu_patient) {
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this, com.example.videocallingapp.ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_doctor) {
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ManagepatientActivity.this,ChatmessageActivity.class));
        }
        return true;
    }
    //end for three dot menu------------------------------
}
