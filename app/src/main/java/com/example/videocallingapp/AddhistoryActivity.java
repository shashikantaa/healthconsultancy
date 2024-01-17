package com.example.videocallingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddhistoryActivity extends AppCompatActivity {
    private EditText edtPatientname,edtTest,edtDateoftest,edtTestresult,edtOthercases;
    private Button btnAddHistory,btnHistorylist,btnUpdateHistory;
    private DatabaseReference mHistoryDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addhistory);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add History of Patient");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mHistoryDatabaseRef= FirebaseDatabase.getInstance().getReference("Histories");
        edtPatientname=(EditText)findViewById(R.id.edtPatientname);
        edtTest=(EditText)findViewById(R.id.edtTest);
        edtDateoftest=(EditText)findViewById(R.id.edtDateoftest);
        edtTestresult=(EditText)findViewById(R.id.edtTestresult);
        edtOthercases=(EditText)findViewById(R.id.edtOthercases);
        btnAddHistory=(Button) findViewById(R.id.btnAddHistory);
        btnHistorylist=(Button)findViewById(R.id.btnHistorylist);
        btnUpdateHistory=(Button)findViewById(R.id.btnUpdateHistory);
        //update history starts---------------------------------------------------
        btnUpdateHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                //Firebase path to store user data named as "Users"
                DatabaseReference reference=database.getReference("Histories");
                //getting user email and uid from auth
                String Temphpid = getIntent().getStringExtra("hpkey");//here key is nothing but the artisteId
                String Temphpname = getIntent().getStringExtra("hpname");
                edtPatientname.setText(Temphpname);

                Calendar cal= Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dotesttime=sdf.format(cal.getTime());
                edtDateoftest.setText(dotesttime);
                HashMap<Object, String> result=new HashMap<>();
                //put info to hashmap
                result.put("mHistoryid",Temphpid);
                result.put("mPatientname",Temphpname);
                result.put("mTest",edtTest.getText().toString());
                result.put("mDateoftest",dotesttime);
                result.put("mTestresult",edtTestresult.getText().toString());
                result.put("mOthercases",edtOthercases.getText().toString());
                //firebase database instance

                //put data within hashmap
                reference.child(Temphpid).setValue(result);
                //ends storing user info to firebase realtime database using HashMap after registration
                Toast.makeText(com.example.videocallingapp.AddhistoryActivity.this, "History Updated Sucessfully!",
                        Toast.LENGTH_SHORT).show();
                edtPatientname.setText("");
                edtTest.setText("");
                edtDateoftest.setText("");
                edtTestresult.setText("");
                edtOthercases.setText("");

            }
        });
        //ends update history ------------------------------------------------------
        //Add history starts-----------------------------------------------
        String Temphpid = getIntent().getStringExtra("hpkey");//here key is nothing but the artisteId
        String Temphpname = getIntent().getStringExtra("hpname");
        edtPatientname.setText(Temphpname);

        Calendar cal= Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dotesttime=sdf.format(cal.getTime());
        edtDateoftest.setText(dotesttime);

        btnAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String historyid=mHistoryDatabaseRef.push().getKey();
                //History history=new History(Temphpid,edtPatientname.getText().toString(), edtTest.getText().toString(),edtDateoftest.getText().toString(),edtTestresult.getText().toString(),edtOthercases.getText().toString());
                History history=new History(Temphpid,edtPatientname.getText().toString(), edtTest.getText().toString(),dotesttime,edtTestresult.getText().toString(),edtOthercases.getText().toString());
                mHistoryDatabaseRef.child(historyid).setValue(history).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(com.example.videocallingapp.AddhistoryActivity.this,"History added successfully !", Toast.LENGTH_LONG).show();
                        edtPatientname.setText("");
                        edtTest.setText("");
                        edtDateoftest.setText("");
                        edtTestresult.setText("");
                        edtOthercases.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(com.example.videocallingapp.AddhistoryActivity.this,"History addition failed !", Toast.LENGTH_SHORT).show();
                        Toast.makeText(com.example.videocallingapp.AddhistoryActivity.this, ""+e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        //Add history ends-----------------------------------------------
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
