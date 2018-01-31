package com.tlc.laque.redcarpet.registration;


import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.users.User;

/**
 * REGISTRATION CLASS
 * ASKING THE USERS: LOCATION, NICKNAME
 * SAVING IN THE DATABASE
 * MOVING TO MAINACTIVITY
 */

public class RegistrationActivity extends AppCompatActivity {

    private static final String REQUIRED = "Required";


    private DatabaseReference mDatabase;
    private EditText nickNameField;
    private EditText locationField;
    private FirebaseAuth mAuth;
    private User oldUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nickNameField = (EditText) findViewById(R.id.editTextNickName);
        locationField = (EditText) findViewById(R.id.editTextLocation);
        setEdit();
    }

    //This method is checking if the user was already registered before and he is giving back the information that he saved before
    private void setEdit(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        String userID = userA.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("users"+"/"+userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){}                    //If user exist show the old informations
                else {
                    DataBaseRead dr = new DataBaseRead();
                    oldUser = dr.getUser(dataSnapshot);
                    //user = getData(dataSnapshot, userId);
                    nickNameField.setText(oldUser.getNickname());
                    locationField.setText(oldUser.getLocation());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //ONLICK for register BUTTON... Checking editText, uploading database and moving to MainActivity
    public void clickRegister(View v){
        final String nickName = nickNameField.getText().toString();
        final String location = locationField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(nickName)) {
            nickNameField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(location)) {
            locationField.setError(REQUIRED);
            return;
        }


        User user = new User(nickName, location, "Everyone"); // creating user object
        // pushing user to 'users' node using the the authID
        DataBaseWrite d = new DataBaseWrite();
        if(oldUser == null){
            d.writeUser(user); //Method to write in the Database
        }
        else{
            user.setUrlPicture(oldUser.getUrlPicture());
            DataBaseRead dr = new DataBaseRead();
            dr.checkExist(user, oldUser, this);
        }


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("MyNickName", user.getNickname());
        editor.commit();

        moveToMain();


    }
    //Move to Main after save the new user in the database and save it in the Preferences
    private void moveToMain(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("logB", true);
        editor.commit();
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
