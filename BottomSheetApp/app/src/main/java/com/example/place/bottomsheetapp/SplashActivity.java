package com.example.place.bottomsheetapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private final int interval = 1000; // 1 Second
    private Handler handler = new Handler();
    private int SIGN_IN_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable runnable = new Runnable() {
            public void run() {
                findViewById(R.id.appName).setVisibility(View.GONE);
                isUserLogged();

            }
        };
        handler.postDelayed(runnable, interval);

    }

    private void isUserLogged() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName() + " " + FirebaseAuth.getInstance().getUid(),
                    Toast.LENGTH_LONG)
                    .show();
            finish();
            Intent intent = new Intent(SplashActivity.this, UserListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();

                setUserData();
                finish();
                Intent intent = new Intent(SplashActivity.this, UserListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }

    private void setUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("users");
        User user = new User(FirebaseAuth.getInstance().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null ? FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().getPath() : "", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "0");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(user);

    }
}
