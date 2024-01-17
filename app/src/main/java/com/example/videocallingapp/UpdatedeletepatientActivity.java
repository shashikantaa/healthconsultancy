package com.example.videocallingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

public class UpdatedeletepatientActivity extends AppCompatActivity {
    final int PICK_IMAGE_REQUEST = 1;
    EditText editTextpatId,editTextpatName,editTextpatContactNo;
    Button buttonUpdatePatient,buttonDeletePatient,buttonShowPatients,button_choose_newpatimage,buttonpatCall;
    Spinner spinnerpatDiseases,spinnerNoopatvisits;
    ImageView image_viewpatnew;

    private Uri ImagenewUrl;
    DatabaseReference mDatabaseRef,mDatabaseRefupdatebuttonclick;//1. mDatabaseRef is the name of the firebase database reference
    StorageReference mStorageRef;
    String storagePath="Users_Profile_Imgs/";
    //private DatabaseReference mDatabaseRef;
    //StorageTask mUploadTask;
    ProgressDialog pdupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedeletepatient);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Patient Data Updation");
        String key=getIntent().getExtras().get("pkey").toString();
        mDatabaseRefupdatebuttonclick= FirebaseDatabase.getInstance().getReference().child("Patients").child(key);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Patients");
        mStorageRef= FirebaseStorage.getInstance().getReference("Patients");
        editTextpatName = (EditText) findViewById(R.id.editTextpatName);
        editTextpatContactNo = (EditText) findViewById(R.id.editTextpatContactNo);
        //editTextImageurl= (EditText) findViewById(R.id.editTextImageurl);
        //editTextGenre=(EditText)findViewById(R.id.editTextGenre);
        spinnerpatDiseases=(Spinner)findViewById(R.id.spinnerpatDiseases);
        spinnerNoopatvisits=(Spinner)findViewById(R.id.spinnerNoopatvisits);

        editTextpatId=(EditText)findViewById(R.id.editTextpatId);
        //mbutton_choose_newimage=(Button) findViewById(R.id.button_choose_newsimage);
        buttonDeletePatient=(Button) findViewById(R.id.buttonDeletePatient);
        buttonUpdatePatient=(Button) findViewById(R.id.buttonUpdatePatient);
        button_choose_newpatimage=(Button) findViewById(R.id.button_choose_newpatimage);
        buttonpatCall=(Button) findViewById(R.id.buttonpatCall);
        image_viewpatnew=(ImageView)findViewById(R.id.image_viewpatnew);
        //init progress dialog
        pdupdate = new ProgressDialog(com.example.videocallingapp.UpdatedeletepatientActivity.this);

        button_choose_newpatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimageFileChooser();
            }
        });
        String Temppid=getIntent().getStringExtra("pkey");//here key is nothing but the artisteId

        //mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("students").child(Tempaid);//2. students is the name of the table to be created in firebase database created
        String Temppname=getIntent().getStringExtra("pname");
        String Temppcontactno=getIntent().getStringExtra("pcontactno");
        String Temppdisease=getIntent().getStringExtra("pdisease");
        String Temppnovisit=getIntent().getStringExtra("pnovisit");
        String Temppgender=getIntent().getStringExtra("pgender");
        String Temppimageurl=getIntent().getStringExtra("pimageurl");
        //---------------------------------------------------------------------------
        setpImage(Temppimageurl);//calling the method setImage() below to display image
        //----------------------------------------------------------------------------
        editTextpatId.setText(Temppid);
        editTextpatName.setText(Temppname);
        editTextpatContactNo.setText(Temppcontactno);
        //for getting the passing genre value to a spinner --------------------------------
        spinnerpatDiseases.setSelection(getIndex_SpinnerItem(spinnerpatDiseases,Temppdisease));//defined method getIndex_SpinnerItem below
        spinnerNoopatvisits.setSelection(getIndex_SpinnerItemnoofvisit(spinnerNoopatvisits,Temppnovisit));//defined method getIndex_SpinnerItem below
