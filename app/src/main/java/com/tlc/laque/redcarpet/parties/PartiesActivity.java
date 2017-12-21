package com.tlc.laque.redcarpet.parties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.MessagePattern;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.chat.ChatActivity;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.users.ListUsers;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PartiesActivity extends MainActivity {
    Switch switchAttending;
    TextView edLocation;
    TextView edName;
    TextView edInfo;
    TextView edStTime;
    TextView edFiTime;
    Button buttonChat;
    Button buttonAttending;
    LinearLayout layoutAdmin;
    LinearLayout layoutUser;
    String idParty;
    Party p;
    DataBaseRead dbR;
    boolean  attending = false;
    private FirebaseStorage mFirebaseStorage;

    private DatabaseReference mDatabase;
    private DataBaseWrite dbW;
    private ImageView v;
    private String pathParty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
       contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_parties, contentFrameLayout);
        dbW = new DataBaseWrite();
         dbR = new DataBaseRead();


        Bundle extras = getIntent().getExtras();
        idParty = extras.getString("idParty");
        pathParty = extras.getString("pathParty");


        setVariable();
        getInformatinDataBase();
        switchAttending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dbW.registerUser(dbR.getUserId(),idParty);
                } else {
                    dbW.cancelRegisterUser(dbR.getUserId(),idParty, p.getName());
                }
            }
        });
    }

    //Initialize the Variable
    private void setVariable(){                                 //SetVariable
        edLocation = findViewById(R.id.textViewLocation);
        edInfo = findViewById(R.id.textViewInformation);
        edStTime = findViewById(R.id.textViewTimeStart);
        edFiTime = findViewById(R.id.textViewTimeFinish);
        switchAttending = findViewById(R.id.switchAttendingUs);
        buttonAttending = findViewById(R.id.buttonAttendindParty);
        buttonChat = findViewById(R.id.buttonChatParty);
        layoutAdmin = findViewById(R.id.layoutAdminParty);
        layoutUser = findViewById(R.id.layoutUserParty);

        if(pathParty.toLowerCase().contains("administration")){
            layoutUser.setVisibility(View.GONE);
            switchAttending.setVisibility(View.GONE);

        }
        else{
            layoutAdmin.setVisibility(View.GONE);
        }

    }

    //Get information Party from the DataBase
    private void getInformatinDataBase(){
        mDatabase = FirebaseDatabase.getInstance().getReference(pathParty);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}                    //If user exist show the old informations
                else {
                   getInfoParty(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void  getInfoParty(DataSnapshot dataSnapshot){
        p = new Party();
        p = dbR.getParty(dataSnapshot);
        if(dataSnapshot.child("userAttending").child(dbR.getUserId()).exists()){
            attending = true;
        }
         else {
            attending = false;
        }
        setTextView();

    }

    //Set the TextView after read the DataBase
    private void setTextView(){
        edLocation.setText(p.getLocation());
        edStTime.setText(p.getTimeStart());
        edFiTime.setText(p.getTimeFinish());
        edInfo.setText(p.getInfo());
        setTitle(p.getName());

        if(attending){
            switchAttending.setChecked(true);
        }
        else{
            switchAttending.setChecked(false);
        }
        setImage();
    }

    //Buttons Listener for all the button in the activity
    public void buttonPartyClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonMaps:
                viewOnMap(p.getLocation());
                break;
            case R.id.buttonAttendindParty:
                intent = new Intent(PartiesActivity.this, ListUsers.class);
                intent.putExtra("idParty", idParty );
                startActivity(intent);
                break;
            case R.id.buttonChatParty:
                intent = new Intent(PartiesActivity.this, ChatActivity.class);
                intent.putExtra("nameChat", p.getName());
                intent.putExtra("path", "Parties/"+idParty+ "/chatParty");
                startActivity(intent);
                break;
            case R.id.buttonAcceptParty:
                acceptPArty();
                break;
            case R.id.buttonRefureParty:
                refuseParty();
                break;


        }
    }

    //if request.auth != null
    private void setImage(){
         v = findViewById(R.id.imageViewParty);
        Picasso.with(PartiesActivity.this).load( p.getUrl()).fit().centerCrop()
                .placeholder(R.drawable.downloading2)
                .error(R.drawable.error_download)
                .into(v);

    }

    //Open Google Maps to show the adress of the Party
    private void viewOnMap(String adress) {
        //String r = adress.replaceAll("\\s+","");
        String map = "http://maps.google.co.in/maps?q=" + adress;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    //For Administration Part Accept or Refuse the PARTY
    private void acceptPArty(){
        DataBaseWrite dw = new DataBaseWrite();
        dw.deleteAttendingPArty(p.getKey());
        dw.saveNewParty(p);
    }

    private void refuseParty(){
        DataBaseWrite dw = new DataBaseWrite();
        dw.deleteAttendingPArty(p.getKey());

        //StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(p.getUrl());
    }

}
