package com.tlc.laque.redcarpet.parties;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.database.DataBaseWrite;
import com.tlc.laque.redcarpet.inputs.MDataEditDate;

import java.util.Calendar;

/*
*Activity for create a new Party
* Only if you have N Friends
*Then it has to be accepted from the Adiminstration
*/

public class CreateNewPartyActivity extends MainActivity {
    private EditText nickNameField;
    private EditText locationField;
    private EditText timeStartField;
    private EditText timeFinishField;
    private EditText informationField;
    private ImageView imageSelected;
    private StorageReference mStorage;
    private Uri uri;

    private int GALLERY_INTENT = 2;

    private String REQUIRED = "Required";
    private Party p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        contentFrameLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.activity_create_new_party, contentFrameLayout);

        initVariable();
    }

    //Initialize all the Variable
    private void initVariable(){
        p = new Party();
        nickNameField = findViewById(R.id.editTextNickNameCParty);
        locationField = findViewById(R.id.editTextLocationCParty);
        timeStartField = findViewById(R.id.editTextTSCParty);
        timeFinishField = findViewById(R.id.editTextTMFParty);
        informationField = findViewById(R.id.editTextInformationCParty);
        imageSelected = findViewById(R.id.imageViewSelectedP);

        timeStartField.addTextChangedListener(new MDataEditDate(timeStartField));
        timeFinishField.addTextChangedListener(new MDataEditDate(timeFinishField));
    }

    //Listener of the Buttons of all the activity
    public void clickCreateParty(View view){
        switch (view.getId()) {
            case R.id.buttonAddImage:
                getImage();
                break;
            case R.id.buttonPostParty:
                uploadParty();
                break;
        }
    }
    //Upload Party to the adiministration Part where will be accepted or not
    private void uploadParty(){
        final String nameParty = nickNameField.getText().toString();
        final String location = locationField.getText().toString();
        final String timeStart = timeStartField.getText().toString();
        final String timeFinish = timeFinishField.getText().toString();
        final String information = informationField.getText().toString();

        if(TextUtils.isEmpty(nameParty)){
            nickNameField.setError(REQUIRED);
            return;
        }
        if(TextUtils.isEmpty(location)){
            locationField.setError(REQUIRED);
            return;
        }
        if(TextUtils.isEmpty(timeStart)){
            timeStartField.setError(REQUIRED);
            return;
        }
        if(TextUtils.isEmpty(timeFinish)){
            timeFinishField.setError(REQUIRED);
            return;
        }
        if(TextUtils.isEmpty(information)){
            informationField.setError(REQUIRED);
        }


        if(uri == null){
            Toast.makeText(CreateNewPartyActivity.this, "Select a image", Toast.LENGTH_LONG).show();
        }
        else {
            StorageReference filePath = mStorage.child("party_images").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    p.setUrl(taskSnapshot.getDownloadUrl().toString());
                    Toast.makeText(CreateNewPartyActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    uploadParty(nameParty, timeStart, timeFinish, location, information);
                }
            });

        }

    }
    private void uploadParty(String nameParty, String timeStart, String timeFinish, String location, String information){
        DataBaseRead dr = new DataBaseRead();
        p.setName(nameParty);
        p.setTimeStart(timeStart);
        p.setTimeFinish(timeFinish);
        p.setLocation(location);
        p.setInfo(information);
        // p.setUrl(uri.toString());
        p.setOrganizer(dr.getUserId());

        DataBaseWrite dw = new DataBaseWrite();
        dw.createParty(p);
        Toast.makeText(this, "Party created, it will be checked by the administration first", Toast.LENGTH_LONG).show();
        finish();
    }

    //Get Image from the Gallery
    private void getImage(){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

    }

    // Open Activity Gallery to select the Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mStorage = FirebaseStorage.getInstance().getReference();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK) {
            uri = data.getData();
            imageSelected.setImageURI(uri);
        }

    }

}
