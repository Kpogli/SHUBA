package com.example.android.shuba1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLetMeIn;
    private AutoCompleteTextView editTextEmail;
    private EditText editTextPassword;
    //private Button buttonJoinUs;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent enterMap = new Intent(this, MapsActivity.class);
            startActivity(enterMap);
        }

        progressDialog = new ProgressDialog(this);

        //changing statusbar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.myDarkRed));
        }

        editTextEmail = (AutoCompleteTextView) findViewById(R.id.email);
        //will release the auto-complete later
        //populateAutoComplete();

        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLetMeIn = (Button) findViewById(R.id.email_sign_in_button);

        //buttonJoinUs = (Button) findViewById(R.id.email_register_button);

        buttonLetMeIn.setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email not entered
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password not entered
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //If validations are ok, we will 1st show a progress bar
        progressDialog.setMessage("Signing in User....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            //start activity for map
                            finish();
                            Intent letMeIn = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(letMeIn);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if (view == buttonLetMeIn) {
            userLogin();
        }
    }

    public void onclick(View view)
    {
        Intent register = new Intent(this,RegistrationActivity.class);
        startActivity(register);

    }

    @Override
    public void onBackPressed() {
        Intent endApp = new Intent(Intent.ACTION_MAIN);
        endApp.addCategory(Intent.CATEGORY_HOME);
        endApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(endApp);
    }
}

