package com.psguide.uttam.Common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.psguide.uttam.R;

public class PostContinueDialog extends Dialog implements View.OnClickListener {

    Button btnYes, btnNo;
    Context context;
    UnifiedNativeAdView frameLayout;

    public PostContinueDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_post_continue);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);
        //frameLayout = findViewById(R.id.frameLayoutDialogNativeAd);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        super.setOnShowListener(listener);
        Log.d("OnShow", "Showing");

    }

    public void setNativeAd(UnifiedNativeAd adView){
        //frameLayout.removeAllViews();
        frameLayout.setNativeAd(adView);

    }

}
