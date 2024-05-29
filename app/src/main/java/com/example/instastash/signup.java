package com.example.instastash;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class signup extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();

            }
        });

        TextView signInText = findViewById(R.id.signInText);
        String fullText = "Already have an account? Sign in";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                navigateToLogin();
            }
        };



        spannableString.setSpan(clickableSpan, fullText.indexOf("Sign in"), fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signInText.setText(spannableString);
        signInText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void signUpUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //champ not empty
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(signup.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(signup.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(signup.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }


        //create user

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signup.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
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
                                } }
                        } else {
                            Toast.makeText(signup.this, "Authentication failed. " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, login.class);
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
