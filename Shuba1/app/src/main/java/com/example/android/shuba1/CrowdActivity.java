package com.example.android.shuba1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CrowdActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button buttonSendMessage;
    private EditText inputMessage;
    private TextView chatConversation;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference messages;

    private String userName;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd);

        toolbar = (Toolbar) findViewById(R.id.app_bar_crowd);
        setSupportActionBar(toolbar);

        //add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("crowd");


        buttonSendMessage = (Button) findViewById(R.id.send_button);
        inputMessage = (EditText) findViewById(R.id.crowd_message);
        chatConversation = (TextView) findViewById(R.id.textView);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map1 = new HashMap<String, Object>();
                temp_key = databaseReference.push().getKey();
                databaseReference.updateChildren(map1);

                messages = databaseReference.child(temp_key);

                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", user.getDisplayName());
                map2.put("msg", inputMessage.getText().toString());

                messages.updateChildren(map2);

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        append_crowd_conversation(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        append_crowd_conversation(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private String chat_msg, chat_user_name;

    private void append_crowd_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chatConversation.append(chat_user_name +" : "+chat_msg +" \n");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();   //close this activity and return to previous.:)
        }
        return super.onOptionsItemSelected(item);
    }
}
