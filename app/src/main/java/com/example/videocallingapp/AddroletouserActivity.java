package com.example.videocallingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddroletouserActivity extends AppCompatActivity {

    ImageView user_profile_imageinrole;
    EditText editTextuser_roleinrole,editTextuser_curroleinrole;
    Button buttonDeleteUserinrole;
    DatabaseReference mDatabaseRefUserUpdateButton,mDatabaseRefUserDeleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addroletouser);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Assign Role to User");
        //-----------------------------------------------------------------------
        buttonDeleteUserinrole=(Button)findViewById(R.id.buttonDeleteUserinrole);

        TextView user_idinrole = (TextView) findViewById(R.id.user_profile_idinrole);
        TextView user_nameinrole = (TextView) findViewById(R.id.user_profile_nameinrole);
        TextView user_Phoneinrole = (TextView) findViewById(R.id.user_phoneinrole);
        TextView user_Emailinrole = (TextView) findViewById(R.id.user_emailinrole);
        user_profile_imageinrole = (ImageView) findViewById(R.id.user_profile_imageinrole);
        //----------------------------------------------------------------------------------------

        String Tempuid = getIntent().getStringExtra("userId");//here key is nothing but the artisteId
        String Tempuname = getIntent().getStringExtra("userName");
        String Tempuphoneno = getIntent().getStringExtra("userPhone");
        String TempuEmail = getIntent().getStringExtra("userEmail");

        //String TempCurUserRole = getIntent().getStringExtra("curUserRole");

        String Tempuimageurl = getIntent().getStringExtra("userimageUrl");
        //------------------------------------------------------------------------
        //if (Tempuimageurl != null)
        // {
        user_idinrole.setText(Tempuid);
        user_nameinrole.setText(Tempuname);
        user_Phoneinrole.setText(Tempuphoneno);
        user_Emailinrole.setText(TempuEmail);

        //editTextuser_curroleinrole.setText(TempCurUserRole);
        //----------------------------------------------------------------------------
        setUserImage(Tempuimageurl);//calling the method setImage() below to display image
        //----------------------------------------------------------------------------
          buttonDeleteUserinrole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                if(user.getEmail().equals(TempuEmail) && user.getEmail().equals("admin@gmail.com"))
                {
                    Toast.makeText(AddroletouserActivity.this,"Sorry Admin user can't be deleted...",Toast.LENGTH_LONG).show();

                }
                else if(user.getEmail().equals(TempuEmail) )
                {
                    deleteCurrentUser();
                }
                else {
                    Toast.makeText(AddroletouserActivity.this,"Sorry you are not authorised to delete this user...",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void deleteCurrentUser() {
        String Tempuid = getIntent().getStringExtra("userId");//here key is nothing but the artisteId
        mDatabaseRefUserDeleteButton = FirebaseDatabase.getInstance().getReference().child("Users").child(Tempuid);
        AlertDialog.Builder builder=new AlertDialog.Builder(AddroletouserActivity.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseRefUserDeleteButton.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddroletouserActivity.this,"User Record Deleted Successfully...",Toast.LENGTH_LONG).show();
                            AddroletouserActivity.this.finish();
                            startActivity(new Intent(AddroletouserActivity.this,UsersActivity.class));
                        }
                        else
                        {
                            Toast.makeText(AddroletouserActivity.this,"User Record Not Deleted...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
/* not used method below--------------------------------------------------------
    //Assign role Teacher to user
    private void assignUserToRole() {

        String Tempuid = getIntent().getStringExtra("userId");//here key is nothing but the artisteId

        mDatabaseRefUserUpdateButton = FirebaseDatabase.getInstance().getReference().child("Users").child(Tempuid);

        //------------------------------------------------------------------------------
        //input and check current userRole is Admin or not
        //init firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //user=firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get data
                        //String name = "" + ds.child("name").getValue();
                        //String email = "" + ds.child("email").getValue();
                        //String phone = "" + ds.child("phone").getValue();
                        //String imagevalue = "" + ds.child("image").getValue();
                        String uRole = "" + ds.child("userRole").getValue();
                        Toast.makeText(AddroletouserActivity.this, "User Role:"+uRole.toString(),
                                Toast.LENGTH_LONG).show();
                        if(uRole.equals("Teacher"))
                        //if((5>4) && (7>9))
                        //if(imagevalue!=null)
                        {
                            editTextuser_roleinrole.setText("Admin");


                        }else {
                            editTextuser_roleinrole.setText("Teacher");
                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //if image is not available set default image
                    //Picasso.get().load(R.drawable.ic_add_photo).into(avatarTv);
                }

            });
        } else
        {
            Toast.makeText(AddroletouserActivity.this, "User not authorized to proceed!.",
                    Toast.LENGTH_SHORT).show();
        }

//----------------------------------------------------------------------------

        //String userNewRole;
        String crole=editTextuser_curroleinrole.getText().toString();
        if(crole.equals("Admin")) {
            editTextuser_roleinrole.setText("Admin");
        }
        else {
            editTextuser_roleinrole.setText("Teacher");
        }

        mDatabaseRefUserUpdateButton.addListenerForSingleValueEvent(new ValueEventListener() {
            String userNewRole=editTextuser_roleinrole.getText().toString();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("userRole").setValue(userNewRole);
                Toast.makeText(AddroletouserActivity.this, "Role assigned to user successfully!", Toast.LENGTH_LONG).show();
                AddroletouserActivity.this.finish();
                startActivity(new Intent(AddroletouserActivity.this, UsersActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Sorry Role not assigned to user!",Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    private void setUserImage(String tempuimageurl) {
        Picasso.get().load(tempuimageurl)
                .fit()
                .centerCrop()
                //.centerInside()
                .into(user_profile_imageinrole);
    }

    @Override
    protected void onStart() {
        // checkForUserImageProfile();
        super.onStart();
    }
/*
    private void checkForUserImageProfile() {
        String Tempuimageurl = getIntent().getStringExtra("userimageUrl");
        //------------------------------------------------------------------------
        if (Tempuimageurl == null)
        {
        //user_idinrole.setText(Tempuid);
        //user_nameinrole.setText(Tempuname);
        //user_Phoneinrole.setText(Tempuphoneno);
        //user_Emailinrole.setText(TempuEmail);

        //editTextuser_curroleinrole.setText(TempCurUserRole);
            Toast.makeText(AddroletouserActivity.this, "Update User profile Image!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddroletouserActivity.this,ShowuserprofileActivity.class));
            //----------------------------------------------------------------------------
        setUserImage(Tempuimageurl);//calling the method setImage() below to display image
        //----------------------------------------------------------------------------
        }
        else {
            startActivity(new Intent(AddroletouserActivity.this,AddroletouserActivity.class));
         }
    }*/
}
