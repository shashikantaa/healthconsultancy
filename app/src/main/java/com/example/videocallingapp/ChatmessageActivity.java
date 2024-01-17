package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatmessageActivity extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;
    private DatabaseReference mChatmessageRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseUsersReference;
    //private FirebaseListAdapter<ChatMessage> adapter;
    ConstraintLayout activity_chatmessage;
    RelativeLayout activity_list_layout_chatmessage;
    ImageButton fabchatImageButton;//floating action button
    ListView listchatmessage;
    List<Chatmessage> chatmessageList;
    //ImageView userIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmessage);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Public Chat Room");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity_chatmessage = (ConstraintLayout) findViewById(R.id.activity_chatmessage);
        //activity_list_layout_chatmessage = (RelativeLayout) findViewById(R.id.activity_list_layout_chatmessage);
        //userIv=(ImageView)findViewById(R.id.userIv);
        listchatmessage = (ListView) findViewById(R.id.listchatmessage);
        chatmessageList=new ArrayList<>();
        mChatmessageRef= FirebaseDatabase.getInstance().getReference("Chatmessages");
        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseUsersReference = firebaseDatabase.getReference("Users");
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fabchatImageButton = (ImageButton) findViewById(R.id.fabchatImageButton);
        fabchatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText inputchatmessage = (EditText) findViewById(R.id.inputchatmessage);

                Calendar cal= Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String chattime=sdf.format(cal.getTime());
                String messageid=mChatmessageRef.push().getKey();
                Chatmessage chatmessag=new Chatmessage(messageid,inputchatmessage.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),chattime);
                mChatmessageRef.child(messageid).setValue(chatmessag);
                Toast.makeText(com.example.videocallingapp.ChatmessageActivity.this,"Message sent !", Toast.LENGTH_LONG).show();
                inputchatmessage.setText("");
            }
        });
        //--checking at the start of the activity whether signed in or not
        //check if not signed in then navigate Sign in page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_chatmessage, "Welcome:" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).show();
        }
    }
    // signed in checking finish
    @Override
    protected void onStart() {
        //Check if user is signed in or not on the start of App
        FirebaseUser fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onStart();
        mChatmessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatmessageList.clear();
                for (DataSnapshot chatmessageSnapshot : dataSnapshot.getChildren()) {
                    Chatmessage chatmessage = chatmessageSnapshot.getValue(Chatmessage.class);
                    chatmessageList.add(chatmessage);
                }
                ChatmessageAdapter chatmessageadapter = new ChatmessageAdapter(com.example.videocallingapp.ChatmessageActivity.this, chatmessageList);
                listchatmessage.setAdapter(chatmessageadapter);
                chatmessageadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //getCurrentUserInformation();//calling getCurrentUserInformation() method defined below

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
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chatmessage, "You have been signed out!", Snackbar.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.example.videocallingapp.ChatmessageActivity.this,StartActivity.class));
                }
            });
        }
        if (item.getItemId() == R.id.menu_student) {
            //startActivity(new Intent(ChatroomActivity.this,AddstudentActivity.class));
        }
        if (item.getItemId() == R.id.menu_faculty) {
            //startActivity(new Intent(ChatroomActivity.this,UploadimageActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            //startActivity(new Intent(ChatroomActivity.this,UploadimageActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            //startActivity(new Intent(ChatroomActivity.this,AddhistoryActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ChatmessageActivity.this,ChatmessageActivity.class));
        }
        return true;
    }
    //end for three dot menu------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_chatmessage, "Successfully Signed in, Welcome to Chat Room!", Snackbar.LENGTH_LONG).show();
                //displayChatMessage();
            } else {
                Snackbar.make(activity_chatmessage, "Sign In Failed, Try again later !", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
