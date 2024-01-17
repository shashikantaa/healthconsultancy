package com.example.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class UsersActivity extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mSearchList;
    private DatabaseReference mUserDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUserDatabaseRef= FirebaseDatabase.getInstance().getReference("Users");
        //action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Users List");
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        mSearchList = (RecyclerView) findViewById(R.id.result_list);
        //mSearchList.setHasFixedSize(true);
        //mSearchList.setLayoutManager(new LinearLayoutManager(this));//required this other wise not displayed
        //or use the following alternative code
        mSearchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));//required this otherwise not displayed
        //for searching users using editText
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                {
                    searchUserUsingEditText(s.toString()); //calling search method defined below
                }
                else
                {
                    showAllUsers(); //calling showAllUsers method defined below when blank user name
                }
            }
        });
        //-------------------------------------------------------
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText=mSearchField.getText().toString().trim();
                firebaseUserSearch(searchText);//defined just below
            }
        });
    }
    //show All Users after searching calling automatically just after typing user name
    private void searchUserUsingEditText(String ss) {
        String s=toTitleCaseSearchname(ss); //converting ss to lower TitleCase
        Query query=mUserDatabaseRef.orderByChild("name").startAt(s).endAt(s+"\uf8ff"); // starting with passing string s and ends with some characters.
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)

        {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder viewHolder, int position, @NonNull Users users) {
                viewHolder.setDetails(users.getName(),users.getPhone(),users.getEmail(),users.getImage());
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_layout_users,parent,false);
                return new UsersViewHolder(v);
            }
        };
        mSearchList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    //converting user name to titlecase calling from above
    private static String toTitleCaseSearchname(String str) {
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

    //show All Users after searching calling from button search click
    private void firebaseUserSearch(String searchText) {
        Query query = mUserDatabaseRef.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");//for displaying all users after searching
        //Query query = FirebaseDatabase.getInstance().getReference().child("Users");//for displaying all users
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)

{
    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder viewHolder, int position, @NonNull Users users) {
        viewHolder.setDetails(users.getName(),users.getPhone(),users.getEmail(),users.getImage());
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_layout_users,parent,false);
        return new UsersViewHolder(v);
    }
};
    mSearchList.setAdapter(firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();
    }
    //show All Users at calling from onStart below
    private void showAllUsers() {
        //Query query = mUserDatabaseRef.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");//for displaying all users after searching
        //Query query = FirebaseDatabase.getInstance().getReference().child("Users");//for displaying all users
        //alternative code below
        //Query query = FirebaseDatabase.getInstance().getReference("Users");//for displaying all users
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        //.setQuery(query, Users.class)
                        .setQuery(mUserDatabaseRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)

        {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder viewHolder, int position, @NonNull Users users) {
                //calling the setDetails method of UsersViewHolder Class defined below through the object viewHolder for getting the data
                viewHolder.setDetails(users.getName(),users.getPhone(),users.getEmail(),users.getImage());
                viewHolder.parentLayoutUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (users.getName().toString().isEmpty() ==true || users.getPhone().toString().isEmpty() ==true || users.getImage().toString().isEmpty() ==true)
                        {
                            Toast.makeText(com.example.videocallingapp.UsersActivity.this, "Please complete your profile first!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent intent = new Intent(UsersActivity.this, AddroletouserActivity.class);
                            intent.putExtra("userId", users.getUid());
                            intent.putExtra("userName", users.getName());
                            intent.putExtra("userPhone", users.getPhone());
                            intent.putExtra("userEmail", users.getEmail());
                            //intent.putExtra("curUserRole",users.getUserRole());
                            intent.putExtra("userimageUrl", users.getImage());

                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_layout_users,parent,false);
                return new UsersViewHolder(v);
            }
        };
        mSearchList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    //View holder class
    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public RelativeLayout parentLayoutUser;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        //setDetails method below is called from inside onBindViewHolder method defined above by passing 4 parameters
    public void setDetails(String uName, String uPhone, String uEmail, String uImage){
    TextView user_name=(TextView)mView.findViewById(R.id.user_profile_name);
    TextView user_Phone=mView.findViewById(R.id.user_phone);
    TextView user_Email=mView.findViewById(R.id.user_email);
    ImageView user_profileImage=mView.findViewById(R.id.user_profile_image);
        parentLayoutUser=itemView.findViewById(R.id.activity_list_layout_users);
     user_name.setText(uName);
     user_Phone.setText(uPhone);
     user_Email.setText(uEmail);
     //Picasso.get().load(uImage).into(user_profileImage);
        try {
            //if image is available
            //get() is used for picasso 2.71828
            Picasso.get().load(uImage).into(user_profileImage);
        } catch (Exception e) {
            //if image is not available set default image
            Picasso.get().load(R.drawable.addpicture).into(user_profileImage);
        }
        }
    }
    @Override
    protected void onStart() {
        //Check if user is signed in or not on the start of App
        showAllUsers();//calling showAllUsers() method defined above
        super.onStart();
    }
}
