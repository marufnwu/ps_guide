package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psguide.uttam.Common.Common;
import com.psguide.uttam.Models.Activity;
import com.psguide.uttam.Models.User;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    String currentVersion, latestVersion;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();


    }

    @Override
    protected void onStart() {
        super.onStart();

        getCurrentVersion();
    }

    private void checkLogin() {
        muser = mAuth.getCurrentUser();



        if (muser!=null){


            setUserActivity();
            startActivity(new Intent(this, MainActivity.class));

        }else {
            startActivity(new Intent(this, LoginActivity.class));

        }
    }

    private void setUserActivity() {

        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            User user = dataSnapshot.getValue(User.class);

                            Activity activity = new Activity();

                            activity.date = currentDate;
                            activity.time = currentTime;
                            activity.name = user.getFullName();
                            activity.userId = user.getUid();

                           DatabaseReference actRef =  FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Activity")
                                    .child(currentDate);
                                    actRef.push().setValue(activity);
                        }else {
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





    }

    private void checkUser(final FirebaseUser user) {

        DatabaseReference mUserRef= FirebaseDatabase.getInstance().getReference().child("User");


        final String userId = user.getUid();
        Log.d("Myuserid", userId);

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)){
                    //user already registered
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    //user not registered
                    //so need to show details dialog
                    //then goto login
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mAuth.signOut();
            }
        });
        creteMyFile();

    }

    private void creteMyFile() {

    }


    private  void getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        currentVersion = currentVersion.replaceAll("[-+.^:,]","");




        new GetLatestVersion().execute();

    }


    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.psguide.uttam").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();
                latestVersion = latestVersion.replaceAll("[-+.^:,]","");
                Log.d("latestVer", latestVersion);

            }catch (Exception e){
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null) {
                if (Long.parseLong(currentVersion) < Long.parseLong(latestVersion)){
                    if(!isFinishing()){ //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }else {
                    //checkLogin();


                }
            }
            else
                //checkLogin();
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.psguide.uttam")));
                dialog.dismiss();
            }
        });


        builder.setCancelable(false);
        dialog = builder.show();
    }
}
