package com.example.videocallingapp;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AdddoctorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    final int PICK_IMAGE_REQUEST = 1;
    private Button button_choose_fimage,buttonfAdd,buttonDateffrom;
    private TextView textViewShowfImage;
    private EditText edit_text_file_fname,editTextfAge,editTextfContactno,editTextfDoj,editTextfAddress;
    private Spinner spinnerfSubjects;
    //private RadioGroup mradioGroup1;
    private RadioButton radiofMale,radiofFemale;
    ImageView image_fview;
    private ProgressBar progress_fbar;

    private Uri mImageUri;
    //for uploading image
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    ConstraintLayout activity_adddoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddoctor);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Doctor");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity_adddoctor = (ConstraintLayout) findViewById(R.id.activity_adddoctor);
        button_choose_fimage=findViewById(R.id.button_choose_fimage);
        buttonfAdd=findViewById(R.id.buttonfAdd);
        edit_text_file_fname=findViewById(R.id.edit_text_file_fname);
        editTextfAge=findViewById(R.id.editTextfAge);
        editTextfContactno=findViewById(R.id.editTextfContactno);
        spinnerfSubjects=findViewById(R.id.spinnerfSubjects);
        //RadioGroup grp=(RadioGroup) findViewById(R.id.radioGroup1);
        //RadioButton answer=(RadioButton) findViewById(grp.getCheckedRadioButtonId());
        //String gender=answer.getText().toString();
        //mradioGroup1=findViewById(R.id.radioGroup1);
        radiofMale=findViewById(R.id.radiofMale);
        radiofFemale=findViewById(R.id.radiofFemale);
        buttonDateffrom=findViewById(R.id.buttonDateffrom);
        //meditTextDob=findViewById(R.id.editTextDob);
        editTextfDoj=findViewById(R.id.editTextfDoj);
        editTextfAddress=findViewById(R.id.editTextfAddress);
        image_fview=findViewById(R.id.image_fview);
        textViewShowfImage=findViewById(R.id.textViewShowfImage);
        textViewShowfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();//defined below
            }
        });

        buttonDateffrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        progress_fbar=findViewById(R.id.progress_fbar);
        //for uploading image
        mStorageRef= FirebaseStorage.getInstance().getReference("Doctors");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Doctors");

        mDatabaseRef.keepSynced(true);// for Offline mode enabled-----------------------------

        buttonfAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress())
                {
                    Toast.makeText(com.example.videocallingapp.AdddoctorActivity.this, "Please wait while uploading image!", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadFile();
                }
            }
        });
        button_choose_fimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void showDatePickerDialog() {
        //DatePickerDialog datePickerDialog;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    //for displaying images
    private void openImagesActivity() {
        Intent intent=new Intent(com.example.videocallingapp.AdddoctorActivity.this,DoctorActivity.class);
        startActivity(intent);
    }

    //for uploading image--------------------------------------------------------------------------
// for uploading image file extension
    private String getFileExtension(Uri uri){ //calling from uploadFile method below
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    //for uploading image
    private void uploadFile() {
        RadioGroup grp=(RadioGroup) findViewById(R.id.radioGroupf);
        int radioId=grp.getCheckedRadioButtonId();
        RadioButton radioButton=findViewById(radioId);
        final String gender=radioButton.getText().toString();
        //int radioId=(RadioGroup) findViewById(R.id.radioGroup1);
        //final RadioButton answer=(RadioButton) findViewById(grp.getCheckedRadioButtonId());
        //String gender=answer.getText().toString();
        //Calendar cal=Calendar.getInstance();
        //SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //String joiningDate=sdf.format(cal.getTime());
        //final String dob=sdf.format(meditTextDob);
        //final String dob=String.valueOf(meditTextDob);
        if(mImageUri!=null)
        {
            //StorageReference fileReference=mStorageRef.child("uploads/"+ System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            //here currentTimeMillis current time in milliseconds will be the image name
            final StorageReference fileReference=mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri)); //calling getFileExtensione method above

            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress_fbar.setProgress(0);
                                }
                            },500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri mImageUri) {
                                    Doctor doctor;// Alternative statement of the above statement -----------------
                                    String doctorId=mDatabaseRef.push().getKey();
                                    String doctorname=edit_text_file_fname.getText().toString().trim();
                                    String age=editTextfAge.getText().toString().trim();
                                    String contactno=editTextfContactno.getText().toString().trim();
                                    String subject=spinnerfSubjects.getSelectedItem().toString().trim();
                                    String doj=editTextfDoj.getText().toString().trim();
                                    String address=editTextfAddress.getText().toString().trim();
                                    doctor = new Doctor(doctorId,doctorname,age,contactno,subject,gender,doj,address,mImageUri.toString());
                                    // artist = new Artist(artistId,mEditTextFileName.getText().toString().trim(),meditTextAge.getText().toString().trim(),meditTextContactno.getText().toString().trim(),mspinnerGenres.getSelectedItem().toString().trim(),gender,meditTextDob.getText().toString().trim(),meditTextAddress.getText().toString().trim(),uri.toString());
                                    mDatabaseRef.child(doctorId).setValue(doctor);
                                    Toast.makeText(com.example.videocallingapp.AdddoctorActivity.this, "Doctor added successfully!", Toast.LENGTH_LONG).show();
                                    image_fview.setImageResource(R.drawable.addpicture);
                                    edit_text_file_fname.setText("");
                                    editTextfAge.setText("");
                                    editTextfContactno.setText("");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.videocallingapp.AdddoctorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progress_fbar.setProgress((int)progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No image file selected", Toast.LENGTH_LONG).show();
        }
    }
    //-------------------------------------------------------------------------------
