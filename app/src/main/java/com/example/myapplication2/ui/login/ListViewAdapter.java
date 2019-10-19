package com.example.myapplication2.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.myapplication2.R;


class ListViewAdapter extends BaseAdapter {

    //variables
    private final Context mContext;
    private final LayoutInflater inflater;
    private final List<Model> modellist;
    private final ArrayList<Model> arrayList;


    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(modellist);
    }


    class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
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

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)view.getTag();
        }

        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());

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