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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewUserAdapter extends BaseAdapter {
    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Users> userslist;
    ArrayList<Users>arrayList;
    String queryText="";//for text high lighting search text
    //constructor
    public ListViewUserAdapter(Context context, List<Users> userslist) {
        mContext = context;
        this.userslist = userslist;
        inflater=LayoutInflater.from(mContext);
        this.arrayList=new ArrayList<Users>();
        this.arrayList.addAll(userslist);
    }
    //ViewHolder
    public class ViewHolder{

        TextView nameTv,phoneTv,emailTv;
        ImageView userImageIv;
    }

    @Override
    public int getCount() {
        return userslist.size();
    }

    @Override
    public Object getItem(int position) {
        return userslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view==null)
        {
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.list_layout_users,null);
            //locate the views in list_layout_users
            holder.nameTv=view.findViewById(R.id.user_profile_name);
            holder.phoneTv=view.findViewById(R.id.user_phone);
            holder.emailTv=view.findViewById(R.id.user_email);
            holder.userImageIv=view.findViewById(R.id.user_profile_image);
            view.setTag(holder);
        }
        else
            {
                holder=(ViewHolder)view.getTag();
            }
        //for highlighting Category name
        String dataText=userslist.get(position).getName().toString();
        if(queryText!=null && !queryText.isEmpty()) {
            int startPos = userslist.get(position).getName().toLowerCase().indexOf(queryText.toLowerCase());
            int endPos = startPos + queryText.length();
            if (startPos != -1) {
                Spannable spannable = new SpannableString(dataText);
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorStateList, null);
                spannable.setSpan(textAppearanceSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.nameTv.setText(spannable);//applying high light to the category name
                //set the results into textviews and imageview
                //holder.nameTv.setText(userslist.get(position).getName());
                holder.phoneTv.setText(userslist.get(position).getPhone());
                holder.emailTv.setText(userslist.get(position).getEmail());

                //Picasso.get().load(uImage).into(user_profileImage);
                try {
                    //if image is available
                    //get() is used for picasso 2.71828
                    Picasso.get().load(userslist.get(position).getImage()).into(holder.userImageIv);
                } catch (Exception e) {
                    //if image is not available set default image
                    Picasso.get().load(R.drawable.addpicture).into(holder.userImageIv);
                }
            }
            else
                {
                    holder.nameTv.setText(userslist.get(position).getName());
                    holder.phoneTv.setText(userslist.get(position).getPhone());
                    holder.emailTv.setText(userslist.get(position).getEmail());

                    //Picasso.get().load(uImage).into(user_profileImage);
                    try {
                        //if image is available
                        //get() is used for picasso 2.71828
                        Picasso.get().load(userslist.get(position).getImage()).into(holder.userImageIv);
                    } catch (Exception e) {
                        //if image is not available set default image
                        Picasso.get().load(R.drawable.addpicture).into(holder.userImageIv);
                    }
            }

        }
        else
            {
                holder.nameTv.setText(userslist.get(position).getName());
                holder.phoneTv.setText(userslist.get(position).getPhone());
                holder.emailTv.setText(userslist.get(position).getEmail());

                //Picasso.get().load(uImage).into(user_profileImage);
                try {
                    //if image is available
                    //get() is used for picasso 2.71828
                    Picasso.get().load(userslist.get(position).getImage()).into(holder.userImageIv);
                } catch (Exception e) {
                    //if image is not available set default image
                    Picasso.get().load(R.drawable.addpicture).into(holder.userImageIv);
                }
        }
                //listView item click
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (userslist.get(position).getName().toString().isEmpty() ==true || userslist.get(position).getPhone().toString().isEmpty() ==true || userslist.get(position).getImage().toString().isEmpty() ==true)
               {
                   Toast.makeText(mContext, "Please complete your profile first!", Toast.LENGTH_LONG).show();
               }
               else
               {
                   Intent intent = new Intent(mContext, AddroletouserActivity.class);
                   intent.putExtra("userId", userslist.get(position).getUid());
                   intent.putExtra("userName", userslist.get(position).getName());
                   intent.putExtra("userPhone", userslist.get(position).getPhone());
                   intent.putExtra("userEmail", userslist.get(position).getEmail());
                   //intent.putExtra("curUserRole",users.getUserRole());
                   intent.putExtra("userimageUrl", userslist.get(position).getImage());

                   mContext.startActivity(intent);
               }
           }
       });
        return view;
    }
    //searching users and this method is called from ShowusersActivity.java
    public void filterUsers(String s)
    {
    s=s.toLowerCase(Locale.getDefault());
    userslist.clear();
    if(s.length()==0)
    {
        userslist.addAll(arrayList);
    }
    else
        {
            queryText=s.toString();//for text high light
            for(Users users:arrayList)
        {
        if(users.getName().toLowerCase(Locale.getDefault()).contains(s))
            {
            userslist.add(users);
            }
        }
        }
        notifyDataSetChanged();
    }
}
