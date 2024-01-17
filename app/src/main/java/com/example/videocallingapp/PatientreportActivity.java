package com.example.videocallingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class PatientreportActivity extends AppCompatActivity {
    final int PICK_IMAGE_REQUEST = 1;
    EditText editTextpatId, editTextpatName, editTextpatContactNo, editTextpatAge, editTextpatDot, editTextpatAddress, editTextpatMedicine;
    Button buttonUpdatePatient, buttonDeletePatient, button_choose_newpatimage, buttonpatCall, buttonPatientReport;
    Spinner spinnerpatDiseases, spinnerNoopatvisits;
    ImageView image_viewpatnew;

    private Uri ImagenewUrl;
    DatabaseReference mDatabaseRef, mDatabaseRefupdatebuttonclick;//1. mDatabaseRef is the name of the firebase database reference
    StorageReference mStorageRef;
    String storagePath = "Users_Profile_Imgs/";
    //private DatabaseReference mDatabaseRef;
    StorageTask mUploadTask;
    ProgressDialog pdupdate;
    Bitmap bmp, scaledbmp;
    int pageWidth = 1200;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientreport);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Patient Report");
        String key = getIntent().getExtras().get("pkey").toString();
        mDatabaseRefupdatebuttonclick = FirebaseDatabase.getInstance().getReference().child("Patients").child(key);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        mStorageRef = FirebaseStorage.getInstance().getReference("Patients");
        editTextpatName = (EditText) findViewById(R.id.editTextpatName);
        editTextpatContactNo = (EditText) findViewById(R.id.editTextpatContactNo);
        editTextpatAge = (EditText) findViewById(R.id.editTextpatAge);
        editTextpatDot = (EditText) findViewById(R.id.editTextpatDot);
        editTextpatAddress = (EditText) findViewById(R.id.editTextpatAddress);
        spinnerpatDiseases = (Spinner) findViewById(R.id.spinnerpatDiseases);
        spinnerNoopatvisits = (Spinner) findViewById(R.id.spinnerNoopatvisits);
        editTextpatMedicine= (EditText) findViewById(R.id.editTextpatMedicine);
        editTextpatId = (EditText) findViewById(R.id.editTextpatId);
        //mbutton_choose_newimage=(Button) findViewById(R.id.button_choose_newsimage);
        buttonDeletePatient = (Button) findViewById(R.id.buttonDeletePatient);
        buttonUpdatePatient = (Button) findViewById(R.id.buttonUpdatePatient);

        mFirebaseAuth = FirebaseAuth.getInstance();
//For Patient Report----------------------------------------------------------------------------------
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.doctorcall);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);
        //-----------------------------------------------------------------------------
        buttonPatientReport = (Button) findViewById(R.id.buttonPatientReport);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        createPDFreport();//Calling createPDFreport() method defined below
//-------------------------------------------------------------------------------------------
        button_choose_newpatimage = (Button) findViewById(R.id.button_choose_newpatimage);
        buttonpatCall = (Button) findViewById(R.id.buttonpatCall);

        image_viewpatnew = (ImageView) findViewById(R.id.image_viewpatnew);
        //init progress dialog
        pdupdate = new ProgressDialog(com.example.videocallingapp.PatientreportActivity.this);

        button_choose_newpatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimageFileChooser();
            }
        });
        String Temppid = getIntent().getStringExtra("pkey");//here key is nothing but the artisteId

        //mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("students").child(Tempaid);//2. students is the name of the table to be created in firebase database created
        String Temppname = getIntent().getStringExtra("pname");
        String Temppcontactno = getIntent().getStringExtra("pcontactno");
        String Temppage = getIntent().getStringExtra("page");
        String Temppaddress = getIntent().getStringExtra("paddress");
        String Temppmedicine = getIntent().getStringExtra("pmedicine");
        String Temppdot = getIntent().getStringExtra("pdot");
        String Temppdisease = getIntent().getStringExtra("pdisease");
        String Temppnovisit = getIntent().getStringExtra("pnovisit");
        String Temppgender = getIntent().getStringExtra("pgender");
        String Temppimageurl = getIntent().getStringExtra("pimageurl");
        //---------------------------------------------------------------------------
        setpImage(Temppimageurl);//calling the method setImage() below to display image
        //----------------------------------------------------------------------------
        editTextpatId.setText(Temppid);
        editTextpatName.setText(Temppname);
        editTextpatContactNo.setText(Temppcontactno);
        editTextpatAge.setText(Temppage);
        editTextpatAddress.setText(Temppaddress);
        editTextpatMedicine.setText(Temppmedicine);
        editTextpatDot.setText(Temppdot);
        //for getting the passing genre value to a spinner --------------------------------
        spinnerpatDiseases.setSelection(getIndex_SpinnerItem(spinnerpatDiseases, Temppdisease));//defined method getIndex_SpinnerItem below
        spinnerNoopatvisits.setSelection(getIndex_SpinnerItemnoofvisit(spinnerNoopatvisits, Temppnovisit));//defined method getIndex_SpinnerItem below
