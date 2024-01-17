package com.example.videocallingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorAdapter extends RecyclerView.Adapter<com.example.videocallingapp.DoctorAdapter.ImageViewHolder> {
    DatabaseReference mDatabaseRef;//1. mDatabaseRef is the name of the firebase database reference
    private Context mContext;
    private List<Doctor> doctorList;
    ArrayList<Doctor> arrayList;//Required for searching item in filter method defined below-------------------1
    String queryText="";//for text high lighting search text
    public DoctorAdapter(Context context, List<Doctor> Doctors){//here faculties is table name created in firebase database
        mContext=context;
        doctorList=Doctors;
        this.arrayList=new ArrayList<Doctor>();//Required for searching item in filter method defined below----2
        this.arrayList.addAll(doctorList);//Required for searching item in filter method defined below---------3
    }
    //@NonNull
    @Override
    //public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    public com.example.videocallingapp.DoctorAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.list_layout_doctor,parent,false);
        return new com.example.videocallingapp.DoctorAdapter.ImageViewHolder(v);
    }

    @Override
    //public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    public void onBindViewHolder(@NonNull final com.example.videocallingapp.DoctorAdapter.ImageViewHolder holder, int position) {

        final Doctor doctor=doctorList.get(position);
  //for highlighting Doctor name
        String dataText=doctor.getmName().toString();

