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

public class LoginActivity extends AppCompatActivity {
    EditText emailId,password;
    Button buttonLogIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog loginprogressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("User Log In");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        mFirebaseAuth=FirebaseAuth.getInstance();
        //initialize progress dialog
        loginprogressDialog=new ProgressDialog(this);
        loginprogressDialog.setMessage("Logging in user...");
        //Views initializations
        emailId=(EditText)findViewById(R.id.editTextem);
        password=(EditText)findViewById(R.id.editTextpassword);
        buttonLogIn=(Button) findViewById(R.id.buttonLogIn);
        tvSignUp=(TextView)findViewById(R.id.textViewSignup);
        /*mAuthStateListener=new mFirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    Toast.makeText(LoginActivity.this, "You are signed in !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "Signed in failed, Try again!", Toast.LENGTH_LONG).show();
                }
            }
        };*/
        //Log in button click
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailId.getText().toString().trim();
                String pwd=password.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailId.setError("Invalid Email !");
                    emailId.setFocusable(true);
                }
                else if(pwd.length()<6)
                {
                    password.setError("Password length should be at least 6 characters");
                    password.setFocusable(true);
                }
                else {
                    loginUser(email,pwd);//calling loginUser method defined below
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });
    }

    private void loginUser(String email, String pwd) {
        loginprogressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            loginprogressDialog.dismiss();
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            //For simple login the following is not required
                            //creating Users and adding values using HashMap
                            //getting user email and uid from auth
                            String email=user.getEmail();
                            String uid=user.getUid();
                            /*
                            //storing user info to firebase realtime database using HashMap
                            HashMap<Object,String> hashMap=new HashMap<>();
                            //put info to hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            //firebase database instance
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            //Firebase path to store user data named as "Users"
                            DatabaseReference reference=database.getReference("Users");
                            //put data within hashmap
                            reference.child(uid).setValue(hashMap);
                            //ends creating Users and adding values using HashMap after login
                            */
                            Toast.makeText(LoginActivity.this, "Login Successful !.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                            //startActivity(new Intent(LoginActivity.this,VideocallActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            loginprogressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //if error shows error message
                loginprogressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        //Check if user is signed in or not on the start of App
        //getCurrentUserInformation();//calling getCurrentUserInformation() method defined below
        super.onStart();
    }
}
