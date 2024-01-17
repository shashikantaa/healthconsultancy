package com.example.videocallingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    EditText memailId,mpassword;
    Button mbtnSignUp;
    TextView mtvSignIn;
    //FirebaseAuth mFirebaseAuth;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("New User Registration");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //mFirebaseAuth=FirebaseAuth.getInstance();
        memailId=(EditText)findViewById(R.id.editTextemail);
        mpassword=(EditText)findViewById(R.id.editTextpwd);
        mbtnSignUp=(Button) findViewById(R.id.buttonSignUp);
        mtvSignIn=(TextView)findViewById(R.id.textViewSignin);
        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");
        mbtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uemail=memailId.getText().toString().trim();
                String upwd=mpassword.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(uemail).matches()){
                    memailId.setError("Invalid Email !");
                    memailId.setFocusable(true);
                }
                else if(upwd.length()<6)
                {
                    mpassword.setError("Password length should be at least 6 characters");
                    mpassword.setFocusable(true);
                }
                else {
                    registerUser(uemail,upwd);//calling registerUser method defined below
                }
            }
        });
        mtvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }
    //registerUser method starts here --------calling from above
    private void registerUser(String uemail, String upwd) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(uemail, upwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //getting user email and uid from auth
                            String email=user.getEmail();
                            String uid=user.getUid();
                            //storing user info to firebase realtime database using HashMap
                            //HashMap<Object,String> hashMap=new HashMap<>();
                            HashMap<Object,String> result=new HashMap<>();
                            //put info to hashmap
                            result.put("email",email);
                            result.put("uid",uid);
                            result.put("name","");
                            result.put("phone","");
                            result.put("image","");
                            //firebase database instance
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            //Firebase path to store user data named as "Users"
                            DatabaseReference reference=database.getReference("Users");
                            //put data within hashmap
                            reference.child(uid).setValue(result);
                            //ends storing user info to firebase realtime database using HashMap after registration
                            Toast.makeText(RegistrationActivity.this, "Registered...\n"+user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            //finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Registration failed, try again...",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
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