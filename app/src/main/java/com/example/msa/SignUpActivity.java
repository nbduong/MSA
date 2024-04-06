package com.example.msa;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirestoreService firestoreService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView signUpTextView = findViewById(R.id.signInTextView);

        firestoreService = new FirestoreService();
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent( SignUpActivity.this , LoginActivity.class);
                startActivity(signIn);
                finish();
            }
        });

        //
        mAuth = FirebaseAuth.getInstance();
        final EditText fullnameText = findViewById(R.id.editTextName);
        final EditText emailText = findViewById(R.id.editTextEmail);
        final EditText phoneNumberText = findViewById(R.id.editTextPhoneNumber);
        final EditText passwordText = findViewById(R.id.editTextPassword);
        final EditText rePasswordText = findViewById(R.id.editTextRePassword);
        ImageView registerButton = findViewById(R.id.loginImageView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fullname = fullnameText.getText().toString();
                final String email =  emailText.getText().toString();
                final String phoneNumber = phoneNumberText.getText().toString();
                final String password = passwordText.getText().toString();
                final String rePassword = rePasswordText.getText().toString();

                if(fullname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || rePassword.isEmpty()){

                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneNumberText.getText().toString().length() != 10) {

                    Toast.makeText(SignUpActivity.this, "Phone number unrecognizable", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 8) {
                    Toast.makeText(SignUpActivity.this, "At least 8 characters for password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!rePassword.equals(password)) {

                    Toast.makeText(SignUpActivity.this, "Passwords are not matching !", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    try {


                    final Long phoneNumberDecimal = Long.valueOf(phoneNumberText.getText().toString());

                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "Create user successfully", Toast.LENGTH_SHORT).show();
                                            firestoreService.addUserToFireStore(email,fullname,phoneNumberDecimal);
                                            Intent openSignIn = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(openSignIn);
                                        }
                                        else {
                                            Log.e(TAG, "Create user failed", task.getException());
                                            Toast.makeText(SignUpActivity.this, "Create user failed", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });

                }catch (NumberFormatException exception){
                        Toast.makeText(SignUpActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

}