package com.example.videocallingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SendsmsActivity extends AppCompatActivity {
    EditText txt_message, txt_pNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Send SMS");
        //Intent intent=getIntent();
        String Tempholder = getIntent().getStringExtra("acontactnotosms");
        txt_message = (EditText) findViewById(R.id.txt_message);
        txt_pNumber = (EditText) findViewById(R.id.txt_phone_number);
        txt_pNumber.setText(Tempholder);
    }

    public void btn_send(View view) {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            MyMessage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }

    }

    private void MyMessage() {
        String phoneNumber = txt_pNumber.getText().toString().trim();
        String message = txt_message.getText().toString().trim();
        if (phoneNumber.length() > 0 && message.length() > 0) {
            // if (!txt_pNumber.getText().toString().equals("") || !txt_message.getText().toString().equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
            //for changing the color of Toast text
            SpannableString spannableString = new SpannableString("Message sent!");
            spannableString.setSpan(
                    new ForegroundColorSpan(getResources().getColor(android.R.color.holo_red_dark)),
                    0, spannableString.length(), 0);
            Toast.makeText(this, spannableString, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter phone no. and message", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MyMessage();
                } else {
                    Toast.makeText(this, "You don't have required permission to send message!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void btn_call(View view) {
        //code later
        String Tempphoneno = txt_pNumber.getText().toString().trim();
        //String Templistview="9402836042";
        Intent intent = new Intent(com.example.videocallingapp.SendsmsActivity.this, PhonecallActivity.class);
        intent.putExtra("Phonenoclickvalue", Tempphoneno);//EXTRA_PHONE is declared at the above
        startActivity(intent);
        //startActivity(new Intent(SendsmsActivity.this,PhonecallActivity.class));
    }
}
