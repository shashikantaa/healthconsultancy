package com.example.videocallingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhonecallActivity extends AppCompatActivity {
    private static final int REQUEST_CALL=1;
    EditText edit_text_number,mEditTextNumber;
    ImageView imageCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecall);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Make Call");

        mEditTextNumber=(EditText)findViewById(R.id.edit_text_number);
        String Tempnoholder=getIntent().getStringExtra("Phonenoclickvalue");
        mEditTextNumber.setText(Tempnoholder);

        ImageView imageCall=findViewById(R.id.image_call);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }
    private void makePhoneCall() {
        String number=mEditTextNumber.getText().toString();
        if(number.trim().length()>0){

            if(ContextCompat.checkSelfPermission(com.example.videocallingapp.PhonecallActivity.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(com.example.videocallingapp.PhonecallActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial="tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else{
            Toast.makeText(com.example.videocallingapp.PhonecallActivity.this,"Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults){
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
            else{
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