//----------------------------------------------------------------------------------
        RadioButton radpatmale = findViewById(R.id.radioMalepatUpdate);
        RadioButton radpatfemale = findViewById(R.id.radioFemalepatUpdate);
        if (Temppgender.equals("Male")) {
            radpatmale.setChecked(true);
        }
        if (Temppgender.equals("Female")) {
            radpatfemale.setChecked(true);
        }
        buttonpatCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.videocallingapp.PatientreportActivity.this, SendsmsActivity.class);
                intent.putExtra("acontactnotosms", editTextpatContactNo.getText().toString());
                startActivity(intent);
                //startActivity(new Intent(UpdatedeleteartistActivity.this,SendsmsActivity.class));
            }
        });
        //final String genderUpdate=radioButtonUpdate.getText().toString();
        //-----------------------------------------------------------

        buttonUpdatePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientnewname=editTextpatName.getText().toString().trim();
                String pnewname=toTitleCasePname(patientnewname);//calling method toTitleCasePname() defined below
                editTextpatName.setText(pnewname);
                RadioGroup grpUpdate = (RadioGroup) findViewById(R.id.radioGrouppatUpdate);
                int radioIdUpdate = grpUpdate.getCheckedRadioButtonId();
                final RadioButton radioButtonUpdate = findViewById(radioIdUpdate);
                //final String niurl=getNewImageUrlforupdation(ImagenewUrl);
                //UpdateArtist();
                mDatabaseRefupdatebuttonclick.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("mName").setValue(editTextpatName.getText().toString());
                        dataSnapshot.getRef().child("mContactno").setValue(editTextpatContactNo.getText().toString());
                        dataSnapshot.getRef().child("mAge").setValue(editTextpatAge.getText().toString());
                        dataSnapshot.getRef().child("mAddress").setValue(editTextpatAddress.getText().toString());
                        dataSnapshot.getRef().child("mMedicine").setValue(editTextpatMedicine.getText().toString());
                        dataSnapshot.getRef().child("mDot").setValue(editTextpatDot.getText().toString());
                        dataSnapshot.getRef().child("mDisease").setValue(spinnerpatDiseases.getSelectedItem().toString());
                        dataSnapshot.getRef().child("mNoofvisit").setValue(spinnerNoopatvisits.getSelectedItem().toString());
                        dataSnapshot.getRef().child("mGender").setValue(radioButtonUpdate.getText().toString());
                        //Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "New record updated successfully!", Toast.LENGTH_LONG).show();
                        //Toast Text with background and color---------------------------------
                        Toast toast=Toast.makeText(com.example.videocallingapp.PatientreportActivity.this,"Patient's new record updated successfully!",Toast.LENGTH_LONG);
                        View view =toast.getView();
                        view.setBackgroundColor(Color.BLUE);
                        TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                        toastMessage.setTextColor(Color.WHITE);
                        toast.show();
                        //------------------------
                        com.example.videocallingapp.PatientreportActivity.this.finish();
                        startActivity(new Intent(com.example.videocallingapp.PatientreportActivity.this, ShowpatientActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        buttonDeletePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Record Deleted Successfully...", Toast.LENGTH_LONG).show();
                            com.example.videocallingapp.PatientreportActivity.this.finish();
                        } else {
                            Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Record Not Deleted...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private String toTitleCasePname(String str) {
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
    //Creating Patient Report------------------------------------------------------------------------

    private void createPDFreport() {

        buttonPatientReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grpUpdate = (RadioGroup) findViewById(R.id.radioGrouppatUpdate);
                int radioIdUpdate = grpUpdate.getCheckedRadioButtonId();
                final RadioButton radioButtonUpdate = findViewById(radioIdUpdate);
                if (editTextpatName.getText().toString().length() == 0 || editTextpatContactNo.getText().toString().length() == 0) {
                    Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Some fields are empty !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlePaint = new Paint();
                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    canvas.drawBitmap(scaledbmp, 0, 0, myPaint);
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(70);
                    canvas.drawText("Online Treatment", pageWidth / 2, 270, titlePaint);

                    myPaint.setColor(Color.rgb(0, 113, 188));
                    myPaint.setTextSize(30f);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Call: 9402836042", 1160, 40, myPaint);
                    canvas.drawText("0385-1234567", 1160, 80, myPaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                    titlePaint.setTextSize(50);
                    canvas.drawText("Patient Treatment Report", pageWidth / 2, 620, titlePaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(30f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Patient Name:" + editTextpatName.getText(), 20, 720, myPaint);
                    canvas.drawText("Age:" + editTextpatAge.getText(), 20, 770, myPaint);
                    canvas.drawText("Address:" + editTextpatAddress.getText(), 20, 820, myPaint);
                    canvas.drawText("Date of Treatment:" + editTextpatDot.getText(), 20, 870, myPaint);
                    canvas.drawText("Contact No:" + editTextpatContactNo.getText(), 20, 920, myPaint);
                    canvas.drawText("Gender:" + radioButtonUpdate.getText(), 20, 970, myPaint);
                    canvas.drawText("No. of Visits:" + spinnerNoopatvisits.getSelectedItem(), 20, 1020, myPaint);
                    canvas.drawText("Treatment For:" + spinnerpatDiseases.getSelectedItem(), 20, 1070, myPaint);
                    canvas.drawText("Medicines Prescribed:", 20, 1150, myPaint);
                    canvas.drawText("Rx:", 20, 1250, myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(32f);
                    myPaint.setColor(Color.BLACK);

                    //for displaying medicines line by line;
                    String fileName=editTextpatName.getText().toString();

                    String medicines=editTextpatMedicine.getText().toString();
                    int x=30,y=1300;
                    for(String line:medicines.split("\n"))//every line for pressing enter in the multiline edittext of medicine
                    {
                      myPage1.getCanvas().drawText(line,x,y,myPaint);
                      y+=myPaint.descent()-myPaint.ascent();
                    }

                    myPdfDocument.finishPage(myPage1);

                    File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/Download/"+fileName+".pdf");
                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myPdfDocument.close();
                    Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Report generated successfully...", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    //Ends Patient Report in PDF Creation-------------------------------------------
    //display the image calling from above
    private void setpImage(String temppimageurl) {

        //Picasso.with(this).load(tempimageurl)
        //for picasso 2.71828
        Picasso.get().load(temppimageurl)
                .fit()
                .centerCrop()
                //.centerInside()
                .into(image_viewpatnew);
    }

    // for uploading image file extension
    private String getFileExtension(Uri uri) { //calling from uploadFile method below
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openimageFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent,PICK_IMAGE_REQUEST);
        startActivityForResult(intent, 1);
    }//after writing code upto above code press CTRL + O then select the above onActivityResult method

    //loading selected image to the image_view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        //if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            ImagenewUrl = data.getData();
            Picasso.get().load(ImagenewUrl).into(image_viewpatnew);
            updatePatientdata(ImagenewUrl); //calling method uploadProfilePhoto() below
        }
    }

    //Patient data updating with new photo
    private void updatePatientdata(Uri uri) {
        String key = getIntent().getExtras().get("pkey").toString();
        String patientnewname=editTextpatName.getText().toString().trim();
        String pnewname=toTitleCasePname(patientnewname);//calling method toTitleCasePname() defined below
        editTextpatName.setText(pnewname);
        ;//here key is nothing but the artisteId
        RadioGroup grpUpdate = (RadioGroup) findViewById(R.id.radioGrouppatUpdate);
        int radioIdUpdate = grpUpdate.getCheckedRadioButtonId();
        final RadioButton radioButtonUpdate = findViewById(radioIdUpdate);
        pdupdate.setMessage("Updating patient data...");
        //show progress dialog
        pdupdate.show();
        mStorageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// image is uploaded to storage and store to the user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();
                        //check if image is uploaded or not and url is received
                        if (uriTask.isSuccessful()) {
                            //image uploaded and add/update url in user's database
                            HashMap<String, Object> hashMap = new HashMap<>();
                            //results.put(profilePhoto,downloadUri.toString());//here profilePhoto="image" initialized above
                            //alternative code of the above line
                            String patName = editTextpatName.getText().toString();
                            String patContactno = editTextpatContactNo.getText().toString();
                            String patDisease = spinnerpatDiseases.getSelectedItem().toString();
                            String patNoopatvisits = spinnerNoopatvisits.getSelectedItem().toString();
                            String patGender = radioButtonUpdate.getText().toString();
                            String patAddress = editTextpatAddress.getText().toString();
                            String patmedicine = editTextpatMedicine.getText().toString();

                            hashMap.put("mName", "" + patName);// here mName is field name in Patient Table created
                            hashMap.put("mContactno", "" + patContactno);
                            hashMap.put("mDisease", "" + patDisease);
                            hashMap.put("mNoofvisit", "" + patNoopatvisits);
                            hashMap.put("mGender", "" + patGender);
                            hashMap.put("mAddress", "" + patAddress);
                            hashMap.put("mMedicine", "" + patmedicine);
                            hashMap.put("mImageUrl", "" + downloadUri);// here mImageUrl is field name in Patient Table created

                            mDatabaseRef.child(key).updateChildren(hashMap)//Update pfrofile photo==========================
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //dismiss progress bar
                                            pdupdate.dismiss();
                                            //Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Patient data updated successfully.",Toast.LENGTH_SHORT).show();
                                            //Toast Text with background and color---------------------------------
                                            Toast toast=Toast.makeText(com.example.videocallingapp.PatientreportActivity.this,"Patient data updated successfully.!",Toast.LENGTH_LONG);
                                            View view =toast.getView();
                                            view.setBackgroundColor(Color.BLUE);
                                            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                                            toastMessage.setTextColor(Color.WHITE);
                                            toast.show();
                                            //------------------------
                                            startActivity(new Intent(com.example.videocallingapp.PatientreportActivity.this, ShowpatientActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //dismiss progress bar
                                    pdupdate.dismiss();
                                    Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Profile photo updating failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            pdupdate.dismiss();
                            Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// if there is some error
                        pdupdate.dismiss();
                        Toast.makeText(com.example.videocallingapp.PatientreportActivity.this, e.getMessage(),
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
//searching the passed course from the list of spinner
    private int getIndex_SpinnerItemnoofvisit(Spinner spinner, String item) {
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

            editTextpatAddress.setEnabled(true);
            editTextpatName.setEnabled(true);
            editTextpatContactNo.setEnabled(true);
            editTextpatAge.setEnabled(true);
            editTextpatDot.setEnabled(true);
            editTextpatMedicine.setEnabled(true);

            buttonUpdatePatient.setEnabled(true);
            buttonDeletePatient.setEnabled(true);
            button_choose_newpatimage.setEnabled(true);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
        if (user != null && !(email.equals("admin@gmail.com"))) {

                editTextpatAddress.setEnabled(false);
                editTextpatName.setEnabled(false);
                editTextpatContactNo.setEnabled(false);
                editTextpatAge.setEnabled(false);
                editTextpatDot.setEnabled(false);
                editTextpatMedicine.setEnabled(false);
                buttonDeletePatient.setEnabled(false);

                buttonUpdatePatient.setEnabled(true);
                button_choose_newpatimage.setEnabled(true);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
    }
}