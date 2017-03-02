package com.example.android.shubadriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private Button emailRegisterButton;
    private EditText editTextFullName;
    private EditText editTextEmail;
    private EditText editTextPassword1;
    private EditText editTextPassword2;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private UserProfileChangeRequest profileUpdates;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        emailRegisterButton = (Button) findViewById(R.id.email_register_button);
        editTextFullName = (EditText) findViewById(R.id.full_name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword1 = (EditText) findViewById(R.id.password1);
        editTextPassword2 = (EditText) findViewById(R.id.password2);

        emailRegisterButton.setOnClickListener(this);
    }


    private void saveDriverFullName() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        DriverFullName driverFullName = new DriverFullName(fullName,email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("drivers").child(user.getUid()).setValue(driverFullName);
    }

    public void onclick_go_back(View view)
    {
        Intent goBack = new Intent(this,LoginActivity.class);
        startActivity(goBack);
    }

    private void registerUser() {
        final String fullName = editTextFullName.getText().toString().trim();
        String emailR = editTextEmail.getText().toString().trim();
        String password = null;
        String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();

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

                            saveDriverFullName();

                            progressDialog.dismiss();

                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                user = FirebaseAuth.getInstance().getCurrentUser();

                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();
                user.updateProfile(profileUpdates);

                FirebaseAuth.getInstance().signOut();
                reLogin();

            }
        });
    }

    public void reLogin() {
        String emailR = editTextEmail.getText().toString().trim();
        String password = editTextPassword1.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(emailR,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //progressDialog.dismiss();
                            //start activity for map
                            finish();
                            Intent letMeIn = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(letMeIn);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == emailRegisterButton){
            registerUser();
        }
    }
}
