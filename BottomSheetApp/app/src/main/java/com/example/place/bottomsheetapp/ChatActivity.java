package com.example.place.bottomsheetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.internal.ActivityLifecycleTracker;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private String uid1, uid2;
    private RecyclerView listOfMessages;
    private TextView textTyping;
    private EditText input;
    private ImageView imgClose, imgBack;
    private boolean isOnline = false;
    private String lastScene;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    private void removeActive(String uid1) {

        if (FirebaseDatabase.getInstance().getReference().child("users").child(uid1) != null)

            FirebaseDatabase.getInstance().getReference().child("users").child(uid1).removeValue();

        FirebaseDatabase.getInstance().getReference().child("personalmessages").startAt(uid1).endAt(uid1).getRef().removeValue();

        if (uid2 != null)
            FirebaseDatabase.getInstance().getReference().child("users").child(uid2).child("isActive").setValue("0");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ((TextView) findViewById(R.id.receiverId)).setText(getIntent().getStringExtra("receiverId"));
        ((TextView) findViewById(R.id.senderid)).setText(FirebaseAuth.getInstance().getUid());
        ((TextView) findViewById(R.id.chatName)).setText(getIntent().getStringExtra("headerName"));
        listOfMessages = findViewById(R.id.list_of_messages);
        textTyping = findViewById(R.id.textTyping);
        imgClose = findViewById(R.id.imgClose);
        imgBack = findViewById(R.id.imgBack);
        imgClose.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        input = findViewById(R.id.input);
       /* FirebaseDatabase.getInstance().getReference().child("users").orderByChild("isActive").equalTo("0").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getValue(User.class).getUserID() != null && !child.getValue(User.class).getUserID().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())) {
                        user = child.getValue(User.class);

                        uid1 = FirebaseAuth.getInstance().getUid();
                        uid2 = user.getUserID();
                        if (FirebaseDatabase.getInstance().getReference().child("users").child(uid1) != null)
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid1).child("isActive").setValue("1");

                        if (FirebaseDatabase.getInstance().getReference().child("users").child(uid2) != null)
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid2).child("isActive").setValue("1");

                        displayChatMessage();
                        break;
                    }

                }

                Log.d(getClass().getSimpleName(), "onDataChange: ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //  uid1End = Integer.parseInt(FirebaseAuth.getInstance().getUid().substring(FirebaseAuth.getInstance().getUid().length() - 2, FirebaseAuth.getInstance().getUid().length()));
        //  uid2End = Integer.parseInt(getIntent().getStringExtra("receiverId").substring(getIntent().getStringExtra("receiverId").length() - 2, getIntent().getStringExtra("receiverId").length()));

        uid1 = FirebaseAuth.getInstance().getUid();
        uid2 = getIntent().getStringExtra("receiverId");
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                if (input.getText().toString().trim().length() > 0) {
                    FirebaseDatabase.getInstance()
                            .getReference().child("personalmessages").child(uid1.compareTo(uid2) > 0 ? uid1 + uid2 : uid2 + uid1).child("messageData")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(), FirebaseAuth.getInstance().getUid())
                            );
                } else {
                    Toast.makeText(ChatActivity.this, "message is blank", Toast.LENGTH_SHORT).show();
                }
                // Clear the input
                input.setText("");
            }
        });
        input.addTextChangedListener(this);

        displayChatMessage();
        // updateTypingStatus();

    }

    private void updateTypingStatus() {
        FirebaseDatabase.getInstance().getReference().child("personalmessages").child(uid1.compareTo(uid2) > 0 ? uid1 + uid2 : uid2 + uid1).child("messageStatus").child(getIntent().getStringExtra("receiverId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null && dataSnapshot.getValue().equals("1")) {
                    textTyping.setText("typing..");
                } else if (isOnline)
                    textTyping.setText("Online");
                else
                    textTyping.setText(lastScene);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("users").child(getIntent().getStringExtra("receiverId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null && dataSnapshot.child("userStatus").getValue().equals("1")) {
                    isOnline = true;
                    textTyping.setText("Online");
                } else {
                    isOnline = false;
                    if (dataSnapshot.getValue() != null && dataSnapshot.child("lastScene").getValue() != null) {
                        lastScene = "last scene " + DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                                Long.parseLong(dataSnapshot.child("lastScene").getValue().toString())) + "";
                    }
                    textTyping.setText(lastScene);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayChatMessage() {
        FirebaseDatabase.getInstance().getReference().child("personalmessages").child(uid1.compareTo(uid2) > 0 ? uid1 + uid2 : uid2 + uid1).child("messageData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatMessage> list = new ArrayList<ChatMessage>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(ChatMessage.class));
                }
                if (listOfMessages.getAdapter() == null) {
                    listOfMessages.setAdapter(new ListAdapter(list, FirebaseAuth.getInstance().getUid()));
                    listOfMessages.setLayoutManager(new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false));
                    Log.d(getClass().getSimpleName(), "onDataChange: ");
                } else {
                    ((ListAdapter) listOfMessages.getAdapter()).setList(list);
                    ((ListAdapter) listOfMessages.getAdapter()).notifyDataSetChanged();

                }
                listOfMessages.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

    @Override
    public void afterTextChanged(Editable editable) {

     /*   FirebaseDatabase.getInstance()
                .getReference().child("personalmessages").child(uid1.compareTo(uid2) > 0 ? uid1 + uid2 : uid2 + uid1).child("messageStatus").child(FirebaseAuth.getInstance().getUid()).setValue(editable.toString().length() == 0 ? "0" : "1");*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                logout();
                break;
            case R.id.imgClose:
                logout();
                break;
        }
    }

    private void logout() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChatActivity.this,
                                "You have been signed out.",
                                Toast.LENGTH_LONG)
                                .show();

                        // Close activity
                        finish();
                        Intent intent = new Intent(ChatActivity.this, SplashActivity.class);
                        startActivity(intent);
                    }
                });

        if (uid1 != null)
            removeActive(uid1);
    }

}
