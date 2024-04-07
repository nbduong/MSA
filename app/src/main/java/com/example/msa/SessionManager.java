package com.example.msa;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
public class SessionManager {

    private static final String PREF_NAME = "MyAppPref";
    private static final String KEY_UID = "uid";

    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu trạng thái đăng nhập
    public void saveLoginState() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedPreferences.edit().putString(KEY_UID, uid).apply();
    }

    // Kiểm tra trạng thái đăng nhập
    public boolean isLoggedIn() {
        return sharedPreferences.getString(KEY_UID, null) != null;
    }

    // Xóa trạng thái đăng nhập
    public void clearLoginState() {
        sharedPreferences.edit().remove(KEY_UID).apply();
    }
}
