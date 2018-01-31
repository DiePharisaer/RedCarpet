package com.tlc.laque.redcarpet.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.parties.CreateNewPartyActivity;
import com.tlc.laque.redcarpet.parties.PartiesActivity;
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
    private int GALLERY_INTENT = 2;
    private ImageView imageSelected;
    private StorageReference mStorage;
    private Uri uri;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_user_setting, contentFrameLayout);

        nickNameField = (EditText) findViewById(R.id.editTextNameSetting);
        locationField = (EditText) findViewById(R.id.editTextLocationSetting);
        imageSelected = findViewById(R.id.imageViewUserSelected);


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
                    DataBaseRead dr = new DataBaseRead();
                    user = dr.getUser(dataSnapshot);
                    //user = getData(dataSnapshot, userId);
                    nickNameField.setText(user.getNickname());
                    locationField.setText(user.getLocation());
                    setImage();
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

        if(uri == null){
            Toast.makeText(UserSettingActivity.this, "Error Server", Toast.LENGTH_LONG).show();
        }
        else {
            StorageReference filePath = mStorage.child("users_images").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    url = taskSnapshot.getDownloadUrl().toString();
                    uploadUser(nickName, location);
                }
            });

            Toast.makeText(this, "Data Saved", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadUser(String nickName, String location){
        String privacy = mSpinner.getSelectedItem().toString();

        dr.checkExist(new User(nickName, location, privacy, url), user, this);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("MyNickName", nickName);
        editor.commit();
    }

    public void buttonSelectImage(View view){
        getImage();
    }

    //Get Image from the Gallery
    private void getImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }
    private void setImage(){
        ImageView v = findViewById(R.id.imageViewUserSelected);
        Picasso.with(UserSettingActivity.this).load(user.getUrlPicture()).fit().centerCrop()
                .placeholder(R.drawable.progress_animation )
                .error(R.drawable.error_download)
                .into(v);


    }
    // Open Activity Gallery to select the Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mStorage = FirebaseStorage.getInstance().getReference();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            imageSelected.setImageURI(uri);
        }
    }
}

