package com.example.mapselection;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Model> arrayList = new ArrayList<Model>();


    //OnCreate is in charge of setting all the titles, description, and pictures for each parking lot.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Array for each parking lot's name/title
        title = new String[]{"Parking Lot #1", "Parking Lot #2", "Parking Lot #3", "Parking Lot #4", "Parking Lot #5", "Parking Lot #6", "Parking Lot #7"};

        //Array for each parking lot's description
        description = new String[]{"parking lot detail - near the ERB", "parking lot detail - near the MAC building", "parking lot detail - near the SIER building", "parking lot detail - near college park", "parking lot detail - near the Physical Education building", "parking lot detail #F", "parking lot detail #G"};

        //Array for each parking lot's description
        icon = new int[]{R.drawable.questionmark, R.drawable.questionmark, R.drawable.questionmark, R.drawable.questionmark, R.drawable.questionmark, R.drawable.questionmark, R.drawable.questionmark};


        listView = findViewById(R.id.listView);

        //This loop creates the list of parking lots based on the length of the title array
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

        if(id == R.id.action_settings)
        {
            //future functionality here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


/*Design row of listview*/
/*adding menu to add searchview in action bar*/
/*add model class*/
/*add adapter class*/
/*add images in drawable*/

