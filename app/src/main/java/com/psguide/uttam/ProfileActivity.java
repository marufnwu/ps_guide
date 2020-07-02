package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.psguide.uttam.Common.FirebaseString;
import com.psguide.uttam.Models.PPayment.BankMethod;
import com.psguide.uttam.Models.PPayment.PhoneMethod;
import com.psguide.uttam.Models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imgProfilePic;
    private TextView txtProfileName, txtProfileNum, txtProfileEmail;
    private CircleImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FloatingActionButton floatingEditProfile;


    private ImageView imgBack;
    private SpotsDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        waitingDialog = new SpotsDialog(this);


        if (mAuth!=null){

            loadData(mUser);
        }else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

        floatingEditProfile.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }


    private void loadData(FirebaseUser mUser) {


        final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                .child(mUser.getUid());

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    User user = new User();
                    user = dataSnapshot.getValue(User.class);

                    txtProfileName.setText(user.fullName);
                    txtProfileNum.setText(user.phone);
                    txtProfileEmail.setText(user.email);

                    Glide.with(ProfileActivity.this).load(user.photoUrl).into(imgProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initView() {
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileNum = findViewById(R.id.txtProfileNum);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);

        profilePic = findViewById(R.id.imgProfilePic);


        floatingEditProfile = findViewById(R.id.floatingEditProfile);



        imgBack = findViewById(R.id.imgBack);
        imgProfilePic = findViewById(R.id.imgProfilePic);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.floatingEditProfile){
           // startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        }else if (id == R.id.imgBack){
            onBackPressed();
        }
    }
}
