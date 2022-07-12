package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText edit_text_name, edit_text_email, edit_text_password;
    TextView tv_login;
    Button btn_register;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_email = findViewById(R.id.login_email_et);
        edit_text_password = findViewById(R.id.login_password_et);

        tv_login = findViewById(R.id.tv_login);

        btn_register = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(view -> {
            createUser();
        });

        tv_login.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String name = Objects.requireNonNull(edit_text_name.getText()).toString();
        String email= Objects.requireNonNull(edit_text_email.getText()).toString();
        String password = Objects.requireNonNull(edit_text_password.getText()).toString();

        if(TextUtils.isEmpty(name)) {
            edit_text_name.setText("Full name cannot be empty");
            edit_text_name.requestFocus();
        } else if(TextUtils.isEmpty(email)) {
            edit_text_email.setText("Email cannot be empty");
            edit_text_email.requestFocus();
        } else if(TextUtils.isEmpty(password)) {
            edit_text_password.setText("Password cannot be empty");
            edit_text_password.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }
}