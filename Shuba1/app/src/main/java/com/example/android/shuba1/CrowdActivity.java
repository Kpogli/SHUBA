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

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Calendar;
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

    //private Date messagesFromDate;
    private long timeStampToStart = new Date().getTime()-(6*60*60*1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd);

        toolbar = (Toolbar) findViewById(R.id.app_bar_crowd);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);


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

        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.message,FirebaseDatabase.getInstance().getReference().child("crowd").orderByChild("timestamp").startAt(timeStampToStart)) {
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

        if (item.getItemId() == R.id.six_hours) {
            timeStampToStart = new Date().getTime()-(6*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last 6 Hours", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.twelve_hours) {
            timeStampToStart = new Date().getTime()-(12*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last 12 Hours", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.twentyfour_hours) {
            timeStampToStart = new Date().getTime()-(24*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last 24 hours", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.fortyeight_hours) {
            timeStampToStart = new Date().getTime()-(48*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last 48 Hours", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.last_week) {
            timeStampToStart = new Date().getTime()-(7*24*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last Week", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.last_2_weeks) {
            timeStampToStart = new Date().getTime()-(2*7*24*60*60*1000);
            displayChatMessages();

            Toast.makeText(CrowdActivity.this, "Messages from Last 2 Weeks", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.all_messages) {
            timeStampToStart = 0;
            displayChatMessages();
            Toast.makeText(CrowdActivity.this, "All messages", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
