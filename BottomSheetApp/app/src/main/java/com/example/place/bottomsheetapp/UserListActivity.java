package com.example.place.bottomsheetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserListActivity extends AppCompatActivity {

    private FirebaseListAdapter<User> adapter;
    private ListView listOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        displayUsers();
        findViewById(R.id.groupChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserListActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
    }


    private void displayUsers() {
        listOfMessages = (ListView) findViewById(R.id.userList);

        adapter = new FirebaseListAdapter<User>(this, User.class,
                R.layout.user_list_item, FirebaseDatabase.getInstance().getReference().child("users")) {
            @Override
            protected void populateView(View v, User model, int position) {
                // Get references to the views of message.xml
                try {
                    TextView userName = (TextView) v.findViewById(R.id.userName);
                    TextView email = v.findViewById(R.id.email);

                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                            intent.putExtra("receiverId", model.getUserID());
                            intent.putExtra("headerName", model.getDisplayName());
                            startActivity(intent);
                        }
                    });
                    // Set their text
                    userName.setText(model.getDisplayName());
                    email.setText(model.getEmail());
                    if (model.getUserID().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        v.setVisibility(View.GONE);
                        userName.setVisibility(View.GONE);
                        email.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UserListActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                            Intent intent = new Intent(UserListActivity.this, SplashActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActiveActivitiesTracker.activityStarted();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActiveActivitiesTracker.activityStopped();
    }

}
