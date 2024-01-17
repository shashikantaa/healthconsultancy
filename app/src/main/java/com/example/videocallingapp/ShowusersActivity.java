package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowusersActivity extends AppCompatActivity {
    EditText editTextuser;
    DatabaseReference mUserDatabaseRef;
    ListView listViewUsers;
    ListViewUserAdapter listViewUserAdapter;
    //UserAdapter userAdapter;
    List<Users> userList;
    RelativeLayout activity_showusers;
    //ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showusers);
        mUserDatabaseRef= FirebaseDatabase.getInstance().getReference("Users");
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("ShowusersActivity");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        editTextuser=(EditText) findViewById(R.id.editTextuser);
        activity_showusers = (RelativeLayout) findViewById(R.id.activity_showusers);
        listViewUsers=(ListView)findViewById(R.id.listViewUsers);
        //for display users records when activity starts
        userList=new ArrayList<Users>();
        mUserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                    Users users=userSnapshot.getValue(Users.class);
                    userList.add(users);
                }

                // arrayList.addAll(userList);
                //calling UserAdapter class
                listViewUserAdapter=new ListViewUserAdapter(ShowusersActivity.this,userList);
                listViewUsers.setAdapter(listViewUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //----------------------------------------------------------------------
        //for searching users in editText
        editTextuser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                {
                    //search(s.toString()); //calling search method defined below
                    //search(s.toString()); //calling search method defined below
                    listViewUserAdapter.filterUsers(s.toString());//calls filterUsers method defined below inside the ListViewUserAdapter.java class
                }
                else
                {
                    userList=new ArrayList<Users>();
                    //for display company records
                    mUserDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                                Users users=userSnapshot.getValue(Users.class);
                                userList.add(users);
                            }

                            // arrayList.addAll(userList);
                            //calling UserAdapter class
                            listViewUserAdapter=new ListViewUserAdapter(ShowusersActivity.this,userList);
                            listViewUsers.setAdapter(listViewUserAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        //-------------------------------------------------------------------------
    }
    //Start for search using SearchView -------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_doctor,menu);
        MenuItem item=menu.findItem(R.id.searchdoctor);
        //very important SearchView object below should be of SearchView(androidx.appcompat.widget.SearchView) as mentioned as exactly as in search_item.xml under menu
        //if you take other SearchView(android.widget.SearchView) then runtime error will occur
        //SearchView searchView=(SearchView) menuItem.getActionView();
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)) {
                    listViewUserAdapter.filterUsers(s);//calls filterUsers method defined below inside the ListViewUserAdapter.java class
                }
                else{
                    userList=new ArrayList<Users>();
                    //for display company records
                    mUserDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                                Users users=userSnapshot.getValue(Users.class);
                                userList.add(users);
                            }
                            //calling ListViewUserAdapter.class
                            listViewUserAdapter=new ListViewUserAdapter(ShowusersActivity.this,userList);
                            listViewUsers.setAdapter(listViewUserAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                return true;
            }
        });
        //return super.onCreateOptionsMenu(menu);
        return true;
    }
    // end for searching record -------------------------------------
    // for other menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(activity_showusers, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.ShowusersActivity.this,StartActivity.class));

        }
        if (item.getItemId() == R.id.searchdoctor) {
            return true;
        }
        if (item.getItemId() == R.id.menu_patient) {
            startActivity(new Intent(com.example.videocallingapp.ShowusersActivity.this,ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_doctor) {
            startActivity(new Intent(com.example.videocallingapp.ShowusersActivity.this,ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            //startActivity(new Intent(ManagefacultyActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.ShowusersActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ShowusersActivity.this,Chatmessage.class));
        }
        return true;
    }
    //ends for other menu items
}
