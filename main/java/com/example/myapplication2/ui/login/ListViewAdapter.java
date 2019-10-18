package com.example.myapplication2.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.myapplication2.R;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;


public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;


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
    public View getView(int position, View view, ViewGroup parent) {
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

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }

        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());

        /*This sets the favorite functions for each button in the view - when clicked this sets of the
        Snackbar notification at the bottom of the screen. This function will do future transactions with the
        Firebase Database and update the user's favorite list in the database */
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