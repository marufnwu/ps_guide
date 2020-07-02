package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.psguide.uttam.Common.FirebaseString;
import com.psguide.uttam.Models.PPayment.BankMethod;
import com.psguide.uttam.Models.PPayment.PhoneMethod;
import com.psguide.uttam.Models.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtFullName, edtEmail, edtPhone, edtCountry, edtAddress, edtGender;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private Button btnUpdate;

    private SpotsDialog waitingDialog;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_ad_unit));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        if (mAuth!=null){
            setExitsData(mAuth);
        }


        //rdioPaytm.setChecked(true);

        btnUpdate.setOnClickListener(this);
        waitingDialog = new SpotsDialog(this);




    }


    private void setExitsData(FirebaseAuth mAuth) {

        final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference(FirebaseString.User).child(mAuth.getUid());

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = new User();

                    user = dataSnapshot.getValue(User.class);
                    edtFullName.setText(user.fullName);
                    edtEmail.setText(user.email);
                    edtPhone.setText(user.phone);
                    if (!user.address.isEmpty()){
                        edtAddress.setText(user.address);
                    }
                    if (!user.country.isEmpty()){
                        edtCountry.setText(user.getCountry());
                    }
                    if (!user.gender.isEmpty()){
                        edtGender.setText(user.getGender());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtCountry = findViewById(R.id.edtCountry);
        edtAddress = findViewById(R.id.edtAddress);
        edtGender = findViewById(R.id.edtGender);


        btnUpdate = findViewById(R.id.btnUpdate);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id== R.id.btnUpdate){
           // updtateData();
        }
    }


}
