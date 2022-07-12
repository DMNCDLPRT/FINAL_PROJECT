package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText login_email_et, login_password_et;
    TextView tv_register;
    Button btn_login;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_email_et = findViewById(R.id.login_email_et);
        login_password_et= findViewById(R.id.login_password_et);
        tv_register = findViewById(R.id.tv_register);
        btn_login= findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(view -> loginUser());

        tv_register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = Objects.requireNonNull(login_email_et.getText()).toString();
        String password = Objects.requireNonNull(login_password_et.getText()).toString();

        if(TextUtils.isEmpty(email)) {
            login_email_et.setError("Email cannot be empty");
            login_email_et.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            login_password_et.setError("Password cannot  be empty");
            login_password_et.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User Successfully logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                } else{
                    Toast.makeText(LoginActivity.this, "Login Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}