package com.example.msa;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirestoreService {
    private FirebaseFirestore database;

    public FirestoreService() {
        this.database = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseFirestore database) {
        this.database = database;
    }

    public void addUserToFireStore(String email, String fullname, Long phonenumber){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("full_name",fullname);
        newUser.put("phone_number",phonenumber);
        newUser.put("account_state",1);
    database.collection("Users").document(email)
            .set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "User added successfully to Firestore");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error adding user to Firestore", e);
                }
            });

    }
}
