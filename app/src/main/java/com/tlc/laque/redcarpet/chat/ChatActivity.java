package com.tlc.laque.redcarpet.chat;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tlc.laque.redcarpet.R;

public class ChatActivity extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessage> adapter;
    private String nickNameUser;
    private String nameChat;
    private String path;
    private static final String REQUIRED = "Required";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        nickNameUser = sharedPref.getString("MyNickName", "hamza" );

        Bundle extras = getIntent().getExtras();
        nameChat = extras.getString("nameChat");
        path = extras.getString("path");

        setTitle(nameChat + " CHAT");
        displayChatMessage();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database)
                String inString = input.getText().toString();
                if(inString == null) {
                    input.setError(REQUIRED);
                    return;
                }

                FirebaseDatabase.getInstance()
                        .getReference(path)
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                nickNameUser)
                        );

                // Clear the input
                input.setText("");
            }
        });
    }

    private void displayChatMessage(){
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.adapter_chat, FirebaseDatabase.getInstance().getReference(path)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
        listOfMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listOfMessages.setStackFromBottom(true);
    }
}
