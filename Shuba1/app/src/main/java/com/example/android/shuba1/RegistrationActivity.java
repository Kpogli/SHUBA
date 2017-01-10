package com.example.android.shuba1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_real);
    }

    public void onclick_go_back(View view)
    {
        Intent goBack = new Intent(this,LoginActivity.class);
        startActivity(goBack);
    }
}