//----------------------------------------------------------------------------------
        RadioButton radpatmale=findViewById(R.id.radioMalepatUpdate);
        RadioButton radpatfemale=findViewById(R.id.radioFemalepatUpdate);
        if(Temppgender.equals("Male"))
        {
            radpatmale.setChecked(true);
        }
        if(Temppgender.equals("Female"))
        {
            radpatfemale.setChecked(true);
        }
        buttonpatCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.videocallingapp.UpdatedeletepatientActivity.this,SendsmsActivity.class);
                intent.putExtra("acontactnotosms",editTextpatContactNo.getText().toString());
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
                RadioGroup grpUpdate=(RadioGroup) findViewById(R.id.radioGrouppatUpdate);
                int radioIdUpdate=grpUpdate.getCheckedRadioButtonId();
                final RadioButton radioButtonUpdate=findViewById(radioIdUpdate);
                //final String niurl=getNewImageUrlforupdation(ImagenewUrl);
                //UpdateArtist();
                mDatabaseRefupdatebuttonclick.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("mName").setValue(editTextpatName.getText().toString());
                        dataSnapshot.getRef().child("mContactno").setValue(editTextpatContactNo.getText().toString());
                        dataSnapshot.getRef().child("mDisease").setValue(spinnerpatDiseases.getSelectedItem().toString());
                        dataSnapshot.getRef().child("mNoofvisit").setValue(spinnerNoopatvisits.getSelectedItem().toString());
                        dataSnapshot.getRef().child("mGender").setValue(radioButtonUpdate.getText().toString());
                        //Toast Text with background and color---------------------------------
                        Toast toast=Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this,"Patient's new record updated successfully!",Toast.LENGTH_LONG);
                        View view =toast.getView();
                        view.setBackgroundColor(Color.BLUE);
                        TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                        toastMessage.setTextColor(Color.WHITE);
                        toast.show();
                        //------------------------
                        //Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this, "New record updated successfully!", Toast.LENGTH_LONG).show();
                        com.example.videocallingapp.UpdatedeletepatientActivity.this.finish();


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
                        if(task.isSuccessful())
                        {
                            Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this,"Record Deleted Successfully...", Toast.LENGTH_LONG).show();
                            com.example.videocallingapp.UpdatedeletepatientActivity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this,"Record Not Deleted...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        buttonShowPatients=(Button) findViewById(R.id.buttonShowPatients);
        buttonShowPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.videocallingapp.UpdatedeletepatientActivity.this,ShowpatientActivity.class));
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
        startActivityForResult(intent,1);
    }//after writing code upto above code press CTRL + O then select the above onActivityResult method
    //loading selected image to the image_view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        //if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            ImagenewUrl= data.getData();
            Picasso.get().load(ImagenewUrl).into(image_viewpatnew);
            updatePatientdata(ImagenewUrl); //calling method uploadProfilePhoto() below
        }
    }
//Patient data updating with new photo
    private void updatePatientdata(Uri uri) {
        String key=getIntent().getExtras().get("pkey").toString();;//here key is nothing but the artisteId
        RadioGroup grpUpdate=(RadioGroup) findViewById(R.id.radioGrouppatUpdate);
        int radioIdUpdate=grpUpdate.getCheckedRadioButtonId();
        final RadioButton radioButtonUpdate=findViewById(radioIdUpdate);
        pdupdate.setMessage("Updating patient data...");
        //show progress dialog
        pdupdate.show();
        mStorageRef.putFile(uri)
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
                            String patName=editTextpatName.getText().toString();
                            String patContactno=editTextpatContactNo.getText().toString();
                            String patDisease=spinnerpatDiseases.getSelectedItem().toString();
                            String patNoopatvisits=spinnerNoopatvisits.getSelectedItem().toString();
                            String patGender=radioButtonUpdate.getText().toString();

                            hashMap.put("mName",""+patName);// here mName is field name in Patient Table created
                            hashMap.put("mContactno",""+patContactno);
                            hashMap.put("mDisease",""+patDisease);
                            hashMap.put("mNoofvisit",""+patNoopatvisits);
                            hashMap.put("mGender",""+patGender);
                            hashMap.put("mImageUrl",""+downloadUri);// here mImageUrl is field name in Patient Table created

                            mDatabaseRef.child(key).updateChildren(hashMap)//Update pfrofile photo==========================
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //dismiss progress bar
                                            pdupdate.dismiss();
                                            Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this, "Patient data updated.",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(com.example.videocallingapp.UpdatedeletepatientActivity.this,ShowpatientActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //dismiss progress bar
                                    pdupdate.dismiss();
                                    Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this, "Profile photo updating failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            pdupdate.dismiss();
                            Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this, "Some error occured!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// if there is some error
                        pdupdate.dismiss();
                        Toast.makeText(com.example.videocallingapp.UpdatedeletepatientActivity.this, e.getMessage(),
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
//-------------------------------------------------------------------------------------------
//Getting current userinfo and restriction to the user for accessing
/*
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

            buttonDeletePatient.setEnabled(false);
            //buttonDeleteDoctor.setEnabled(false);
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

            //buttonUpdateDoctor.setEnabled(true);
            buttonDeletePatient.setEnabled(true);
            //button_choose_fimage.setEnabled(false);
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
        }*/
}