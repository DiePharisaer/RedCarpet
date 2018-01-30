package com.tlc.laque.redcarpet.users;

import android.arch.lifecycle.LifecycleService;
import android.content.Intent;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.parties.ListAdapterParties;
import com.tlc.laque.redcarpet.parties.PartiesActivity;
import com.tlc.laque.redcarpet.parties.Party;

import java.text.ParseException;
import java.util.ArrayList;

public class UserActivity extends MainActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseP;
    TextView txNickname;
    TextView txLocation;
    TextView txNumberFriends;
    TextView txPhoneNumber;
    TextView txFriendStatus;
    ImageView image;
    Button buttonAddFriend;
    Button buttonNoFriend;
    String userID;
    ArrayList<Party> partiesAttending;
    Query query;
    private ListView listView;
    DataBaseRead dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_user, contentFrameLayout);

        dr = new DataBaseRead();
        //Get variable from ListUsers activity
        Bundle extras = getIntent().getExtras();
        userID = extras.getString("idUser");

        initVariable();
        getUserInfo();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(UserActivity.this, PartiesActivity.class);
                intent.putExtra("pathParty",  "Parties/"+partiesAttending.get(position).getKey());
                intent.putExtra("idParty", partiesAttending.get(position).getKey());
                startActivity(intent);
            }
        });


    }


    private void initVariable(){
        listView = findViewById(R.id.listViewUserParty);
        txNickname = findViewById(R.id.textViewNNUser);
        txLocation = findViewById(R.id.TextViewLocUser);
        image = findViewById(R.id.imageView2);
       // txNumberFriends = findViewById(R.id.textViewNumberFriends)
        txPhoneNumber = findViewById(R.id.textViewPhoneNumberUser);
        txFriendStatus = findViewById(R.id.textViewAddFriend);

        buttonAddFriend = findViewById(R.id.buttonAddFriends);
        buttonNoFriend = findViewById(R.id.buttonNoFriend);

        if(dr.getUserId().equalsIgnoreCase(userID)){
            buttonAddFriend.setVisibility(View.INVISIBLE);
            txFriendStatus.setVisibility(View.INVISIBLE);
        }
    }

    private void getUserInfo(){


        mDatabase = FirebaseDatabase.getInstance().getReference("users"+"/"+userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}                    //If user exist show the old informations
                else {
                    DataBaseRead dr = new DataBaseRead();
                    //user = getData(dataSnapshot, userId);
                    setTextUser(dr.getUser(dataSnapshot));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabaseP = FirebaseDatabase.getInstance().getReference();
        query= mDatabaseP.child("Parties").orderByChild("userAttending/"+""+userID).equalTo(""+userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}
                else {
                    DataBaseRead dbR = new DataBaseRead();
                    try {
                        partiesAttending = dbR.getAllParties(dataSnapshot);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ListAdapterParties LicustomAdapter = new ListAdapterParties(UserActivity.this, R.layout.adapter_list_user, partiesAttending);
                    listView.setAdapter(LicustomAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTextUser(User user){
        setImage(user);
        buttonNoFriend.setVisibility(View.INVISIBLE);
        txNickname.setText(user.getNickname());
        if(checkPRivacy(user)) {
            txLocation.setText(user.getLocation());
            txPhoneNumber.setText(user.getPhoneNumber());
        }
        else {
            txLocation.setText("private");
            txPhoneNumber.setText("private");
        }
        if(user.isSentFriendRequest()){
            txFriendStatus.setText("Request sent");
            buttonAddFriend.setText("Cancel");
        }
        else if(user.isFriendOrNot()){
            txFriendStatus.setText("Friend");
            buttonAddFriend.setText("UnFriend");
        }
        else if(user.isGotFriendRequest()) {
            txFriendStatus.setText("Accept Friend Request?");
            buttonNoFriend.setVisibility(View.VISIBLE);
            buttonAddFriend.setText("Accept");

        }
        else {
            txFriendStatus.setText("Send Friend Request");
            buttonAddFriend.setText("Add Friend");
        }
    }

    public void clickAddfriend(View v){
        DataBaseWrite dw = new DataBaseWrite();
        if(txFriendStatus.getText().toString().equalsIgnoreCase("Send Friend Request"))
            dw.addFriendAttending(userID);
        else if(txFriendStatus.getText().toString().equalsIgnoreCase("Accept Friend Request?"))
            dw.acceptFriendRequest(userID);
        else if(txFriendStatus.getText().toString().equalsIgnoreCase("Friend"))
            dw.unFriend(userID);
        else if(txFriendStatus.getText().toString().equalsIgnoreCase("Request sent"))
            dw.cancelSentFriendRequest(userID);
    }
    public void buttonRefuseFriend(View v){
        DataBaseWrite dw = new DataBaseWrite();
        dw.refuseFriendRequest(userID);
    }
    private boolean checkPRivacy(User u){
        if(u.getPrivacy().equalsIgnoreCase("No one")){
        return false;}
        else if(u.getPrivacy() == null)
            return false;
        else if(u.getPrivacy().equalsIgnoreCase("Only Friends") && u.isFriendOrNot())
           return true;
        else if(u.getPrivacy().equalsIgnoreCase("Everyone"))
            return true;
        return false;
    }

    private void setImage (User u){
        Picasso.with(getApplicationContext()).load( u.getUrlPicture()).fit().centerCrop()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.error_download)
                .into(image);
    }
}
