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

public class ShowpatientActivity extends AppCompatActivity {
    EditText editText;
    //Button buttonSearch;
    private RecyclerView mRecyclerView;
    private PatientAdapter mFAdapter;
    //FirebaseRecyclerAdapter<Student,ViewHolder>mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Patient> patientList;
    FirebaseRecyclerOptions<Patient> options;
    RelativeLayout activity_showpatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpatient);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Patient List");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity_showpatient = (RelativeLayout) findViewById(R.id.activity_showpatient);
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
            public void afterTextChanged(Editable s)
            {
                mFAdapter.filterPatient(s.toString());//-------------------------calling filter method in PatientAdapter.java
                //mFAdapter.notifyDataSetChanged();
              }

        });
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientList=new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Patients");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Patient patient=postSnapshot.getValue(Patient.class);
                    patientList.add(patient);
                }
                mFAdapter= new PatientAdapter(com.example.videocallingapp.ShowpatientActivity.this,patientList);
                //mAdapter.startListening();
                mRecyclerView.setAdapter(mFAdapter);
                mFAdapter.notifyDataSetChanged(); // to refresh adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(com.example.videocallingapp.ShowpatientActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                //mFAdapter.filterPatient(s.toString());//-------------------------calling filter method in PatientAdapter.java
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())) {
                    mFAdapter.filterPatient(s.toString());//-------------------------calling filter method in PatientAdapter.java
                    // mFAdapter.notifyDataSetChanged();
                    //mFAdapter.getFilter().filter(s); //calling getFilter() method defined in FacultyAdapter but not using here
                }
                else
                {
                    patientList=new ArrayList<>();
                    mDatabaseRef= FirebaseDatabase.getInstance().getReference("Patients");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                                Patient patient=postSnapshot.getValue(Patient.class);
                                patientList.add(patient);
                            }
                            mFAdapter= new PatientAdapter(com.example.videocallingapp.ShowpatientActivity.this,patientList);
                            //mAdapter.startListening();
                            mRecyclerView.setAdapter(mFAdapter);
                            mFAdapter.notifyDataSetChanged(); // to refresh adapter
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(com.example.videocallingapp.ShowpatientActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                    return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    /*
    //search using SearchView
    private void searchPatient(String sss) {
        //String s=sss.toUpperCase(); //converting ss to capital letter
        //String s=sss.toLowerCase(); //converting ss to lower case letter
        String s=toTitleCaseSearchname(sss);; //converting ss to lower TitleCase
        Query query=mDatabaseRef.orderByChild("mName").startAt(s).endAt(s+"\uf8ff"); // starting with passing string s and ends with some characters.
        //Query query = mDatabaseRef.orderByChild("mName").equalTo(s + "\uf8ff");
        //Query query=mDatabaseRef.orderByChild("mName").equalTo(s);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    patientList.clear();
                    for(DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        final Patient patient=dss.getValue(Patient.class);
                        patientList.add(patient);
                    }
                    if(patientList.size()==0)
                    {
                        Toast.makeText(com.example.videocallingapp.ShowpatientActivity.this,"No record found...", Toast.LENGTH_LONG).show();
                    }
                    mFAdapter= new PatientAdapter(com.example.videocallingapp.ShowpatientActivity.this,patientList);
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
    //converting patient name to titlecase calling from above
    private static String toTitleCaseSearchname(String str) {
        if(str == null || str.isEmpty())
            return "";

        if(str.length() == 1)
            return str.toUpperCase();

        //split the string by space
        String[] parts = str.split(" ");

        StringBuilder sb = new StringBuilder( str.length() );

        for(String part : parts){

            if(part.length() > 1 )
                sb.append( part.substring(0, 1).toUpperCase() )
                        .append( part.substring(1).toLowerCase() );
            else
                sb.append(part.toUpperCase());

            sb.append(" ");
        }

        return sb.toString().trim();
    }
*/
    // end for searching record -------------------------------------
    // for other menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(activity_showpatient, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.ShowpatientActivity.this,StartActivity.class));

        }
        if (item.getItemId() == R.id.searchdoctor) {
           return true;
        }
        if (item.getItemId() == R.id.menu_patient) {
            startActivity(new Intent(com.example.videocallingapp.ShowpatientActivity.this,ManagepatientActivity.class));
        }
        if (item.getItemId() == R.id.menu_doctor) {
            startActivity(new Intent(com.example.videocallingapp.ShowpatientActivity.this,ManagedoctorActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            //startActivity(new Intent(ManagefacultyActivity.this,ManagevideoActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            startActivity(new Intent(com.example.videocallingapp.ShowpatientActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            startActivity(new Intent(com.example.videocallingapp.ShowpatientActivity.this,Chatmessage.class));
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

