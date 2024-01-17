package com.example.videocallingapp;

import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatientAdapter extends RecyclerView.Adapter<com.example.videocallingapp.PatientAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Patient> patientList;
    ArrayList<Patient> arrayList;//Required for searching item in filter method defined below-------------------1
    String queryText="";//for text high lighting search text
    public PatientAdapter(Context context, List<Patient> Patients){//here faculties is table name created in firebase database
        mContext=context;
        patientList=Patients;
        this.arrayList=new ArrayList<Patient>();//Required for searching item in filter method defined below----2
        this.arrayList.addAll(patientList);//Required for searching item in filter method defined below---------3
    }
    //@NonNull
    @Override
    //public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    public com.example.videocallingapp.PatientAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.list_layout_patient,parent,false);
        return new com.example.videocallingapp.PatientAdapter.ImageViewHolder(v);
    }

    @Override
    //public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    public void onBindViewHolder(@NonNull final com.example.videocallingapp.PatientAdapter.ImageViewHolder holder, int position) {
        final Patient patient=patientList.get(position);
        //for highlighting Doctor name
        String dataText=patient.getmName().toString();

        if(queryText!=null && !queryText.isEmpty()) {
            int startPos = patient.getmName().toLowerCase().indexOf(queryText.toLowerCase());
            int endPos = startPos + queryText.length();
            if (startPos != -1) {
                Spannable spannable = new SpannableString(dataText);
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorStateList, null);
                spannable.setSpan(textAppearanceSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.text_view_facultyname.setText(spannable);

                holder.text_view_facultyid.setText("Patient Id:" + patient.getmPatientId().toString());
                //holder.text_view_facultyname.setText("Patient Name:" + patient.getmName().toString());
                holder.text_view_age.setText("Age:" + patient.getmAge().toString());
                holder.text_view_contactno.setText("Contact No:" + patient.getmContactno().toString());
                holder.text_view_subject.setText("Disease:" + patient.getmDisease().toString());
                holder.text_view_noofvisits.setText("No. of Visits:" + patient.getmNoofvisit().toString());
                holder.text_view_gender.setText("Gender:" + patient.getmGender().toString());
                holder.text_view_address.setText("Address:" + patient.getmAddress().toString());
                holder.text_view_dot.setText("Date of Treatment:" + patient.getmDot().toString());
                holder.text_view_medicine.setText("Medicines:" + patient.getmMedicine().toString());
                //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
                //for picasso 2.71828
                Picasso.get().load(patient.getmImageUrl().toString())
                        .fit()
                        .centerCrop()
                        //.centerInside()
                        .into(holder.imageView);
            } else
            {
                //holder.text_view_facultyname.setText("Patient Name:" + patient.getmName().toString());
                holder.text_view_facultyid.setText("Patient Id:" + patient.getmPatientId().toString());
                holder.text_view_facultyname.setText("Patient Name:" + patient.getmName().toString());
                holder.text_view_age.setText("Age:" + patient.getmAge().toString());
                holder.text_view_contactno.setText("Contact No:" + patient.getmContactno().toString());
                holder.text_view_subject.setText("Disease:" + patient.getmDisease().toString());
                holder.text_view_noofvisits.setText("No. of Visits:" + patient.getmNoofvisit().toString());
                holder.text_view_gender.setText("Gender:" + patient.getmGender().toString());
                holder.text_view_address.setText("Address:" + patient.getmAddress().toString());
                holder.text_view_dot.setText("Date of Treatment:" + patient.getmDot().toString());
                holder.text_view_medicine.setText("Medicines:" + patient.getmMedicine().toString());
                //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
                //for picasso 2.71828
                Picasso.get().load(patient.getmImageUrl().toString())
                        .fit()
                        .centerCrop()
                        //.centerInside()
                        .into(holder.imageView);
            }
        }
        else
            {
            holder.text_view_facultyid.setText("Patient Id:" + patient.getmPatientId().toString());
            holder.text_view_facultyname.setText("Patient Name:" + patient.getmName().toString());
            holder.text_view_age.setText("Age:" + patient.getmAge().toString());
            holder.text_view_contactno.setText("Contact No:" + patient.getmContactno().toString());
            holder.text_view_subject.setText("Disease:" + patient.getmDisease().toString());
            holder.text_view_noofvisits.setText("No. of Visits:" + patient.getmNoofvisit().toString());
            holder.text_view_gender.setText("Gender:" + patient.getmGender().toString());
            holder.text_view_address.setText("Address:" + patient.getmAddress().toString());
            holder.text_view_dot.setText("Date of Treatment:" + patient.getmDot().toString());
            holder.text_view_medicine.setText("Medicines:" + patient.getmMedicine().toString());
            //Picasso.with(mContext).load(uploadCurrent.getmImageUrl().toString())
            //for picasso 2.71828
            Picasso.get().load(patient.getmImageUrl().toString())
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
                Intent intent=new Intent(mContext,UpdatedeletepatientActivity.class);
                intent.putExtra("pkey",uploadCurrent.getmPatientId());
                intent.putExtra("pname",uploadCurrent.getmName());
                //intent.putExtra("page",uploadCurrent.getmAge());
                //intent.putExtra("pdot",uploadCurrent.getmDot());
                intent.putExtra("pcontactno",uploadCurrent.getmContactno());
                intent.putExtra("pdisease",uploadCurrent.getmDisease());
                intent.putExtra("pnovisit",uploadCurrent.getmNoofvisit());
                intent.putExtra("pgender",uploadCurrent.getmGender());
                intent.putExtra("pimageurl",uploadCurrent.getmImageUrl());
                //intent.putExtra("aname",holder.textViewName.getText().toString());
                mContext.startActivity(intent);
            }
        });*/
        //Single short Click on any part of the RecyclerView and goto another activity
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,PatientreportActivity.class);
                //Intent intent=new Intent(mContext,UpdatedeletepatientActivity.class);
                intent.putExtra("pkey",patient.getmPatientId());
                intent.putExtra("pname",patient.getmName());
                intent.putExtra("page",patient.getmAge());
                intent.putExtra("paddress",patient.getmAddress());
                intent.putExtra("pmedicine",patient.getmMedicine());
                intent.putExtra("pdot",patient.getmDot());
                intent.putExtra("pcontactno",patient.getmContactno());
                intent.putExtra("pdisease",patient.getmDisease());
                intent.putExtra("pnovisit",patient.getmNoofvisit());
                intent.putExtra("pgender",patient.getmGender());
                intent.putExtra("pimageurl",patient.getmImageUrl());
                //intent.putExtra("aname",holder.textViewName.getText().toString());
                mContext.startActivity(intent);
            }
        });
        //Single long Click on any part of the RecyclerView and goto AddhistoryActivity activity
        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(mContext,AddhistoryActivity.class);
                //Intent intent=new Intent(mContext,UpdatedeletepatientActivity.class);
                intent.putExtra("hpkey",patient.getmPatientId());
                intent.putExtra("hpname",patient.getmName());
                mContext.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
