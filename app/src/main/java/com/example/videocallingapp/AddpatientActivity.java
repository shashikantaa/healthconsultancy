package com.example.videocallingapp;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
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

public class AddpatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    final int PICK_IMAGE_REQUEST = 1;
    private Button button_choose_pimage,buttonPatientAdd,buttonDatepfrom;
    private TextView textViewShowpImage;
    private EditText edit_text_file_pname,editTextpAge,editTextpContactno,editTextpDot,editTextpMedicine,editTextpAddress;
    private Spinner spinnerpDiseases,spinnerNoopvisits;
    //private RadioGroup mradioGroup1;
    private RadioButton radiopMale,radiopFemale;
    ImageView image_pview;
    private ProgressBar progress_pbar;

    private Uri mImageUri;
    //for uploading image
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    ConstraintLayout activity_addpatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);
        //action bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Patient");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        activity_addpatient = (ConstraintLayout) findViewById(R.id.activity_addpatient);
        button_choose_pimage=findViewById(R.id.button_choose_pimage);
        buttonPatientAdd=findViewById(R.id.buttonPatientAdd);
        edit_text_file_pname=findViewById(R.id.edit_text_file_pname);
        editTextpAge=findViewById(R.id.editTextpAge);
        editTextpContactno=findViewById(R.id.editTextpContactno);
        spinnerpDiseases=findViewById(R.id.spinnerpDiseases);
        spinnerNoopvisits=findViewById(R.id.spinnerNoopvisits);
        radiopMale=findViewById(R.id.radiofMale);
        radiopFemale=findViewById(R.id.radiofFemale);
        buttonDatepfrom=findViewById(R.id.buttonDatepfrom);
        editTextpDot=findViewById(R.id.editTextpDot);
        editTextpAddress=findViewById(R.id.editTextpAddress);
        editTextpMedicine=findViewById(R.id.editTextpMedicine);
        image_pview=findViewById(R.id.image_pview);
        textViewShowpImage=findViewById(R.id.textViewShowpImage);
        textViewShowpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();//defined below
            }
        });

        buttonDatepfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        progress_pbar=findViewById(R.id.progress_pbar);
        //for uploading image
        mStorageRef= FirebaseStorage.getInstance().getReference("Patients");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Patients");

        //mDatabaseRef.keepSynced(true);// for Offline mode enabled-----------------------------

        buttonPatientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress())
                {
                    Toast.makeText(com.example.videocallingapp.AddpatientActivity.this, "Please wait while uploading image!", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadPatientFile();
                }
            }
        });
        button_choose_pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }
    //please not if there is error when writing the following codes then add the foolowing code at the beginning  of class as
    //public class AddpatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
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
    //for date picker dialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(month<10) {
            String datefrom = dayOfMonth + "-" + "0" + (month + 1) + "-" + year; //since month starts from zero to eleven
            //SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            //String classDate=sdf.format(datefrom.);
            //mtxtDatefrom.setText(classDate);
            editTextpDot.setText(datefrom);
        }
        else {
            String datefrom = dayOfMonth + "-" + (month + 1) + "-" + year; //since month starts from zero to eleven
            //SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            //String classDate=sdf.format(datefrom.);
            //mtxtDatefrom.setText(classDate);
            editTextpDot.setText(datefrom);
        }
    }
    //end of date picker

    //for displaying images
    private void openImagesActivity() {
        Intent intent=new Intent(com.example.videocallingapp.AddpatientActivity.this,ShowpatientActivity.class);
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
    private void uploadPatientFile() {
        String patientname=edit_text_file_pname.getText().toString().trim();
        String pname=toTitleCasePname(patientname);//calling method toTitleCasePname() defined below
        RadioGroup grpp=(RadioGroup) findViewById(R.id.radioGroupp);
        int radioIdp=grpp.getCheckedRadioButtonId();
        RadioButton radioButtonp=findViewById(radioIdp);
        final String genderp=radioButtonp.getText().toString();
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
                                    progress_pbar.setProgress(0);
                                }
                            },500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri mImageUri) {
                                    Patient patient;// Alternative statement of the above statement -----------------
                                    String patientId=mDatabaseRef.push().getKey();
                                    //String patientname=edit_text_file_pname.getText().toString().trim();
                                    String age=editTextpAge.getText().toString().trim();
                                    String contactno=editTextpContactno.getText().toString().trim();
                                    String disease=spinnerpDiseases.getSelectedItem().toString().trim();
                                    String novisits=spinnerNoopvisits.getSelectedItem().toString().trim();
                                    String dot=editTextpDot.getText().toString().trim();
                                    String address=editTextpAddress.getText().toString().trim();
                                    editTextpMedicine.setText("To be prescribed by doctor");
                                    String medicine=editTextpMedicine.getText().toString();
                                    patient = new Patient(patientId,pname,age,contactno,disease,novisits,genderp,dot,address,medicine,mImageUri.toString());
                                    // artist = new Artist(artistId,mEditTextFileName.getText().toString().trim(),meditTextAge.getText().toString().trim(),meditTextContactno.getText().toString().trim(),mspinnerGenres.getSelectedItem().toString().trim(),gender,meditTextDob.getText().toString().trim(),meditTextAddress.getText().toString().trim(),uri.toString());
                                    mDatabaseRef.child(patientId).setValue(patient);
                                    //Toast Text with background and color---------------------------------
                                    Toast toast=Toast.makeText(com.example.videocallingapp.AddpatientActivity.this,"Dear Patient your appointment has submitted successfully!",Toast.LENGTH_LONG);
                                    View view =toast.getView();
                                    view.setBackgroundColor(Color.BLUE);
                                    TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                    toastMessage.setTextColor(Color.WHITE);
                                    toast.show();
                                    //------------------------
                                    //Toast.makeText(com.example.videocallingapp.AddpatientActivity.this, "Patient added successfully!", Toast.LENGTH_LONG).show();
                                    image_pview.setImageResource(R.drawable.addpicture);
                                    edit_text_file_pname.setText("");
                                    editTextpAge.setText("");
                                    editTextpContactno.setText("");
                                    editTextpDot.setText("");
                                    editTextpAddress.setText("");
                                    editTextpMedicine.setText("");
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.videocallingapp.AddpatientActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progress_pbar.setProgress((int)progress);
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No image file selected", Toast.LENGTH_LONG).show();
        }
    }
//converting patient name to titlecase calling from above
    private static String toTitleCasePname(String str) {
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
            Picasso.get().load(mImageUri).into(image_pview);
        }
    }
//-------------------------------------------------------------------------------------------------------------------
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
            Snackbar.make(activity_addpatient, "You have been signed out!", Snackbar.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(com.example.videocallingapp.AddpatientActivity.this,StartActivity.class));
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
        if (user != null && email.equals("admin@gmail.com")) {

            //edit_text_file_pname.setEnabled(false);
            //editTextpAge.setEnabled(false);
            //editTextpContactno.setEnabled(false);
            //editTextpDot.setEnabled(false);
            editTextpMedicine.setEnabled(true);
        }
        if (user != null && !(email.equals("admin@gmail.com"))) {

            //edit_text_file_pname.setEnabled(false);
            //editTextpAge.setEnabled(false);
            //editTextpContactno.setEnabled(false);
            //editTextpDot.setEnabled(false);
            editTextpMedicine.setEnabled(false);
            //editTextpAddress.setEnabled(false);

            //buttonPatientAdd.setEnabled(false);
            //buttonDatepfrom.setEnabled(false);
            //button_choose_pimage.setEnabled(false);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
    }
}

