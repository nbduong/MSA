package com.example.msa;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    FirebaseAuthService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText email = findViewById(R.id.editTextEmail);
        ImageView imageViewSend = findViewById(R.id.sendImageView);

        service = new FirebaseAuthService();
        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = email.getText().toString();
                if (TextUtils.isEmpty(strEmail)) {
                    Toast.makeText(ForgetPasswordActivity.this,
                            "Please enter the email for this service", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }
                service.resetPassword(ForgetPasswordActivity.this, strEmail);

                    Intent rsPasswordSuccess = new Intent(ForgetPasswordActivity.this, rsPasswordSuccessActivity.class);
                    startActivity(rsPasswordSuccess);
                    finish();
            }
        });
    }
}