package com.example.videocallingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatmessageAdapter extends ArrayAdapter<Chatmessage> {
    private Activity context;
    private List<Chatmessage> chatmessageList;
    //FirebaseAuth firebaseAuth;
    //FirebaseUser user;
    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseReference;
    //init firebase
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference UsersReference = firebaseDatabase.getReference("Users");
    DatabaseReference ChatmessageReference = firebaseDatabase.getReference("Chatmessages").child("email");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       public ChatmessageAdapter(Activity context, List<Chatmessage> chatmessageList) {
        super(context, R.layout.list_layout_chatmessage, chatmessageList);
        this.context = context;
        this.chatmessageList = chatmessageList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewChatItem = inflater.inflate(R.layout.list_layout_chatmessage, null, true);

        //TextView textViewId=(TextView)listViewItem.findViewById(R.id.textViewId);
        TextView textViewName = (TextView) listViewChatItem.findViewById(R.id.name_user);
        TextView textViewText = (TextView) listViewChatItem.findViewById(R.id.message_user);
        TextView textViewTime = (TextView) listViewChatItem.findViewById(R.id.time_user);
        CircleImageView userCircleIv=(CircleImageView)listViewChatItem.findViewById(R.id.CircularIv_user);

        Chatmessage chatmessage = chatmessageList.get(position);
        //User user=chatroomList.get(position);

        //textViewId.setText(history.getmHistoryId());
        textViewName.setText(chatmessage.getEmail());
        textViewText.setText(chatmessage.getMessageText());
        textViewTime.setText(chatmessage.getMessageTime());

       // if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            //String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            //Query query = UsersReference.orderByChild("email").equalTo(user.getEmail());
            //UsersReference.addValueEventListener(new ValueEventListener() {

            //String emails=ChatmessageReference.child("email").toString();
            Query query = UsersReference.orderByChild("email").equalTo(chatmessage.getEmail());
            if(query!=null){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get data
                        //String name = "" + ds.child("name").getValue();
                        //String email = "" + ds.child("email").getValue();
                        //String phone = "" + ds.child("phone").getValue();
                        String image = "" + ds.child("image").getValue();
                        //set data
                        //nameTv.setText(name);
                        //emailTv.setText(email);
                        //phoneTv.setText(phone);
                        try {
                            //if image is available
                            //get() is used for picasso 2.71828
                            Picasso.get().load(image).into(userCircleIv);
                        } catch (Exception e) {
                            //if image is not available set default image
                            Picasso.get().load(R.drawable.ic_add_photo).into(userCircleIv);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //if image is not available set default image
                    Picasso.get().load(R.drawable.ic_add_photo).into(userCircleIv);
                }
            });

        } else {
            Toast.makeText(context, "User profile not found!.",
                    Toast.LENGTH_SHORT).show();
        }

        return listViewChatItem;
    }
}
