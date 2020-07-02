package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class AppShareActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgShareFacebook, imgShareTwitter, imgShareMessenger, imgShareWhatsapp, imgShareMore, imgLikeButton;
    private static final int SHARE_CODE = 11001;
    private static final int WHATSAPP_RESULT_CODE = 11002;
    private static final int MESSENGER_RESULT_CODE = 11003;
    private static final int TWITTER_RESULT_CODE = 11004;
    private FirebaseAuth mAuth;
    private FrameLayout frameLayoutNativeBellowFeatureImage;
    private ImageView imgBack;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_share);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        mAuth = FirebaseAuth.getInstance();

        frameLayoutNativeBellowFeatureImage = findViewById(R.id.frameLayoutNativeBellowFeatureImage);

        imgBack = findViewById(R.id.imgBack);

        imgShareFacebook = findViewById(R.id.imgShareFacebook);
        imgShareWhatsapp = findViewById(R.id.imgShareWhatsapp);
        imgShareMessenger = findViewById(R.id.imgShareMessenger);
        imgShareTwitter = findViewById(R.id.imgSharTwitter);
        imgShareMore = findViewById(R.id.imgShareMore);

        imgShareFacebook.setOnClickListener(this);
        imgShareWhatsapp.setOnClickListener(this);
        imgShareMessenger.setOnClickListener(this);
        imgShareTwitter.setOnClickListener(this);
        imgShareMore.setOnClickListener(this);

        imgBack.setOnClickListener(this);

        loadNativeAd(frameLayoutNativeBellowFeatureImage);
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();

        if (view.getId() == R.id.imgShareFacebook || view.getId() == R.id.imgShareMessenger
                || view.getId() == R.id.imgShareWhatsapp || view.getId() == R.id.imgSharTwitter
                || view.getId() == R.id.imgShareFacebook || view.getId() == R.id.imgShareMore){


            //waitingDialog.show();
            getDynamicLink(view.getId());

           /* if (mAuth != null){


                FirebaseDatabase.getInstance().getReference()
                        .child("User")
                        .child(mAuth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String referCode = dataSnapshot.child("myReferCode").getValue().toString();
                                    getDynamicLink(view.getId(), referCode);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("AppShareActivity", databaseError.getMessage());

                            }
                        });

            }else {
                Toast.makeText(this, "Not Logged", Toast.LENGTH_SHORT).show();
            }*/
        }else if (id == R.id.imgBack){
            finish();
        }
    }


    private void getDynamicLink(final int shareBtnId) {


        String referLink  ="https://psguide.page.link/?"+
                "link="+"https://www.mediacellapp.com"+
                "&apn="+getPackageName()+
                "&st="+"Install And Join PsGuide"+
                "&sd="+"Ps Guide"+
                "&si="+"https://i.ibb.co/N1RSJQN/splash.jpg";


        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(referLink))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            Log.d("SuccessHolo", task.getResult().getShortLink().toString());
                            // Short link created


                            Uri shortLink  = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            share(shortLink, shareBtnId);


                        } else {
                            Log.e("main", " error "+task.getException() );
                        }
                    }
                });



    }

    private void share(final Uri shortLink, int shareBtnId) {
        Log.d("AppShareActivity", "called share");
        String application = null;
        int sharecode = 0;
        if (shareBtnId == R.id.imgShareMessenger){
            application = "com.facebook.orca";
            sharecode = MESSENGER_RESULT_CODE;
        }else if (shareBtnId == R.id.imgShareWhatsapp){
            application = "com.whatsapp";
            sharecode = WHATSAPP_RESULT_CODE;
        }else if (shareBtnId == R.id.imgSharTwitter){
            application = "com.twitter.android";
            sharecode = TWITTER_RESULT_CODE;

        }else if (shareBtnId == R.id.imgShareFacebook){
            application = "com.facebook.katana";
            sharecode = TWITTER_RESULT_CODE;

        }

        final Intent intent = this.getPackageManager().getLaunchIntentForPackage(application);


        if (intent!=null){
            Log.d("AppShareActivity", "called intent not null");

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage(application);

            if (shareBtnId == R.id.imgShareWhatsapp){
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.whatsapp_share_text)+
                      " "+shortLink);
            }else if(shareBtnId == R.id.imgShareFacebook){
                facebookShare(shortLink);
            }else{
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.whatsapp_share_text)+
                                " "+shortLink);
            }

            sendIntent.setType("text/plain");
            startActivityForResult(sendIntent, SHARE_CODE);



        }else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,  getString(R.string.whatsapp_share_text)+
                    " "+shortLink);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }




    }


    private void facebookShare(Uri shortLink){
       // Log.d("FaceBookShare", "call");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(shortLink)
               .setQuote(getString(R.string.whatsapp_share_text))
                .build();
        shareDialog.show(content, ShareDialog.Mode.NATIVE);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

                Log.d("FaceBookShare", "Success");


            }

            @Override
            public void onCancel() {
                Log.d("FaceBookShare", "Cancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FacebookException", error.toString());
            }
        });
    }


    public void loadNativeAd(final FrameLayout frameLayout){

       /* final View view = getLayoutInflater()
                .inflate(R.layout.layout_native_ad, null);
        final UnifiedNativeAdView adView = view.findViewById(R.id.ad_view);*/

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_ad_unit))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Assumes you have a placeholder FrameLayout in your View layout
                        // (with id fl_adplaceholder) where the ad is to be placed.

                        // Assumes that your ad layout is in a file call ad_unified.xml
                        // in the res/layout folder
                        if (unifiedNativeAd!=null){
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                    .inflate(R.layout.layout_ad_unified, null);
                            // UnifiedNativeAdView adView = (UnifiedNativeAdView) , null);
                            // This method sets the text, images and the native ad, etc into the ad
                            // view.
                            populateNativeAdView(unifiedNativeAd,  adView);
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }
                    }
                });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.

        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        if (nativeAd.getHeadline()!=null)
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        else adView.getHeadlineView().setVisibility(View.GONE);
        if (nativeAd.getBody()!=null)
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        else adView.getBodyView().setVisibility(View.GONE);

        if (nativeAd.getCallToAction()!=null)
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        else adView.getCallToActionView().setVisibility(View.GONE);


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
