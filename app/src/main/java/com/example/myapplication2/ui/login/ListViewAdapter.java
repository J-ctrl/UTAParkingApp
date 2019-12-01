package com.example.myapplication2.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import com.example.myapplication2.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;
   AppCompatActivity Activity ;
    FirebaseDatabase database = FirebaseDatabase.getInstance(); //gets database instance
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); //gets authorization instance
    FirebaseUser currentUser = mAuth.getCurrentUser();


    DatabaseReference myRef = database.getReference("Users");


    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modellist);
    }


    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
        MaterialFavoriteButton FvButton;
        Button DirectionsBtn;

    }


    //Getter methods for size
    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;


        if (view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.actvity_row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDescription);
            holder.mIconIv = view.findViewById(R.id.mainIcon);
            holder.FvButton = view.findViewById(R.id.FavButton);
            holder.DirectionsBtn = view.findViewById(R.id.directions);


            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }

        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        holder.DirectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapBasicActivity.post(position);
                mContext.startActivity(new Intent(mContext,MapBasicActivity.class));
            }
        });
        /*This sets the favorite functions for each button in the view - when clicked this sets of the
        Snackbar notification at the bottom of the screen. This function will do future transactions with the
        Firebase Database and update the user's favorite list in the database */

        /*This is the favorite functions for each button in the view - when clicked this sets of the
        Snackbar notification at the bottom of the screen. */

        holder.FvButton.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite)
                        {
                            Snackbar.make(buttonView, "Added to Favorites", Snackbar.LENGTH_SHORT).show(); //displays the notification
                            System.out.println("Added to Favorites.");

                            //update the database by adding the lot to the favorite list !!!!
                        }
                        else
                        {
                            //unfavorite - delete parking lot from user's favorite's list in the database


                            //displays the notification
                            Snackbar.make(buttonView, "Added to Favorites", Snackbar.LENGTH_SHORT).show();

                            //Get the user's id. The favorited parking lots will be stored under the users unique id
                            String userUid = currentUser.getUid();

                            //check if the lot is already in the lot to prevent multiple favorites for same lot
                            DatabaseReference checkExists = myRef.child(userUid).child(modellist.get(position).getTitle());

                            //This will add the favorited parking lot if it does not exist in the database.
                            checkExists.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.hasChild(modellist.get(position).getTitle() ))
                                    {
                                        //gets id for storage
                                        String userUid = currentUser.getUid();

                                        //writes the user's name in the database
                                        myRef.child(userUid).child(modellist.get(position).getTitle()).setValue("");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
        );

        //set the results in imageview
        holder.mIconIv.setImageResource(modellist.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later

            }
        });



        return view;
    }

    //this method is in charge of the filtering settings for the search functionality

    public void filter(String charText){


        charText = charText.toLowerCase(Locale.getDefault());

        modellist.clear();

        if(charText.length()==0)
        {
            modellist.addAll(arrayList);

        }
        else
        {
            for(Model model : arrayList)
            {
                //this if statement filters by searching for the parking lot's title
                if (model.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    modellist.add(model);
                }

                //this if statement filters by searching through the description of each lot.
                if (model.getDesc().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    modellist.add(model);
                }
            }
        }

        notifyDataSetChanged();
    }
}