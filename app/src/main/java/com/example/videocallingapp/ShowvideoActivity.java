package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShowvideoActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    LinearLayout activity_showvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showvideo);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Video List");
        activity_showvideo = (LinearLayout) findViewById(R.id.activity_showvideo);
        recyclerView=findViewById(R.id.recyclerview_ShowVideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Videos");//table name created in Firebase database
    }
    //start method definition for search firebaseSearch called from onCreateOptionsMenu(Menu menu) below
    private void firebaseSearch(String searchtext)
    {
        String query=searchtext.toLowerCase();
        Query firebaseQuery=databaseReference.orderByChild("videoName").startAt(query).endAt(query + "\uf8ff");
        FirebaseRecyclerOptions<Video> options =
                new FirebaseRecyclerOptions.Builder<Video>()
                        .setQuery(firebaseQuery,Video.class)
                        .build();
        FirebaseRecyclerAdapter<Video,VideoAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Video, VideoAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoAdapter holder, int position, @NonNull Video video) {
                        holder.setExoplayer(getApplication(),video.getVideoName(),video.getVideoUrl());

                    }
                    @NonNull
                    @Override
                    public VideoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_layout_video,parent,false);
                        return new VideoAdapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    //end method for search
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Video> options =
                new FirebaseRecyclerOptions.Builder<Video>()
                        .setQuery(databaseReference,Video.class)
                        .build();
        FirebaseRecyclerAdapter<Video,VideoAdapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Video, VideoAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoAdapter holder, int position, @NonNull Video video) {
                        holder.setExoplayer(getApplication(),video.getVideoName(),video.getVideoUrl());

                    }

                    @NonNull
                    @Override
                    public VideoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_layout_video,parent,false);
                        return new VideoAdapter(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    //for searching on Action Bar described on menu search_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.search_firebase);
        //very important SearchView object below should be of SearchView(androidx.appcompat.widget) as mentioned as exactly as in search_menu.xml
        //if you take other SearchView(android.widget) then runtime error will occur
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);//calling the firebaseSearch() method defined above passing parameter
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);//calling the firebaseSearch() method defined above passing parameter
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //end for searching on Action Bar described on menu search_menu.xml
    // for other menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_showvideo, "You have been signed out!", Snackbar.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,StartActivity.class));

                }
            });
        }
        if (item.getItemId() == R.id.menu_student) {
            startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_faculty) {
            startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ShowvideoActivity.this,ChatmessageActivity.class));
        }
        return true;
    }
    //ends for other menu items
}

