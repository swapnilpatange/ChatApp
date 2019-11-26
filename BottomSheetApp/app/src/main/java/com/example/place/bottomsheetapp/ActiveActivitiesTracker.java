package com.example.place.bottomsheetapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ActiveActivitiesTracker {
    private static int sActiveActivities = 0;

    public static void activityStarted() {
        /*if (sActiveActivities == 0) {
            if (FirebaseAuth.getInstance() != null) {
                FirebaseDatabase.getInstance()
                        .getReference().child("users").child("inactive").child(FirebaseAuth.getInstance().getUid()).child("userStatus").setValue("1");
            }
        }*/
        sActiveActivities++;
    }

    public static void activityStopped() {
        sActiveActivities--;
       /* if (sActiveActivities == 0) {
            if (FirebaseAuth.getInstance() != null) {
                FirebaseDatabase.getInstance()
                        .getReference().child("users").child("inactive").child(FirebaseAuth.getInstance().getUid()).child("userStatus").setValue("0");
                FirebaseDatabase.getInstance()
                        .getReference().child("users").child("inactive").child(FirebaseAuth.getInstance().getUid()).child("lastScene").setValue(new Date().getTime());
            }
        }*/
    }
}