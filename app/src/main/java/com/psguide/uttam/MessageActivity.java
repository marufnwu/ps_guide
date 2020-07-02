package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.psguide.uttam.Notification.Service.NotificationFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageActivity extends AppCompatActivity {

    TextView txtNotiTitle, txtNotiMessage, txtNotiBody;
    ImageView imgNotiBanner;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        initView();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Notification")
                .child(id);

        if(id!=null){
            notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        NotificationFire notificationFire = new NotificationFire();
                        notificationFire = dataSnapshot.getValue(NotificationFire.class);

                        showNotification(notificationFire);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initView() {

         txtNotiTitle = findViewById(R.id.txtNotiTitle);
        txtNotiMessage = findViewById(R.id.txtNotiMessage);
        txtNotiBody = findViewById(R.id.txtNotiBody);

        imgNotiBanner = findViewById(R.id.imgNotiBanner);
    }

    private void showNotification(NotificationFire notificationFire) {

        if (notificationFire.iconUri!=null){
            Glide.with(MessageActivity.this)
                    .load(notificationFire.iconUri)
                    .placeholder(R.drawable.image_placeholder)
                    .into(imgNotiBanner);
        }

        if (notificationFire.title != null){
            txtNotiTitle.setText(notificationFire.title);
        }

        if (notificationFire.desc != null){
            txtNotiMessage.setText(notificationFire.desc);
        }else {
            txtNotiMessage.setVisibility(View.GONE);
        }

        if (notificationFire.body!=null){
            txtNotiBody.setText(notificationFire.body);
        }else {
            txtNotiBody.setVisibility(View.GONE);
        }

    }
}
