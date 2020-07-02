package com.psguide.uttam.Common;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetAddress;

public class MyApplication extends Application {
    private static MyApplication instance;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null){
            instance = this;
        }

        mAuth = FirebaseAuth.getInstance();
       /* if (mAuth!=null){

            final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(mAuth.getUid());
            mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        DatabaseReference onlineRef  =  mUserRef.child("isOnline");
                        onlineRef.setValue(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }*/



    }

    public static MyApplication getInstance(){
        return instance;
    }

    public static boolean hasNetwork(){
        return instance.isNetworkConnected();
    }

    private boolean isNetworkConnected() {
        /*ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();*/

        ConnectivityManager CManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        boolean con = false;
        if (NInfo != null) {
            try {
                if (InetAddress.getByName("https://mediacell.com").isReachable(30000)) {
                    con = true;
                } else {
                    con = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("Netconnection", ""+con);
        return con;

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAuth != null) {

            final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(mAuth.getUid());
            mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DatabaseReference onlineRef = mUserRef.child("isOnline");
                        onlineRef.setValue(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


}
