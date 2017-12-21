package com.tlc.laque.redcarpet.settings;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.users.ListAdapterUser;
import com.tlc.laque.redcarpet.users.ListUsers;
import com.tlc.laque.redcarpet.users.User;

public class UserSettingActivity extends MainActivity {
    String[] selextStates = new String[]{"Everyone", "Only Friends", "No one"};
    private DatabaseReference mDatabase;
    private EditText nickNameField;
    private EditText locationField;
    private Spinner mSpinner;
    private User user;
    private  DataBaseRead dr;
    private static final String REQUIRED = "Required";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_user_setting, contentFrameLayout);

        nickNameField = (EditText) findViewById(R.id.editTextNameSetting);
        locationField = (EditText) findViewById(R.id.editTextLocationSetting);


        mSpinner = findViewById(R.id.spinnerPrivacy);

        String compareValue = "No one";
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        selextStates);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerArrayAdapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = spinnerArrayAdapter.getPosition(compareValue);
            mSpinner.setSelection(spinnerPosition);
        }

        setEdit();
    }


    private void setEdit() {
        dr = new DataBaseRead();
        String userID = dr.getUserId();

        mDatabase = FirebaseDatabase.getInstance().getReference("users" + "/" + userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                }                    //If user exist show the old informations
                else {
                    user = dataSnapshot.getValue(User.class);
                    //user = getData(dataSnapshot, userId);
                    nickNameField.setText(user.getNickname());
                    locationField.setText(user.getLocation());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void saveButton(View view){
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
        String privacy = mSpinner.getSelectedItem().toString();

        dr.checkExist(new User(nickName, location, privacy), user, this);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("MyNickName", nickName);
        editor.commit();


    }
}

