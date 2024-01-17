package com.example.videocallingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText secretCodeBox;
    Button joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Video Call");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        secretCodeBox=findViewById(R.id.secretCodeBox);
        joinBtn=findViewById(R.id.joinBtn);

        URL serverURL;
        try{
            serverURL=new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                        .setRoom(secretCodeBox.getText().toString()).setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(MainActivity.this,options);
            }
        });
    }
    //Getting current userinfo and restriction to the user for accessing
    @Override
    protected void onStart() {
        //getCurrentUserInfo();//calling method getCurrentUserInfo defined below
        super.onStart();
    }
}