if(queryText!=null && !queryText.isEmpty()) {
    int startPos = doctor.getmName().toLowerCase().indexOf(queryText.toLowerCase());
    int endPos = startPos + queryText.length();
    if (startPos != -1) {
        Spannable spannable = new SpannableString(dataText);
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorStateList, null);
        spannable.setSpan(textAppearanceSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.text_view_facultyname.setText(spannable);

        holder.text_view_facultyid.setText("Doctor Id:"+doctor.getmDoctorId().toString());
        //holder.text_view_facultyname.setText("Doctor Name:" + doctor.getmName().toString());
        holder.text_view_age.setText("Age:" + doctor.getmAge().toString());
        holder.text_view_contactno.setText("Contact No:" + doctor.getmContactno().toString());
        holder.text_view_subject.setText("Department:" + doctor.mDepartment.toString());
        holder.text_view_gender.setText("Gender:" + doctor.getmGender().toString());
        holder.text_view_address.setText("Address:" + doctor.getmAddress().toString());
        holder.text_view_doj.setText("Date of Joining:" + doctor.getmDoj().toString());
        //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
        //for picasso 2.71828
        Picasso.get().load(doctor.getmImageUrl().toString())
                .fit()
                .centerCrop()
                //.centerInside()
                .into(holder.imageView);
    } else
        {
            holder.text_view_facultyid.setText("Doctor Id:"+doctor.getmDoctorId().toString());
            holder.text_view_facultyname.setText("Doctor Name:" + doctor.getmName().toString());
            holder.text_view_age.setText("Age:" + doctor.getmAge().toString());
            holder.text_view_contactno.setText("Contact No:" + doctor.getmContactno().toString());
            holder.text_view_subject.setText("Department:" + doctor.mDepartment.toString());
            holder.text_view_gender.setText("Gender:" + doctor.getmGender().toString());
            holder.text_view_address.setText("Address:" + doctor.getmAddress().toString());
            holder.text_view_doj.setText("Date of Joining:" + doctor.getmDoj().toString());
            //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
            //for picasso 2.71828
            Picasso.get().load(doctor.getmImageUrl().toString())
                    .fit()
                    .centerCrop()
                    //.centerInside()
                    .into(holder.imageView);
        }
}
else {
    holder.text_view_facultyid.setText("Doctor Id:"+doctor.getmDoctorId().toString());
    holder.text_view_facultyname.setText("Doctor Name:" + doctor.getmName().toString());
    holder.text_view_age.setText("Age:" + doctor.getmAge().toString());
    holder.text_view_contactno.setText("Contact No:" + doctor.getmContactno().toString());
    holder.text_view_subject.setText("Department:" + doctor.mDepartment.toString());
    holder.text_view_gender.setText("Gender:" + doctor.getmGender().toString());
    holder.text_view_address.setText("Address:" + doctor.getmAddress().toString());
    holder.text_view_doj.setText("Date of Joining:" + doctor.getmDoj().toString());
    //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
    //for picasso 2.71828
    Picasso.get().load(doctor.getmImageUrl().toString())
            .fit()
            .centerCrop()
            //.centerInside()
            .into(holder.imageView);
}
       /*
        //Click on image and goto another activity
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,UpdatedeletedoctorActivity.class);
                intent.putExtra("key",uploadCurrent.getmDoctorId());
                intent.putExtra("aname",uploadCurrent.getmName());
                intent.putExtra("acontactno",uploadCurrent.getmContactno());
                intent.putExtra("adept",uploadCurrent.getmDepartment());
                intent.putExtra("agender",uploadCurrent.getmGender());
                intent.putExtra("imageurl",uploadCurrent.getmImageUrl());
                //intent.putExtra("aname",holder.textViewName.getText().toString());
                mContext.startActivity(intent);
            }
        });
        */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
          if (user != null && !(email.equals("lalit@gmail.com"))) {

              holder.text_view_delete.setEnabled(false);
              holder.text_view_delete.setVisibility(View.INVISIBLE);
              //Uri photoUrl = user.getPhotoUrl();
            //Query query = FirebaseDatabase.getInstance().getReference().child("Users"); //for displaying all users
            //Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());//for displaying only for current user
        }
        holder.text_view_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor.getmDoctorId());
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext,"Record Deleted Successfully...", Toast.LENGTH_LONG).show();
                            //starting the DoctorActivity.clas from inside adapter itself to refresh the list
                              mContext.startActivity(new Intent(mContext,DoctorActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,"Record Deletion failed...", Toast.LENGTH_LONG).show();
                            }
                        });
                     }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
        //Click on any part of the RecyclerView and goto another activity
        holder.parentLayoutdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,UpdatedeletedoctorActivity.class);
                intent.putExtra("dkey",doctor.getmDoctorId());
                intent.putExtra("dname",doctor.getmName());
                intent.putExtra("dcontactno",doctor.getmContactno());
                intent.putExtra("ddept",doctor.getmDepartment());
                intent.putExtra("dgender",doctor.getmGender());
                intent.putExtra("dimageurl",doctor.getmImageUrl());
                //intent.putExtra("aname",holder.textViewName.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
    //this method filter is called from DoctorActivity.java file ---------------------------
    public void filterDoctor(String s) {
        s=s.toLowerCase(Locale.getDefault());
        doctorList.clear();
        if(s.length()==0)
        {
            doctorList.addAll(arrayList);
        }
        else
        {
            queryText=s.toString();//for text high light
            for(Doctor doctor:arrayList)
            {
                if(doctor.getmName().toLowerCase(Locale.getDefault()).contains(s))
                {
                    doctorList.add(doctor);
                }
            }
            if(doctorList.size()>0)
            {
                //Toast.makeText(mContext, "Thanks Doctor found!", Toast.LENGTH_LONG).show();
            }
            if(doctorList.size()==0)
            {
                //Toast.makeText(mContext, "Sorry Doctor not found!", Toast.LENGTH_LONG).show();
            }
        }
        notifyDataSetChanged();
    }

    /*
//----------------------------------------------------------------------
    private Filter facultyfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Faculty>filterlist=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filterlist.addAll(facultyList);
            }
            else
            {
                String pattrn=constraint.toString().toUpperCase().trim();
                for(Faculty item:facultyList){
                    if(item.getmName().toUpperCase().contains(pattrn)){ //search by faculty name with field name mName defined in the Faculty class
                        filterlist.add(item);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            facultyList.clear();
            facultyList.addAll((List)results.values);
            notifyDataSetChanged();
            }
    };
    @Override
    public Filter getFilter() {
        return facultyfilter;
    }
//---------------------------------------------------------------------------------
*/
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView text_view_facultyid,text_view_facultyname,text_view_age,text_view_contactno,text_view_subject,text_view_gender,text_view_address,text_view_doj,text_view_delete;
        public ImageView imageView;
        public CardView parentLayoutdoctor;//CardView is layout type of layout resource file list_layout_patient.xml
        // public ImageViewHolder(@NonNull View itemView) {
        public ImageViewHolder(View itemView) {
            super(itemView);
            text_view_facultyid=itemView.findViewById(R.id.text_view_facultyid);
            text_view_facultyname=itemView.findViewById(R.id.text_view_facultyname);
            text_view_age=itemView.findViewById(R.id.text_view_age);
            text_view_contactno=itemView.findViewById(R.id.text_view_contactno);
            text_view_subject=itemView.findViewById(R.id.text_view_subject);
            text_view_gender=itemView.findViewById(R.id.text_view_gender);
            text_view_doj=itemView.findViewById(R.id.text_view_doj);
            text_view_address=itemView.findViewById(R.id.text_view_address);
            imageView=itemView.findViewById(R.id.image_view_upload);
            text_view_delete=itemView.findViewById(R.id.text_view_delete);
            parentLayoutdoctor=itemView.findViewById(R.id.activity_list_layout_doctor);

        }
    }
}
