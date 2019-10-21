package com.example.dell.ibot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_signup;
    private ImageButton btn_signin;
    private EditText edtTxtEmail;
    private EditText edtTxtPass;
    private ProgressDialog probar;

    private FirebaseAuth firebaseauth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseauth = FirebaseAuth.getInstance();
        //if user is already logged in
        if (firebaseauth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        probar = new ProgressDialog(this);

        btn_signin = findViewById(R.id.but_signin);
        btn_signup = findViewById(R.id.but_signup);

        btn_signin.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

        edtTxtEmail = findViewById(R.id.email1);
        edtTxtPass = findViewById(R.id.pass1);

    }

    @Override
    public void onClick(View view) {

        String email = edtTxtEmail.getText().toString().trim();
        String pass = edtTxtPass.getText().toString().trim();

        if (verification(email, pass)) {
            switch (view.getId()) {
                case R.id.but_signin:
                    performSignInAction(email, pass);
                    break;

                case R.id.but_signup:
                    performSignUpAction(email, pass);
                    break;
            }
        }
    }

    private boolean verification(String email, String pass) {
        if (TextUtils.isEmpty(email)) {
            edtTxtEmail.setError("Enter email");
            return false;
        }
        //else if(){

        //}

        if (TextUtils.isEmpty(pass)) {
            edtTxtPass.setError("Enter pass");
            return false;
        }
        if (pass.length() < 6) {
            edtTxtPass.setError("Password should be at least 6 characters");
            return false;
        }
        return true;
    }


    private void performSignUpAction(final String email, String pass) {

        probar.setMessage("Registering user......");
        probar.show();

        firebaseauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                probar.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("EMAIL", email);
                    startActivity(i);

                } else {

                    Toast.makeText(LoginActivity.this, "Could not registered.....  Please try again  ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void performSignInAction(String email, String pass) {

        probar.setMessage("Getting you logged in...");
        probar.show();
        firebaseauth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                probar.dismiss();
                if (task.isSuccessful()) {
                    finish();
                    firebaseUser = firebaseauth.getCurrentUser();

                    Log.v("user", "sign in successfully");
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    Toast.makeText(LoginActivity.this, "Getting you Logged in....", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(LoginActivity.this, "Not able to login ..Retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