//this method filter is called from ShowpatientActivity.java file
    public void filterPatient(String s) {
         s=s.toLowerCase(Locale.getDefault());
        patientList.clear();
        if(s.length()==0)
        {
            patientList.addAll(arrayList);
        }
        else
        {
            queryText=s.toString();//for text high light
            for(Patient patient:arrayList)
            {
                if(patient.getmName().toLowerCase(Locale.getDefault()).contains(s))
                {
                    patientList.add(patient);
                }
            }
            if(patientList.size()>0)
            {
                //Toast.makeText(mContext, "Thanks Patient found!", Toast.LENGTH_LONG).show();
            }
            if(patientList.size()==0)
            {
                //Toast.makeText(mContext, "Sorry Patient not found!", Toast.LENGTH_LONG).show();
            }
        }
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView text_view_facultyid,text_view_facultyname,text_view_age,text_view_contactno,text_view_subject,text_view_noofvisits,text_view_gender,text_view_address,text_view_medicine,text_view_dot;
        public ImageView imageView;
        public CardView parentLayout;//CardView is layout type of layout resource file list_layout_patient.xml

        // public ImageViewHolder(@NonNull View itemView) {
        public ImageViewHolder(View itemView) {
            super(itemView);
            text_view_facultyid=itemView.findViewById(R.id.text_view_facultyid);
            text_view_facultyname=itemView.findViewById(R.id.text_view_facultyname);
            text_view_age=itemView.findViewById(R.id.text_view_age);
            text_view_contactno=itemView.findViewById(R.id.text_view_contactno);
            text_view_subject=itemView.findViewById(R.id.text_view_subject);
            text_view_noofvisits=itemView.findViewById(R.id.text_view_noofvisits);
            text_view_gender=itemView.findViewById(R.id.text_view_gender);
            text_view_dot=itemView.findViewById(R.id.text_view_dot);
            text_view_address=itemView.findViewById(R.id.text_view_address);
            text_view_medicine=itemView.findViewById(R.id.text_view_medicine);
            imageView=itemView.findViewById(R.id.image_view_upload);
            parentLayout=itemView.findViewById(R.id.activity_list_layout_patient);

        }
    }
}
