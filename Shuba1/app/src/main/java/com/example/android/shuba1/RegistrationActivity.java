package com.example.android.shuba1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private Button emailRegisterButton;
    private EditText editTextFullName;
    private EditText editTextEmail;
    private EditText editTextPassword1;
    private EditText editTextPassword2;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_real);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        emailRegisterButton = (Button) findViewById(R.id.email_register_button_real);
        editTextFullName = (EditText) findViewById(R.id.full_name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword1 = (EditText) findViewById(R.id.password1);
        editTextPassword2 = (EditText) findViewById(R.id.password2);

        emailRegisterButton.setOnClickListener(this);


    }

    public void onclick_go_back(View view)
    {
        Intent goBack = new Intent(this,LoginActivity.class);
        startActivity(goBack);
    }

    private void registerUser(){
        String fullName = editTextFullName.getText().toString().trim();
        String emailR = editTextEmail.getText().toString().trim();
        String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String password = null;

        if (TextUtils.isEmpty(fullName)) {
            //full name not given
            Toast.makeText(this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(emailR)) {
            //email not entered
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password1)) {
            //1st password not entered
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password2)) {
            //2nd password not entered
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password1.equals(password2)) {
            password = password2;
        }

        if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords don't match" ,Toast.LENGTH_SHORT).show();
            return;
        }


        //If validations are ok, we will 1st show a progress bar
        progressDialog.setMessage("Registering User....");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(emailR, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user is successfully registered
                            //will do profile activity here later
                            //display a toast for now
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });
    }


    public void onClick(View view)
    {
        if (view == emailRegisterButton){
            registerUser();
        }
    }
}
