package com.example.myapplication2.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Model> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_search);

    /*
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //gets database instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); //gets authorization instance
        FirebaseUser currentUser = mAuth.getCurrentUser(); //gets the current user

        String UserId = currentUser.getUid();

        DatabaseReference myRef = database.getReference("User Favorites");

        myRef.child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());


                ArrayList<Integer> iconsfav = new ArrayList<>(); //create an array for the icons using the size of the children in the database favorited by user
                ArrayList<String> titlefav = new ArrayList<>();

                //using the number of
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++)
                {
                    iconsfav.add(R.drawable.questionmark);
                }

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Model model = new Model();
                    arrayList.add(model);

                    <YourClass> post = postSnapshot.getValue(<YourClass>.class);
                    Log.e("Get Data", post.<YourMethod>());

                    Model model = new Model();
                    arrayList.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());

            }
        });
        */

        /*  1. get the list of favorited parking spots from the favorites database using the current users UUID
                a. match the titles from the databases with the list of parking lot titles available
                b. make a count of how many favorited lots there are. This will decide the size of the array and loop size to put the icons in.
            2. import all the data from the firebase database into an ArrayList of Model objects using a for loop
            3. send to an array adapter and ListViewAdapter

         */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

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
