package com.psguide.uttam.Common;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psguide.uttam.Models.User;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Common {

    public static final float REFER_COIN = 100;
    public static final float LIKE_REWARD = 1;
    public static final long ACCOINT_CLEARENE_INTERVAL = 24;
    public static final float SHARE_REWARD = 1;
    public static User currentLoggedUser;
    public static String postContentCss =" * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .imgbox {\n" +
            "            display: grid;\n" +
            "            height: 100%;\n" +
            "        }\n" +
            "        .center-fit {\n" +
            "            max-width: 100%;\n" +
            "            max-height: 100vh;\n" +
            "            margin: auto;\n" +
            "        }";
    public static long likeButtonInterval =30000;
    public static String transaactionListener = "TRANSACTION_LISTENER";
    public static String transactionAll = "all";
    public static float minPayoutAmount = 20;
    public static String paymentMethod = "paymentMethod";
    public static double convertunit = 0.005;


    public static Timestamp getCurrentTimeStamp(){
       return new Timestamp(System.currentTimeMillis());
    }

    public static String generateReferCode(){

        Random rand = new Random();

        int n = rand.nextInt(99) + 10;
        String referCode = String.valueOf(System.currentTimeMillis()).substring(String.valueOf(System.currentTimeMillis()).length()-4)+n;
        Log.d("CurrentTime", referCode);

        return referCode;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String dateFormatter(String date){
        String formattedDate = null;
        String oldstring = date;
        try {
            Date Olddate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(oldstring);


             formattedDate = new SimpleDateFormat("d-MMMM-y").format(Olddate);
            System.out.println(formattedDate);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(d));
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }


    public static void deleteMyAccount(String userId){
        FirebaseDatabase.getInstance().getReference("User")
                .child(userId)
                .removeValue();
    }

    public static void setOnlineStatus(String userId, final boolean status){

    }

}
