package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.psguide.uttam.Common.Common;
import com.psguide.uttam.Common.FirebaseString;
import com.psguide.uttam.Models.ReferUserByMe;
import com.psguide.uttam.Models.Transaction;
import com.psguide.uttam.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity {

    private static final int GMAIL_SIGN_IN = 10001;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnGmailLogin;
    private DatabaseReference mUserRef;
    private  android.app.AlertDialog waitingDialog;
    private String deepLinkReferCode = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGmailLogin = findViewById(R.id.btnGmailLogin);
        mUserRef = FirebaseDatabase.getInstance().getReference("User");

         editor = getSharedPreferences("TERMS", MODE_PRIVATE).edit();




        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        waitingDialog = new SpotsDialog(LoginActivity.this);
        //waitingDialog.setMessage("Loading..");
        btnGmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showWaitingDialog();
                gmailSignIn();

                //Crashlytics.getInstance().crash(); // Force a crash
            }
        });




    }

    private void showWaitingDialog() {

        if (waitingDialog != null && !waitingDialog.isShowing()){
            waitingDialog.show();
        }
    }

    private void dissmissWaitingDialog(){
        if (waitingDialog != null && waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
    }

    // [START signin]
    private void gmailSignIn() {
        showWaitingDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GMAIL_SIGN_IN);
    }
    // [END signin]


    @Override
    protected void onStart() {
        super.onStart();
        showWaitingDialog();
        SharedPreferences prefs = getSharedPreferences("TERMS", MODE_PRIVATE);
        String accept = prefs.getString("accept", "no");//"No name defined" is the default value.

        if (accept.equals("no")){
            showTermsDialog();
        }else {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user!=null){
                checkUser(user);
            }
        }

        dissmissWaitingDialog();


    }



    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GMAIL_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);



            } catch (ApiException e) {
                dissmissWaitingDialog();
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in fail", e);
                // [START_EXCLUDE]
               // updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
      //  showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUser(user);
                            //waitingDialog.dismiss();
                            //gotoMainActivity();


                        } else {
                            dissmissWaitingDialog();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                      //  hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    //check user already register or not by checkUser() method
    private void checkUser(final FirebaseUser firebaseUser) {


        final String userId = firebaseUser.getUid();
        Log.d("Myuserid", userId);

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)){
                    //user already registered
                    dissmissWaitingDialog();
                    Toast.makeText(LoginActivity.this, "Welcome For Back.", Toast.LENGTH_SHORT).show();
                    gotoMainActivity();
                }else {
                    //user not registered
                    //so need to show details dialog
                    //then goto login
                    dissmissWaitingDialog();
                    showSignUpDialog(firebaseUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mAuth.signOut();
                dissmissWaitingDialog();
            }
        });

    }

    private void showTermsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = (this).getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_terms_dialog, null);

        final WebView webView = view.findViewById(R.id.webViewTerms);

        builder.setTitle("Terms & Condition");
        builder.setCancelable(false);
        builder.setView(view);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Terms & Condition")
                .setCancelable(false)
                .setPositiveButton("ACCEPT",null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button accept = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                    webView.loadUrl("file:///android_asset/terms.html");

               accept.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       editor.putString("accept", "yes");
                       editor.apply();
                       if (dialog.isShowing()){
                           dialogInterface.dismiss();
                       }
                   }
               });

               cancelButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (dialog.isShowing()){
                           dialogInterface.dismiss();
                       }
                   }
               });


            }
        });

        dialog.show();

        dissmissWaitingDialog();
    }


    private void showSignUpDialog(final FirebaseUser firebaseUser) {
        //dialog now showing

        dissmissWaitingDialog();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = (this).getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_signup_dialog, null);

        final EditText fullName = view.findViewById(R.id.txtFullName);
        final EditText email = view.findViewById(R.id.txtEmail);
        final EditText phone = view.findViewById(R.id.txtPhone);


        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ImageView userImg = view.findViewById(R.id.imgUser);


        final String userfullName = firebaseUser.getDisplayName();
        String Useremail = firebaseUser.getEmail();
        //String userPhone= firebaseUser.getPhoneNumber();
        String userPhotoUrl = String.valueOf(firebaseUser.getPhotoUrl());

        //check user have or not refer code in deepkink variable


        fullName.setText(userfullName);
        email.setText(Useremail);



            Glide.with(getApplicationContext()).load(userPhotoUrl).apply(RequestOptions.centerCropTransform()).into(userImg);



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        builder.setTitle("Sign Up");
        builder.setCancelable(false);
        builder.setView(view);

        final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setView(view)
                .setTitle("Sign Up")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                //dialog positive button pressed
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        showWaitingDialog();

                        final User user = new User();
                        user.country = "India";
                        if (fullName.getText().toString().isEmpty()
                                && email.getText().toString().isEmpty()
                                && phone.getText().toString().isEmpty()){
                            //if user information is empty or null


                            Toast.makeText(LoginActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                        }else {
                            //if user information not null or empty
                                user.fullName = fullName.getText().toString();
                                user.email = email.getText().toString();
                                user.phone = phone.getText().toString();
                                user.photoUrl = String.valueOf(firebaseUser.getPhotoUrl());
                                user.uid = firebaseUser.getUid();
                                user.isOnline = false;

                                mUserRef.child(firebaseUser.getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    if (dialogInterface!=null){
                                                        dialogInterface.dismiss();
                                                        gotoMainActivity();
                                                    }
                                                }else {

                                                    dialogInterface.dismiss();
                                                    mAuth.signOut();

                                                    dissmissWaitingDialog();


                                                }
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {

                                        mAuth.signOut();
                                    }
                                });



                        }

                    }
                });
            }
        });


       if (!LoginActivity.this.isFinishing()){
           dialog.show();
       }



    }

    private void gotoMainActivity() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null){

            //https://digital-wb.firebaseio.com/User/BjYLrFAb39TVe1ntbAYUqZ8y3Gq2
            FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                    .child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                              User loggeUuser =   dataSnapshot.getValue(User.class);
                              Common.currentLoggedUser = loggeUuser;

                                dissmissWaitingDialog();

                                Toast.makeText(LoginActivity.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        }else {
            Toast.makeText(this, "Not Logged", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitingDialog != null && waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
    }
}
