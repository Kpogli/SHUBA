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

public class CrowdActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button buttonSendMessage;
    private EditText inputMessage;
    private TextView chatConversation;

    private FirebaseAuth firebaseAuth;

    private String userName;

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

        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonSendMessage = (Button) findViewById(R.id.send_button);
        inputMessage = (EditText) findViewById(R.id.crowd_message);
        chatConversation = (TextView) findViewById(R.id.textView);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