//for opening image file location
    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent,PICK_IMAGE_REQUEST);
        startActivityForResult(intent,1);  //calling onActivityResult method below
    }//after writing code upto above code press CTRL + O then select the above onActivityResult method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            mImageUri= data.getData();
            //mImageView.setImageURI(mImageUri);
            //Picasso.with(this).load(mImageUri).into(image_fview);
            Picasso.get().load(mImageUri).into(image_fview);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(month<10) {
            String datefrom = dayOfMonth + "-" + "0" + (month + 1) + "-" + year; //since month starts from zero to eleven
            //SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            //String classDate=sdf.format(datefrom.);
            //mtxtDatefrom.setText(classDate);
            editTextfDoj.setText(datefrom);
        }
        else {
            String datefrom = dayOfMonth + "-" + (month + 1) + "-" + year; //since month starts from zero to eleven
            //SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            //String classDate=sdf.format(datefrom.);
            //mtxtDatefrom.setText(classDate);
            editTextfDoj.setText(datefrom);
        }
    }
    //for three dot menu------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(activity_adddoctor, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.AdddoctorActivity.this,StartActivity.class));
        }
        if (item.getItemId() == R.id.menu_student) {
            //startActivity(new Intent(AdddoctorActivity.this,ManagestudentActivity.class));
        }
        if (item.getItemId() == R.id.menu_faculty) {
            //startActivity(new Intent(AdddoctorActivity.this,ManagefacultyActivity.class));
        }
        if (item.getItemId() == R.id.menu_video) {
            //startActivity(new Intent(AdddoctorActivity.this,ManagefacultyActivity.class));
        }
        if (item.getItemId() == R.id.menu_callsms) {
            //startActivity(new Intent(AdddoctorActivity.this,SendsmsActivity.class));
        }
        if (item.getItemId() == R.id.menu_chat) {
            //startActivity(new Intent(AdddoctorActivity.this,ChatroomActivity.class));
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    //Getting current userinfo and restriction to the user for accessing
    @Override
    protected void onStart() {
        //getCurrentUserInfo();//calling method getCurrentUserInfo defined below
        super.onStart();
    }
    /* For restriction of adding doctor see in security rules-----------------------------
    //Getting current userinfo and restriction to the user for accessing
    private void getCurrentUserInfo() {
        //firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        if (user != null && !(email.equals("admin@gmail.com"))) {

            edit_text_file_fname.setEnabled(false);
            editTextfAge.setEnabled(false);
            editTextfContactno.setEnabled(false);
            editTextfDoj.setEnabled(false);
            editTextfAddress.setEnabled(false);

            buttonfAdd.setEnabled(false);
            buttonDateffrom.setEnabled(false);
            button_choose_fimage.setEnabled(false);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
    }*/
}

