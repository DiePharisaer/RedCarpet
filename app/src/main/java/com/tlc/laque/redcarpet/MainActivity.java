package com.tlc.laque.redcarpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.parties.CreateNewPartyActivity;
import com.tlc.laque.redcarpet.parties.ListAdapterParties;
import com.tlc.laque.redcarpet.parties.ListPartiesActivity;
import com.tlc.laque.redcarpet.parties.PartiesActivity;
import com.tlc.laque.redcarpet.parties.Party;
import com.tlc.laque.redcarpet.settings.UserSettingActivity;
import com.tlc.laque.redcarpet.users.ListUsers;

import java.text.ParseException;
import java.util.ArrayList;

/**
 *  MainActivity CLASS
 *
 *
 */
public class MainActivity extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private DatabaseReference mDatabase;
    ListView listViewParties;
    ArrayList<Party> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      //  toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);

        listViewParties = findViewById(R.id.listViewMain);

        readAllParty();

        listViewParties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, PartiesActivity.class);
                intent.putExtra("pathParty", "Parties/" + parties.get(position).getKey());
                intent.putExtra("idParty", parties.get(position).getKey());
                startActivity(intent);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent anIntent;
                switch (item.getItemId()) {

                    case R.id.nav_parties:
                        anIntent = new Intent(getApplicationContext(), ListPartiesActivity.class);
                        anIntent.putExtra("path",  "Parties");
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_friends:
                        anIntent = new Intent(getApplicationContext(), ListUsers.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        anIntent = new Intent(getApplicationContext(), UserSettingActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_create_party:
                        anIntent = new Intent(getApplicationContext(), CreateNewPartyActivity.class);
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_attending_party:
                        anIntent = new Intent(getApplicationContext(), ListPartiesActivity.class);
                        anIntent.putExtra("path",  "administration/attendingParty");
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_parties_attending:
                        anIntent = new Intent(getApplicationContext(), ListPartiesActivity.class);
                        anIntent.putExtra("path",  "Parties");
                        anIntent.putExtra("attending", "true");
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_vote_party:
                        anIntent = new Intent(getApplicationContext(), ListPartiesActivity.class);
                        anIntent.putExtra("path",  "PartiesFinished");
                        anIntent.putExtra("attending", "attended");
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void readAllParty(){
        //Reading from DataBase
        mDatabase = FirebaseDatabase.getInstance().getReference("Parties");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}
                else {

                    DataBaseRead dbR = new DataBaseRead();
                    try {
                        parties = dbR.getAllParties(dataSnapshot);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ListAdapterParties LicustomAdapter = new ListAdapterParties(MainActivity.this, R.layout.adapter_party_list, parties);
                    listViewParties.setAdapter(LicustomAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.testAction);
        getData(menuItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.testAction){
            Intent anIntent;
            anIntent = new Intent(getApplicationContext(), ListUsers.class);
            anIntent.putExtra("path",  "users");
            startActivity(anIntent);
            drawerLayout.closeDrawers();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(final MenuItem menuItem){
        DataBaseRead dr = new DataBaseRead();
        String userID = dr.getUserId();
        menuItem.setIcon(buildCounterDrawable( 0 , R.drawable.ic_menu_gallery));


        mDatabase = FirebaseDatabase.getInstance().getReference("users"+"/"+userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}                    //If user exist show the old informations
                else {
                    DataBaseRead dr = new DataBaseRead();
                    //user = getData(dataSnapshot, userId);

                    menuItem.setIcon(buildCounterDrawable( dr.getUser(dataSnapshot).getNumberFriendsRequestS(), R.drawable.ic_menu_gallery));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
