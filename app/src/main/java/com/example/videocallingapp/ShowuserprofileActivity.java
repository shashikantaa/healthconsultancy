package com.example.videocallingapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ShowuserprofileActivity extends AppCompatActivity {
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Storage
    StorageReference storageReference;
    //path where images of user profile will be stored
    String storagePath="Users_Profile_Imgs/";
    //Views fro xml
    ImageView avatarTv;
    TextView nameTv, emailTv, phoneTv;
    //Button
    Button buttonPassrordReset;
    //floating action bar
    FloatingActionButton fab;
    ProgressDialog pd;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];
    //Uri of pick image
    Uri image_uri;
    //for checking profilePhoto
    String profilePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showuserprofile);
        //action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User Profile");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference=getInstance().getReference();//firebase storage reference
        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //init views
        avatarTv = (ImageView) findViewById(R.id.avatarIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
        phoneTv = (TextView) findViewById(R.id.phoneTv);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //For reset password
        buttonPassrordReset=(Button) findViewById(R.id.buttonPassrordReset);

        buttonPassrordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword=new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter new password>6 characters long");
            passwordResetDialog.setView(resetPassword);
            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   String newPassword=resetPassword.getText().toString();

                   user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(getApplicationContext(), "Password changed successfully!",
                                   Toast.LENGTH_SHORT).show();
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getApplicationContext(), "Password changed failed!",
                                   Toast.LENGTH_SHORT).show();
                       }
                   });
                }
            });

                //add button to cancel
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //create and show dialog
                passwordResetDialog.create().show();
            }
        });
        //end for reset password----------------------------
        //init progress dialog
        pd = new ProgressDialog(com.example.videocallingapp.ShowuserprofileActivity.this);
        //pd = new ProgressDialog(getApplication());
        //fab floating action button onclick
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();//calling method showEditProfileDialog()=================================
            }
        });
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(com.example.videocallingapp.ShowuserprofileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(com.example.videocallingapp.ShowuserprofileActivity.this,storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(com.example.videocallingapp.ShowuserprofileActivity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ActivityCompat.checkSelfPermission( com.example.videocallingapp.ShowuserprofileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(com.example.videocallingapp.ShowuserprofileActivity.this,cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit Profile Photo", "Edit Name", "Edit Phone"};
        //android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getActivity())
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.videocallingapp.ShowuserprofileActivity.this);
        //set title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //edit profile click
                    pd.setMessage("Updating profile photo");
                    profilePhoto="image";// here image is field name in Users Table created
                    showImagePicDialog();

                } else if (which == 1) {
                    //edit name click
                    pd.setMessage("Updating name");
                    showNamePhoneUpdateDialog("name");
                } else if (which == 2) {
                    //edit phone click
                    pd.setMessage("Updating phone");
                    showNamePhoneUpdateDialog("phone");
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }
    //for both name and phone updating
    private void showNamePhoneUpdateDialog(final String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(com.example.videocallingapp.ShowuserprofileActivity.this);
        builder.setTitle("Update:"+key);
        LinearLayout linearLayout=new LinearLayout(getApplicationContext());//at 49:24===================
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        EditText editText=new EditText(getApplicationContext());
        editText.setHint("Enter"+key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        //add button to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input
                String uname=editText.getText().toString().trim();
                String newuname=toTitleCaseUname(uname);//calling method toTitleCaseUname() defined below
                if(!TextUtils.isEmpty(newuname)){
                    pd.show();
                    HashMap<String, Object> result=new HashMap<>();
                    result.put(key,newuname);
                    databaseReference.child(user.getUid()).updateChildren(result)//for update name and phone no-------
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter"+key,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //create and show dialog
        builder.create().show();
    }

     //converting user name to titlecase calling from above
    private static String toTitleCaseUname(String str) {
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
    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        //android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getActivity())
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.videocallingapp.ShowuserprofileActivity.this);
        //set title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Camera click
                    if(!checkCameraPermission()){
                        requestCameraPermission();//======================
                    }
                    else
                    {
                        openCamera();
                        //pickFromCamera();//============================
                    }

                } else if (which == 1) {
                    //Gallery click
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else
                    {
                        pickFromGallery();//====================
                    }
                }

            }
        });
        //create and show dialog
        builder.create().show();
    }
    //first check firebase storage rules

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
//pick from camera
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //if permission granted
                        pickFromCamera();//========================
                    } else {
                        //if permission denied
                        Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "Enable Camera and Storage permissions.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                //pick from gallery
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permission granted
                        pickFromGallery();//============================
                      }
                    else {
                        Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "Enable Storage permissions.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
    //the following method below will call after picking image from camera or gallery for uploading profile photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
//image is pick from gallery,get uri of image
                image_uri=data.getData();//at 34:15 mins
                uploadProfilePhotoFromGallery(image_uri); //calling method uploadProfilePhoto() below
            }
            /*
            if(requestCode==IMAGE_PICK_CAMERA_CODE){
//image is pick from camera,get uri of image
                //uploadProfilePhotoFromCamera(image_uri);  //calling method=============================
                //pickFromCamera();
                openCamera();
            }
            */
            if(requestCode==CAMERA_REQUEST_CODE && resultCode== Activity.RESULT_OK){
                Bundle extras=data.getExtras();
                Bitmap bitmap=(Bitmap)extras.get("data");
                avatarTv.setImageBitmap(bitmap);
                image_uri=getImageUri(getApplicationContext(),bitmap); // 6. calling getImageUri method-----------
                uploadProfilePhotoFromCamera(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhotoFromCamera(Uri image_uri) {
        //show progress dialog
        pd.show();
        storageReference=getInstance().getReference();
//path and name of the image to be stored in firebase storage
        String filePathAndName=storagePath+""+ profilePhoto +"_"+ user.getUid();
        StorageReference storageReference2nd=storageReference.child(filePathAndName);
        storageReference2nd.putFile(image_uri)
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
                            HashMap<String,Object>results=new HashMap<>();
                            //results.put(profilePhoto,downloadUri.toString());//here profilePhoto="image" initialized above
                            //alternative code of the above line
                            results.put("image",downloadUri.toString());// here image is field name in Users Table created
                            databaseReference.child(user.getUid()).updateChildren(results)//Update pfrofile photo==========================
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(ShowuserprofileActivity.this, "Profile photo updated.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //dismiss progress bar
                                    pd.dismiss();
                                    Toast.makeText(ShowuserprofileActivity.this, "Profile photo updating failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(ShowuserprofileActivity.this, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// if there is some error
                        pd.dismiss();
                        Toast.makeText(ShowuserprofileActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Title",null);
        return Uri.parse(path);
    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,CAMERA_REQUEST_CODE);
        }
    }

    //for updating user photo from gallery
    private void uploadProfilePhotoFromGallery(Uri uri) {
        //show progress dialog
        pd.show();
        storageReference=getInstance().getReference();
//path and name of the image to be stored in firebase storage
        String filePathAndName=storagePath+""+ profilePhoto +"_"+ user.getUid();
        StorageReference storageReference2nd=storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
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
                            HashMap<String, Object> results=new HashMap<>();
                            //results.put(profilePhoto,downloadUri.toString());//here profilePhoto="image" initialized above
                            //alternative code of the above line
                            results.put("image",downloadUri.toString());// here image is field name in Users Table created
                            databaseReference.child(user.getUid()).updateChildren(results)//Update pfrofile photo==========================
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "Profile photo updated.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //dismiss progress bar
                                    pd.dismiss();
                                    Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "Profile photo updating failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// if there is some error
                        pd.dismiss();
                        Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
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
        getCurrentUserInformation();//calling getCurrentUserInformation() method defined below
        super.onStart();
    }
    // Display Name, email address, and profile photo Url---------------------------
    private void getCurrentUserInformation() {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get data
                        String name = "" + ds.child("name").getValue();
                        String email = "" + ds.child("email").getValue();
                        String phone = "" + ds.child("phone").getValue();
                        String image = "" + ds.child("image").getValue();
                        //set data
                        nameTv.setText(name);
                        emailTv.setText(email);
                        phoneTv.setText(phone);
                        try {
                            //if image is available
                            //get() is used for picasso 2.71828
                            Picasso.get().load(image).into(avatarTv);
                        } catch (Exception e) {
                            //if image is not available set default image
                            Picasso.get().load(R.drawable.ic_add_photo).into(avatarTv);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //if image is not available set default image
                    Picasso.get().load(R.drawable.ic_add_photo).into(avatarTv);
                }
            });

           } else {
            Toast.makeText(com.example.videocallingapp.ShowuserprofileActivity.this, "User profile not found!.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    private void pickFromCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        //image_uri=getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        image_uri= com.example.videocallingapp.ShowuserprofileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to start camera
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent=new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }
}