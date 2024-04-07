package com.example.msa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewPhoto;
    private FirebaseAuthService firebaseAuthService;
    private FirestoreService firestoreService;
    private SessionManager sessionManager;
    @Override
    public void onBackPressed() {
        if (isOpen) {
            TransitionManager.beginDelayedTransition(constraintLayout);
            layout1.applyTo(constraintLayout);
            isOpen = false;
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        firebaseAuthService = new FirebaseAuthService();
        firestoreService = new FirestoreService();
        //Lay dữ liệu từ Firebase đổ vào;
        String email = firebaseAuthService.getmAuth().getCurrentUser().getEmail();
        TextView displayEmail = findViewById(R.id.txt_email);
        TextView displayFullName = findViewById(R.id.txt_fullname);
        TextView displayPhone = findViewById(R.id.txt_phone);
        DocumentReference docRef = firestoreService.getDatabase()
                .collection("Users")
                .document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private String fullName;
            private long phoneNumber;
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fullName = document.getString("full_name");
                        phoneNumber = document.getLong("phone_number");

                        displayFullName.setText("Username: "+fullName);
                        displayPhone.setText("Phone number: "+ String.valueOf(phoneNumber));
                        displayEmail.setText("Email: "+email.toString());

                        Log.d("TAG", "Dữ liệu: " + document.getData());
                    } else {
                        Log.d("TAG", "Không có tài liệu nào tồn tại");
                    }
                } else {
                    Log.d("TAG", "Lỗi khi lấy dữ liệu: ", task.getException());
                }
            }
        });



        //
        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        imageViewPhoto = findViewById(R.id.photo);
        constraintLayout = findViewById(R.id.constraint_layout);
        layout2.clone(this,R.layout.profile_expanded);
        layout1.clone(constraintLayout);

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = true ;
                }

                else {

                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = false ;

                }
            }
        });
        Button btnLogOut = findViewById(R.id.btnLogout);
        sessionManager = new SessionManager(this);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to sign out?");

                // Thêm nút Đồng ý
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đăng xuất khi người dùng nhấn nút Đồng ý
                        firebaseAuthService.getmAuth().signOut();
                        sessionManager.clearLoginState();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                        finish(); // Kết thúc Activity hiện tại
                    }
                });

                // Thêm nút Hủy
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì khi người dùng nhấn nút Hủy
                        dialog.dismiss(); // Đóng AlertDialog
                    }
                });

                // Hiển thị AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



    }
}