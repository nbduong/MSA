package com.example.msa;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthService {
    private FirebaseAuth mAuth;
    private int status ;

    public FirebaseAuthService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void resetPassword(Context view, String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setStatus(1);
                        Log.d(TAG, "Reset password link has been sent to your Email.");
                        Toast.makeText(view, "Reset password link has been sent to your Email.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Fail to send password reset link.", e);
                        Toast.makeText(view, "Fail to send password reset link.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
