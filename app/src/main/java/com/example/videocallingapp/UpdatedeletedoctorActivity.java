package com.example.videocallingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdatedeletedoctorActivity extends AppCompatActivity {
    final int PICK_IMAGE_REQUESTSS = 777;
    EditText editTextId,editTextName,editTextContactNo;
    Button buttonUpdateDoctor,buttonDeleteDoctor,buttonShowDoctors,button_choose_dnewimage,buttondCall;
    Spinner spinnersDepartment;
    ImageView mimage_viewnew;

    private Uri ImagenewdUrl;
    DatabaseReference mDatabaseRedupdatebutton,mDatabaseRedUpdateWithImage,mDatabaseRefDirectDelete,mDatabaseRefDeleteWithDialog;//1. mDatabaseRef is the name of the firebase database reference
    StorageReference mStorageRedUpdateWithImage;
    //StorageTask mUploadTask;
    ProgressDialog pddupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedeletedoctor);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Doctor Data Update");
        String key=getIntent().getExtras().get("dkey").toString();
        mDatabaseRedupdatebutton= FirebaseDatabase.getInstance().getReference().child("Doctors").child(key);
        mDatabaseRefDirectDelete = FirebaseDatabase.getInstance().getReference().child("Doctors").child(key);
        mDatabaseRefDeleteWithDialog = FirebaseDatabase.getInstance().getReference().child("Doctors").child(key);
 //----------------------------------------------------------------------------------------------------------------------
        mDatabaseRedUpdateWithImage= FirebaseDatabase.getInstance().getReference().child("Doctors");
        mStorageRedUpdateWithImage= FirebaseStorage.getInstance().getReference("Doctors");
        editTextName = (EditText) findViewById(R.id.editTextdName);
        editTextContactNo = (EditText) findViewById(R.id.editTextdContactNo);
        spinnersDepartment=(Spinner)findViewById(R.id.spinnerdDepartment);

        editTextId=(EditText)findViewById(R.id.editTextdId);
         buttonDeleteDoctor=(Button) findViewById(R.id.buttonDeleteDoctor);
        buttonUpdateDoctor=(Button) findViewById(R.id.buttonUpdateDoctor);
        button_choose_dnewimage=(Button) findViewById(R.id.button_choose_newdimage);
        buttondCall=(Button) findViewById(R.id.buttondCall);
        mimage_viewnew=(ImageView)findViewById(R.id.image_viewdnew);
        //init progress dialog
        pddupdate = new ProgressDialog(com.example.videocallingapp.UpdatedeletedoctorActivity.this);

        button_choose_dnewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimageFileChooser();
            }
        });
        String Tempdid=getIntent().getStringExtra("dkey");//here key is nothing but the artisteId

        //mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("students").child(Tempaid);//2. students is the name of the table to be created in firebase database created
        String Tempdname=getIntent().getStringExtra("dname");
        String Tempdcontactno=getIntent().getStringExtra("dcontactno");
        String Tempdimageurl=getIntent().getStringExtra("dimageurl");
        //editTextImageurl.setText(Tempimageurl);
        String Tempddept=getIntent().getStringExtra("ddept");
        String Tempdgender=getIntent().getStringExtra("dgender");

        //----------------------------------------------------------------------------
        setImage(Tempdimageurl);//calling the method setImage() below to display image
        //----------------------------------------------------------------------------
        editTextId.setText(Tempdid);
        editTextName.setText(Tempdname);
        editTextContactNo.setText(Tempdcontactno);
        //for getting the passing genre value to a spinner --------------------------------
        spinnersDepartment.setSelection(getIndex_SpinnerItem(spinnersDepartment,Tempddept));//defined method getIndex_SpinnerItem below
