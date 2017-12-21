package com.tlc.laque.redcarpet.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.parties.ListAdapterParties;
import com.tlc.laque.redcarpet.parties.ListPartiesActivity;
import com.tlc.laque.redcarpet.parties.PartiesActivity;
import com.tlc.laque.redcarpet.parties.Party;

import java.util.ArrayList;

public class ListUsers extends MainActivity {
    ArrayAdapter adapterList;
    private DatabaseReference mDatabase;
    ListView listViewUsers;
    ArrayList<User> users;
    String urlId;
    private Button buttonFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_list_users, contentFrameLayout);
        users = new ArrayList<>();
        listViewUsers = findViewById(R.id.listViewUsers);

        buttonFriends = findViewById(R.id.buttonFriendsRequest);
        Bundle extras = getIntent().getExtras();

        DataBaseRead dr = new DataBaseRead();
        String myId = dr.getUserId();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query;
        if(extras == null){
            setTitle("Friends");
            query = mDatabase.child("users").orderByChild("friends/"+""+myId).equalTo(myId);
        }else{
            String path = extras.getString("path");
            if(path == null){
                setTitle("People attending the party");
                urlId = extras.getString("idParty");
                query= mDatabase.child("users").orderByChild("partyAttending/"+""+urlId).equalTo(""+urlId);
                buttonFriends.setVisibility(View.GONE);}
            else{
                setTitle("Friends Request");
                query= mDatabase.child("users").orderByChild("friendSent/"+""+myId).equalTo(""+myId);
            }
        }




        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}
                else {
                    DataBaseRead dbR = new DataBaseRead();
                    users = dbR.getAllUsers(dataSnapshot);
                    ListAdapterUser LicustomAdapter = new ListAdapterUser(ListUsers.this, R.layout.adapter_list_user, users);
                    listViewUsers.setAdapter(LicustomAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ListUsers.this, UserActivity.class);
                intent.putExtra("idUser",  users.get(position).getKey());
                startActivity(intent);
            }
        });


        //LIST VIEW CLICK
     /*   listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ListUsers.this, PartiesActivity.class);
                intent.putExtra("idParty",  parties.get(position).getKey());
                startActivity(intent);
            }
        }); */
    }


}
