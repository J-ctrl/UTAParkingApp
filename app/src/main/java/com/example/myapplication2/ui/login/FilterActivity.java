package com.example.myapplication2.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.EventsList;
import com.example.myapplication2.R;
import com.example.myapplication2.Reminder;

import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class FilterActivity extends AppCompatActivity{
    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Model> arrayList = new ArrayList<>();
   public String userStatus;

    FirebaseDatabase database = FirebaseDatabase.getInstance(); //gets database instance
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); //gets authorization instance
    FirebaseUser currentUser = mAuth.getCurrentUser();
    DatabaseReference myRef = database.getReference("Users");




    //OnCreate is in charge of setting all the titles, description, and pictures for each parking lot.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_search);
        //Array for each parking lot's name/title
        title = new String[]{"Lot F1",
                "Lot F4",
                "Lot F5",
                "Lot F9",
                "Lot F10",
                "Lot F11",
                "Lot F12",
                "Lot F15",
                "Lot F17",
                "Lot 14",
                "Lot 24",
                "Lot 25",
                "Lot 26",
                "Lot 27",
                "Lot 28",
                "Lot 30",
                "Lot 34",
                "Lot 35",
                "Lot 36",
                "Lot 38",
                "Lot 47",
                "Lot 49",
                "Lot 50",
                "Lot 51",
                "Lot 52",
                "Lot 53",
                "Lot 55",
                "Lot 56",
                "Lot AO",
                "Lot GR",
                "Lot MR",
                "Lot TS",
                "Lot UV"};

        //Array for each parking lot's description
        description = new String[]{"Near the Gilstrap Athletic Center.\nParking: Faculty",
                "Near the Environmental Health & Safety Building.\nParking: Faculty",
                "Near the Swift Center.\nParking: Faculty",
                "Near Davis Hall.\nParking: Faculty",
                "Near the Pickard Hall. \nParking: Faculty",
                "Near University Center. \nParking: Faculty",
                "Near ERB.\nParking: Faculty",
                "Near the Social Work Complex.\nParking: Faculty",
                "Near Continuing Education Building\nParking: Faculty",
                "Near the ERB",
                "Near Military Veteran Services.\nParking: General",
                "Near The Intramural Fields.\nParking: General",
                "Near Civil Engineering Lab.\nParking: General",
                "Near Parking And Transportation Services.\nParking: General",
                "Near Swift Center.\nParking: General",
                "Near University Village.\nParking: Student",
                "Near the Nanotech Building.\nParking: Student",
                "Near the Social Work Complex.\nParking: Student",
                "Near The ERB.\nParking: Student",
                "Near the UTA Bookstore.\nParking: Faculty",
                "Near the SEIR Building.\nParking: Student",
                "Across the SEIR Building.\nParking: Student",
                "Across SEIR Building Doug Russel & S West St.\nParking: Student",
                "Across SEIR Building Doug Russel & S West St.\nParking: Student",
                "Across the SEIR Building.\nParking: Student",
                "Near Continuing Education.\nParking: Student",
                "Near Aerodynamics Research Building.\nParking: General",
                "Near the Heights on Pecan.\nParking: Student",
                "Near Arbor Oaks.\nParking: General",
                "Near Greek Row.\nParking: General",
                "Near Meadow Run.\nParking: General",
                "Near Texas Hall.\nParking: Faculty",
                "Near University Village.\nParking: General"};

        icon = new int[]{R.drawable.f1, R.drawable.f4, R.drawable.f5, R.drawable.f9,
                R.drawable.f10, R.drawable.f11,R.drawable.f12,R.drawable.f15,
                R.drawable.f17, R.drawable.p14,R.drawable.p24,R.drawable.p25,
                R.drawable.p26, R.drawable.p27, R.drawable.p28,R.drawable.p30,
                R.drawable.p34, R.drawable.p35,R.drawable.p36,R.drawable.p3839,
                R.drawable.p47, R.drawable.p49, R.drawable.p5051,R.drawable.p5051,
                R.drawable.p52, R.drawable.p53, R.drawable.p55,R.drawable.p56,
                R.drawable.ao, R.drawable.gr,R.drawable.mr, R.drawable.ts,R.drawable.uv};

        listView = findViewById(R.id.listView);

        //This loop creates the list of parking lots based on the length of the title array
        for (int i = 0; i < title.length; i++)
        {
            Model model = new Model(title[i], description[i], icon[i]);
            //Bind all strings in an array
            arrayList.add(model);
        }

        /*
            If the current user's role is student (check from database value) add only those with "Student" or "General" in the description to the arrayList
            else the current user's role is a faculty member (check from database value) add all the parking lots to the array.

            This will display only the student and general parking lots to the student. While the faculty member sees all the lots.
        */


        //This will be used to retrieve the role value of the user. It determines which parking lots are displayed.
        // The values "Student" or "Faculty" will be returned and stored in the string userStatus
        String userUid = currentUser.getUid();
        myRef.child(userUid).child("Role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userStatus = (String) dataSnapshot.getValue(); //grabs the Role value from the database either Student or Faculty will be stored in Userstatus
                System.out.println(userStatus); //Reads it out too.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            for (int i = 0; i < title.length; i++)
            {
                Model model = new Model(title[i], description[i], icon[i]);
                //Bind all strings in an array
                arrayList.add(model);
            }

        //pass results to listViewAdapter class
        adapter = new ListViewAdapter(this, arrayList);
        //bind the adapter to the listview
        listView.setAdapter(adapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_favorites)
        {

            //future functionality here
            openFavoriteListActivity();
            return true;
        }

        if(id == R.id.action_reminder){
            Intent myIntent = new Intent(this, Reminder.class);
            startActivity(myIntent);
        }


        if(id == R.id.action_eventsList){
            Intent myIntent = new Intent(this, EventsList.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFavoriteListActivity() {
        Intent intent = new Intent(this, FavoriteListActivity.class);
        startActivity(intent);

    }
    public  void GetDirections(){
        Intent intent = new Intent(this,MapBasicActivity.class);
        startActivity(intent);
    }

}