//----------------------------------------------------------------------------------
        RadioButton radmale=findViewById(R.id.radioMaledUpdate);
        RadioButton radfemale=findViewById(R.id.radioFemaledUpdate);
        if(Tempdgender.equals("Male"))
        {
            radmale.setChecked(true);
        }
        if(Tempdgender.equals("Female"))
        {
            radfemale.setChecked(true);
        }
        buttondCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.videocallingapp.UpdatedeletedoctorActivity.this,SendsmsActivity.class);
                intent.putExtra("acontactnotosms",editTextContactNo.getText().toString());
                startActivity(intent);
                //startActivity(new Intent(UpdatedeleteartistActivity.this,SendsmsActivity.class));
            }
        });
        //final String genderUpdate=radioButtonUpdate.getText().toString();
        //-----------------------------------------------------------
        buttonUpdateDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grpUpdate=(RadioGroup) findViewById(R.id.radioGroupdUpdate);
                int radioIdUpdate=grpUpdate.getCheckedRadioButtonId();
                final RadioButton radioButtonUpdate=findViewById(radioIdUpdate);
                //final String niurl=getNewImageUrlforupdation(ImagenewUrl);
                //UpdateArtist();
                mDatabaseRedupdatebutton.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("mName").setValue(editTextName.getText().toString());
                        dataSnapshot.getRef().child("mContactno").setValue(editTextContactNo.getText().toString());
                        dataSnapshot.getRef().child("mDepartment").setValue(spinnersDepartment.getSelectedItem().toString());
                        dataSnapshot.getRef().child("mGender").setValue(radioButtonUpdate.getText().toString());
                        //dataSnapshot.getRef().child("mImageUrl").setValue(niurl);
                        //dataSnapshot.getRef().child("artistGenre").setValue(spinnerGenre.getSelectedItem().toString());
                        Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this, "New record updated successfully!", Toast.LENGTH_LONG).show();
                        com.example.videocallingapp.UpdatedeletedoctorActivity.this.finish();
                        startActivity(new Intent(com.example.videocallingapp.UpdatedeletedoctorActivity.this,DoctorActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        buttonDeleteDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
deleteDoctor();
            }
        });
        /*
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdatedeletedoctorActivity.this,"Record Deleted Successfully...",Toast.LENGTH_LONG).show();
                            UpdatedeletedoctorActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(UpdatedeletedoctorActivity.this,"Record Not Deleted...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });*/
        buttonShowDoctors=(Button) findViewById(R.id.buttonShowDoctors);
        buttonShowDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.UpdatedeletedoctorActivity.this,DoctorActivity.class));
            }
        });



    }

    private void deleteDoctor() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseRefDeleteWithDialog.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this,"Record Deleted Successfully...", Toast.LENGTH_LONG).show();
                            com.example.videocallingapp.UpdatedeletedoctorActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this,"Record Not Deleted...", Toast.LENGTH_LONG).show();
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

    //display the image calling from above
    private void setImage(String tempimageurl) {

        //Picasso.with(this).load(tempimageurl)
        //for picasso 2.71828
        Picasso.get().load(tempimageurl)
                .fit()
                .centerCrop()
                //.centerInside()
                .into(mimage_viewnew);
    }

    // for uploading image file extension
    private String getFileExtension(Uri uri){ //calling from uploadFile method below
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void openimageFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent,PICK_IMAGE_REQUEST);
        startActivityForResult(intent,777);
    }//after writing code upto above code press CTRL + O then select the above onActivityResult method
    //loading selected image to the image_view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUESTSS && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            ImagenewdUrl= data.getData();
            Picasso.get().load(ImagenewdUrl).into(mimage_viewnew);
            updateDoctordata(ImagenewdUrl); //calling method uploadProfilePhoto() below
        }
    }

    private void updateDoctordata(Uri uri) {
        String key=getIntent().getExtras().get("dkey").toString();;//here key is nothing but the artisteId
        RadioGroup grdUpdate=(RadioGroup) findViewById(R.id.radioGroupdUpdate);//Update went wrong in this line here
        int radioIdUpdate=grdUpdate.getCheckedRadioButtonId();
        final RadioButton radioButtonUpdate=findViewById(radioIdUpdate);
        pddupdate.setMessage("Updating doctor's data...");
        //show progress dialog
        pddupdate.show();
        mStorageRedUpdateWithImage.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// image is uploaded to storage and store to the user's database
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri=uriTask.getResult();
                        //check if image is uploaded or not and url is received
                        if(uriTask.isSuccessful()){
                            //image uploaded and add/update url in user's database
                            HashMap<String, Object> hashMap=new HashMap<>();
                            //results.put(profilePhoto,downloadUri.toString());//here profilePhoto="image" initialized above
                            //alternative code of the above line
                            String doctName=editTextName.getText().toString();
                            String doctContactno=editTextContactNo.getText().toString();
                            String doctordept=spinnersDepartment.getSelectedItem().toString();
                            //String patNoopatvisits=spinnerNoopatvisits.getSelectedItem().toString();
                            String patGender=radioButtonUpdate.getText().toString();

                            hashMap.put("mName",""+doctName);// here mName is field name in Patient Table created
                            hashMap.put("mContactno",""+doctContactno);
                            hashMap.put("mDepartment",""+doctordept);
                            //hashMap.put("mNoofvisit",""+patNoopatvisits);
                            hashMap.put("mGender",""+patGender);
                            hashMap.put("mImageUrl",""+downloadUri);// here mImageUrl is field name in Patient Table created

                            mDatabaseRedUpdateWithImage.child(key).updateChildren(hashMap)//Update pfrofile photo==========================
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //dismiss progress bar
                                            pddupdate.dismiss();
                                            Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this, "Doctor data updated.",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(com.example.videocallingapp.UpdatedeletedoctorActivity.this,ShowpatientActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //dismiss progress bar
                                    pddupdate.dismiss();
                                    Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this, "Profile photo updating failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            pddupdate.dismiss();
                            Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// if there is some error
                        pddupdate.dismiss();
                        Toast.makeText(com.example.videocallingapp.UpdatedeletedoctorActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
/*
    private String getNewImageUrlforupdation(Uri imagenewUrl) {

            //StorageReference fileReference=mStorageRef.child("uploads/"+ System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            final StorageReference fileReference=mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imagenewUrl)); //calling getFileExtensione method above

            return  fileReference.getDownloadUrl().toString();

    }*/

    //searching the passed course from the list of spinner
    private int getIndex_SpinnerItem(Spinner spinner, String item) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(item)) {
                index = i;
                break;
            }
        }
        return index;
    }
//-------------------------------------------------------------------------------------------
//Getting current userinfo and restriction to the user for accessing
@Override
protected void onStart() {
    getCurrentUserInfo();//calling method getCurrentUserInfo defined below
    super.onStart();
}
    //Getting current userinfo and restriction to the user for accessing
    private void getCurrentUserInfo() {
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        if (user != null && !(email.equals("admin@gmail.com"))) {

            //edit_text_file_fname.setEnabled(false);
            //editTextfAge.setEnabled(false);
            //editTextfContactno.setEnabled(false);
            //editTextfDoj.setEnabled(false);
            //editTextfAddress.setEnabled(false);

            buttonUpdateDoctor.setEnabled(false);
            buttonDeleteDoctor.setEnabled(false);
            //button_choose_fimage.setEnabled(false);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
        if (user != null && (email.equals("admin@gmail.com"))) {

            //edit_text_file_fname.setEnabled(false);
            //editTextfAge.setEnabled(false);
            //editTextfContactno.setEnabled(false);
            //editTextfDoj.setEnabled(false);
            //editTextfAddress.setEnabled(false);

            buttonUpdateDoctor.setEnabled(true);
            buttonDeleteDoctor.setEnabled(true);
            //button_choose_fimage.setEnabled(false);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
    }
}

