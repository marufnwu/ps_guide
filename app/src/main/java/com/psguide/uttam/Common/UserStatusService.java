package com.psguide.uttam.Common;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserStatusService extends Service {

    FirebaseAuth mAuth;

    public UserStatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mAuth = FirebaseAuth.getInstance();




        if(mAuth.getUid()!=null){

            FirebaseDatabase.getInstance().getReference("User")
                    .child(mAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                DatabaseReference userStatus =
                                        FirebaseDatabase.getInstance().getReference("User/"+mAuth.getUid()+"/isOnline");
                                userStatus.onDisconnect().setValue(false);
                                userStatus.setValue(true);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            DatabaseReference activeUser =
                    FirebaseDatabase.getInstance().getReference("ActiveUser/"+mAuth.getUid());
            activeUser.onDisconnect().removeValue();
            activeUser.setValue(true);



        }
        String userId = "Unknown";

        if (mAuth!=null){
            userId = mAuth.getUid();
        }




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //setUserStatus(true, mAuth);


        Log.d("startedService", "running");

        return  START_NOT_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("startedService", "On Task Removed");
        //setUserStatus(false, mAuth);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setUserStatus(false, mAuth);

    }

    private void setUserStatus(final boolean status, FirebaseAuth mAuth){
        if (mAuth.getCurrentUser() != null){
            FirebaseDatabase.getInstance().getReference().child("User")
                    .child(mAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                dataSnapshot.child("isOnline").getRef().setValue(status);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else {
            Log.d("startedService", "Auth null");

        }
    }
}
