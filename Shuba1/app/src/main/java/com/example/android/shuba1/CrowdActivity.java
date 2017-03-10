package com.example.android.shuba1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CrowdActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button buttonSendMessage;
    private EditText inputMessage;
    private ListView chatConversation;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference messages;

    private FirebaseListAdapter<ChatMessage> adapter;

    private String userName;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd);

        toolbar = (Toolbar) findViewById(R.id.app_bar_crowd);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.RED);


        //add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        //load chat room contents


        final FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("crowd");

        displayChatMessages();

        buttonSendMessage = (Button) findViewById(R.id.send_button);
        inputMessage = (EditText) findViewById(R.id.crowd_message);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("crowd")
                        .push()
                        .setValue(new ChatMessage(inputMessage.getText().toString(),user.getDisplayName(),user.getUid()));
                inputMessage.setText("");
            }
        });


    }

    private void displayChatMessages() {
        chatConversation = (ListView) findViewById(R.id.msg_view);

        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message,FirebaseDatabase.getInstance().getReference().child("crowd")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                // Get references to the views of message.xml
                TextView msg = (TextView)v.findViewById(R.id.message_text);
                TextView name = (TextView)v.findViewById(R.id.message_sender);
                TextView timestamp = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                msg.setText(model.getMsg());
                name.setText(model.getName());

                // Format the date before showing it
                timestamp.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimestamp()));

            }
        };

        chatConversation.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();   //close this activity and return to previous.:)
        }

        if (item.getItemId() == R.id.nothing_yet) {
            Toast.makeText(CrowdActivity.this, "Does nothing yet", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
