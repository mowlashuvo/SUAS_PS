package com.example.suas_ps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class SingInActivity extends AppCompatActivity {
    private static final String TAG = "Loginivity";
    private static final int RC_SIGN_IN = 123;

    private MySharedPreferences mySharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    List<AuthUI.IdpConfig> providers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        mAuth = FirebaseAuth.getInstance();
        mySharedPreferences = new MySharedPreferences(this);

        providers = Arrays.asList(
                /*new AuthUI.IdpConfig.FacebookBuilder().build(),*/
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()

        );

        showSignInOption();

    }

    private void showSignInOption () {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyFireBaseUITheme)
                        .build(),Constants.MY_LOGIN_REQUEST_CODE
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.MY_LOGIN_REQUEST_CODE) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                mySharedPreferences.setLoggedIn(true);
                startActivity(new Intent(SingInActivity.this, DashboardActivity.class));
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
