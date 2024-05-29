package com.example.instastash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        TextView signUpText = findViewById(R.id.loginText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUp();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "Login successful!");

                            // Extract domain from email
                            String[] parts = email.split("@");
                            if (parts.length == 2) {
                                String domain = parts[1].split("\\.")[0]; // Get the part before the dot
                                switch (domain.toLowerCase()) {
                                    case "staff":
                                        navigateToStaff();
                                        break;
                                    case "admin":
                                        navigateToAdmin();
                                        break;
                                    default:
                                        navigateToHome();
                                        break;
                                }
                            }
                        } else {
                            // Login failed
                            Toast.makeText(login.this, "Authentication failed. " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", "Authentication failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAdmin() {
        Intent intent = new Intent(this, manageproducts.class);
        startActivity(intent);
        finish();
    }

    private void navigateToStaff() {
        Intent intent = new Intent(this, staffhome.class);
        startActivity(intent);
        finish();
    }
}
