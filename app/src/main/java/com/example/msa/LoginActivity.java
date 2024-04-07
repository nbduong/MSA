package com.example.msa;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity {
    FirebaseAuthService service;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent home = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(home);
            finish();
        }

        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        TextView signUpTextView = findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });

        TextView textViewForgetPassword = findViewById(R.id.textViewForgetPassword);
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPassword = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetPassword);
            }
        });

        ImageView login = findViewById(R.id.loginImageView);
        service = new FirebaseAuthService();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr =  email.getText().toString();
                final String passwordStr = password.getText().toString();
                if(emailStr.isEmpty() || passwordStr.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    Toast.makeText(LoginActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passwordStr.length() < 8) {
                    Toast.makeText(LoginActivity.this, "At least 8 characters for password", Toast.LENGTH_SHORT).show();
                    return;
                }
                service.getmAuth().signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            sessionManager.saveLoginState();
                            Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(home);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


    }
}