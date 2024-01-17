package com.example.videocallingapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddvideoActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 1;
    private Button choose_btn,upload_btn;
    private VideoView video_view;
    private Uri videoUri;
    MediaController mediaController;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private EditText video_name;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvideo);

        video_view=findViewById(R.id.video_view);
        video_name=findViewById(R.id.video_name);
        choose_btn=findViewById(R.id.choose_btn);
        upload_btn=findViewById(R.id.upload_btn);
        progress_bar=findViewById(R.id.progress_bar);
        mediaController=new MediaController(this);
        mStorageRef= FirebaseStorage.getInstance().getReference("Videos");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Videos");
        video_view.setMediaController(mediaController);
        mediaController.setAnchorView(video_view);
        video_view.start();
        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVideo();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });
    }
    private void ChooseVideo() {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            videoUri= data.getData();
            video_view.setVideoURI(videoUri);
        }
    }
    // for uploading video file extension
    private String getFileExtension(Uri videoUri){ //calling from UploadVideo method below
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(videoUri));
    }
    //upload method begins here
    private void UploadVideo() {
        progress_bar.setVisibility(View.VISIBLE);
        if(videoUri!=null){
            StorageReference reference=mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(videoUri));
            reference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progress_bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Video Uploaded Successfully...", Toast.LENGTH_LONG).show();
                         Video video=new Video(video_name.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String videoId=mDatabaseRef.push().getKey();
                            mDatabaseRef.child(videoId).setValue(video);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(),"No Video Selected !", Toast.LENGTH_LONG).show();
        }
    }

}
