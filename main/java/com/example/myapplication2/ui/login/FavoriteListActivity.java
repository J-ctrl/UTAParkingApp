package com.example.myapplication2.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.myapplication2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {

    Context mContext = this;
    ListView listView;
    ListViewAdapter adapter;
    ArrayList<Model> arrayList = new ArrayList<>();
    ArrayList<String> LotNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_search);

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //gets database instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); //gets authorization instance - used to get the current logged user

        FirebaseUser currentUser = mAuth.getCurrentUser(); //gets the current user

        String UserId = currentUser.getUid(); //get user's unique user id. This will be used to reference the user's favorite list in the DB.
        DatabaseReference myRef = database.getReference("User Favorites"); //gets the path to where the favorites are stored in the DB

        myRef = myRef.child(UserId); //Under the section "User Favorites" in the DB, jump to the current logged in user's section and store the reference

        //This code block gets the titles of the parking lots saved under the User's favorite list and stores it into the LotNameList arrayList
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        //for loop that stores the names of the children (Lot Names) into the arrayList
                        for(DataSnapshot dsp : dataSnapshot.getChildren())
                        {
                            LotNameList.add(String.valueOf(dsp.getKey()));
                        }

                        //gets the list view from xml files
                        listView = findViewById(R.id.listView);

                        //This loop creates the list of parking lots based on the length of the title array
                        for (String l : LotNameList)
                        {
                            Model model = new Model(l, "", R.drawable.questionmark);

                            //bind all strings to arrayList
                            arrayList.add(model);
                        }

                        adapter = new ListViewAdapter(mContext, arrayList);

                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {

                if(TextUtils.isEmpty(s))
                {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else
                {
                    adapter.filter(s);
                }

                return true;
            }
        });

        return true;
    }

}
