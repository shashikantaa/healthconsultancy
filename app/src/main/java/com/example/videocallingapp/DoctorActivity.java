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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {
    EditText editText;
    //Button buttonSearch;
    private RecyclerView mRecyclerView;
    private DoctorAdapter mFAdapter;
    //FirebaseRecyclerAdapter<Student,ViewHolder>mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Doctor> doctorList;
    FirebaseRecyclerOptions<Doctor> options;
    RelativeLayout activity_doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Doctor List");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity_doctor = (RelativeLayout) findViewById(R.id.activity_doctor);
        editText=(EditText)findViewById(R.id.editText);
        // search automatically just after typing text in the editText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //mFAdapter.filter(s.toString());//-------------------------calling filter method in PatientAdapter.java
                if(!TextUtils.isEmpty(s.toString().trim())) {
                    mFAdapter.filterDoctor(s.toString());//-------------------------calling filter method in PatientAdapter.java
                }
                else{
                    doctorList=new ArrayList<>();
                    mDatabaseRef= FirebaseDatabase.getInstance().getReference("Doctors");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                Doctor faculty=postSnapshot.getValue(Doctor.class);
                                doctorList.add(faculty);
                            }
                            mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                            //mAdapter.startListening();
                            mRecyclerView.setAdapter(mFAdapter);
                            mFAdapter.notifyDataSetChanged(); // to refresh adapter
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(com.example.videocallingapp.DoctorActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //mFAdapter.getFilter().filter(s); //calling getFilter() method defined in FacultyAdapter but not using here
                //return false;
            }

        });
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorList=new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Doctors");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Doctor faculty=postSnapshot.getValue(Doctor.class);
                    doctorList.add(faculty);
                }
                mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                //mAdapter.startListening();
                mRecyclerView.setAdapter(mFAdapter);
                mFAdapter.notifyDataSetChanged(); // to refresh adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(com.example.videocallingapp.DoctorActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    // for searching record using EditText-------------------------------------
    private void search(String ss) {
        String s1=ss.toLowerCase();
        char c=s1.charAt(0);
        String es=new String(""+c);
        String fc=es.toUpperCase();
        String s=fc+s1.substring(1);
        //String s=ss.toUpperCase(); //converting ss to capital letter
        Query query=mDatabaseRef.orderByChild("mName").startAt(s).endAt(s+"\uf8ff"); // starting with passing string s and ends with some characters.
        //Query query = mDatabaseRef.orderByChild("mName").equalTo(s + "\uf8ff");
        //Query query=mDatabaseRef.orderByChild("mName").equalTo(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    doctorList.clear();
                    for(DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        final Doctor faculty=dss.getValue(Doctor.class);
                        doctorList.add(faculty);
                    }
                    if(doctorList.size()==0)
                    {
                        Toast.makeText(com.example.videocallingapp.DoctorActivity.this,"No record found...", Toast.LENGTH_LONG).show();
                    }
                    mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                    //mAdapter.startListening();
                    mRecyclerView.setAdapter(mFAdapter);
                    mFAdapter.notifyDataSetChanged(); // to refresh adapter

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // end for searching record -------------------------------------
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_doctor,menu);
        MenuItem item=menu.findItem(R.id.searchdoctor);
        //very important SearchView object below should be of SearchView(androidx.appcompat.widget) as mentioned as exactly as in search_menu.xml
        //if you take other SearchView(android.widget) then runtime error will occur
        //SearchView searchView=(SearchView) menuItem.getActionView();
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())) {
                    //mFAdapter.filterDoctor(s);//-------------------------calling filter method in PatientAdapter.java
                }
                /*
                else{
                    doctorList=new ArrayList<>();
                    mDatabaseRef= FirebaseDatabase.getInstance().getReference("Doctors");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                Doctor faculty=postSnapshot.getValue(Doctor.class);
                                doctorList.add(faculty);
                            }
                            mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                            //mAdapter.startListening();
                            mRecyclerView.setAdapter(mFAdapter);
                            mFAdapter.notifyDataSetChanged(); // to refresh adapter
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(com.example.videocallingapp.DoctorActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())) {
                    mFAdapter.filterDoctor(s);//-------------------------calling filter method in PatientAdapter.java
                }
                else{
                    doctorList=new ArrayList<>();
                    mDatabaseRef= FirebaseDatabase.getInstance().getReference("Doctors");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                Doctor faculty=postSnapshot.getValue(Doctor.class);
                                doctorList.add(faculty);
                            }
                            mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                            //mAdapter.startListening();
                            mRecyclerView.setAdapter(mFAdapter);
                            mFAdapter.notifyDataSetChanged(); // to refresh adapter
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(com.example.videocallingapp.DoctorActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //mFAdapter.getFilter().filter(s); //calling getFilter() method defined in FacultyAdapter but not using here
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    /*
    //search using SearchView
    private void searchFaculty(String sss) {
        String s=sss.toUpperCase(); //converting ss to capital letter
        Query query=mDatabaseRef.orderByChild("mName").startAt(s).endAt(s+"\uf8ff"); // starting with passing string s and ends with some characters.
        //Query query = mDatabaseRef.orderByChild("mName").equalTo(s + "\uf8ff");
        //Query query=mDatabaseRef.orderByChild("mName").equalTo(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    doctorList.clear();
                    for(DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        final Doctor faculty=dss.getValue(Doctor.class);
                        doctorList.add(faculty);
                    }
                    if(doctorList.size()==0)
                    {
                        Toast.makeText(com.example.videocallingapp.DoctorActivity.this,"No record found...", Toast.LENGTH_LONG).show();
                    }
                    mFAdapter= new DoctorAdapter(com.example.videocallingapp.DoctorActivity.this,doctorList);
                    //mAdapter.startListening();
                    mRecyclerView.setAdapter(mFAdapter);
                    mFAdapter.notifyDataSetChanged(); // to refresh adapter
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // end for searching record -------------------------------------
         */
    // for other menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(activity_doctor, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.DoctorActivity.this,StartActivity.class));

        }
        if (item.getItemId() == R.id.searchdoctor) {
            return true;
        }
        if (item.getItemId() == R.id.menu_patient) {
            startActivity(new Intent(com.example.videocallingapp.DoctorActivity.this,ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_doctor) {
            startActivity(new Intent(com.example.videocallingapp.DoctorActivity.this,ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            //startActivity(new Intent(ManagefacultyActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.DoctorActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.DoctorActivity.this,ChatmessageActivity.class));
        }
        return true;
    }
    //ends for other menu items
    // registerUser method ends here
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();

        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
