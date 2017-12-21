package com.tlc.laque.redcarpet.parties;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
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
import com.tlc.laque.redcarpet.users.ListAdapterUser;
import com.tlc.laque.redcarpet.users.ListUsers;

import java.util.ArrayList;

//Showing the listView of the Parties


public class ListPartiesActivity extends MainActivity {
    ArrayAdapter adapterList;
    private DatabaseReference mDatabase;
    private String path;
    ListView listViewParties;
    ArrayList<Party> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_list_parties, contentFrameLayout);
        parties = new ArrayList<Party>();
        listViewParties = findViewById(R.id.listView_parties);


        Bundle extras = getIntent().getExtras();
        path = extras.getString("path");
        String attending = extras.getString("attending");
        if (attending == null) {
            readAllParty();
        } else {
            readAttendingParty();
        }


        //LIST VIEW CLICK
        listViewParties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ListPartiesActivity.this, PartiesActivity.class);
                intent.putExtra("pathParty", path + "/" + parties.get(position).getKey());
                intent.putExtra("idParty", parties.get(position).getKey());
                startActivity(intent);
            }
        });

    }
        //IF has to show the Parties that you are attending
        private void readAttendingParty(){
        DataBaseRead dr = new DataBaseRead();
        String userID = dr.getUserId();
        Query query;
            mDatabase = FirebaseDatabase.getInstance().getReference();
        query = mDatabase.child("Parties").orderByChild("userAttending/"+""+userID).equalTo(userID);
            query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}
                else {
                    DataBaseRead dbR = new DataBaseRead();
                    parties = dbR.getAllParties(dataSnapshot);
                    ListAdapterParties LicustomAdapter = new ListAdapterParties(ListPartiesActivity.this, R.layout.adapter_party_list, parties);
                    listViewParties.setAdapter(LicustomAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }


        //If has to show all the Parties
        private void readAllParty(){
            //Reading from DataBase
            mDatabase = FirebaseDatabase.getInstance().getReference(path);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null){}
                    else {

                        DataBaseRead dbR = new DataBaseRead();
                        parties = dbR.getAllParties(dataSnapshot);
                        ListAdapterParties LicustomAdapter = new ListAdapterParties(ListPartiesActivity.this, R.layout.adapter_party_list, parties);
                        listViewParties.setAdapter(LicustomAdapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
}



