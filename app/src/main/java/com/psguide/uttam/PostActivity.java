package com.psguide.uttam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.psguide.uttam.Common.Common;
import com.psguide.uttam.Common.FirebaseString;
import com.psguide.uttam.Common.PostContinueDialog;
import com.psguide.uttam.Models.Like;
import com.psguide.uttam.Models.Post.Posts;
import com.psguide.uttam.Models.Share;
import com.psguide.uttam.Models.User;
import com.psguide.uttam.Retrofit.IRetrofitApiCall;
import com.psguide.uttam.Retrofit.RetrofitClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nex3z.notificationbadge.NotificationBadge;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isActivityRunning = false;
    private static final int SHARE_CODE = 11001;
    private static final int WHATSAPP_RESULT_CODE = 11002;
    private static final int MESSENGER_RESULT_CODE = 11003;
    private static final int TWITTER_RESULT_CODE = 11004;
    private TextView txtPostTitle, txtAuthorName, txtDate, txtPostContent;
   private ImageView imgFeatureImg;
    private WebView webView;
    private AlertDialog waitingDialog;
    private LinearLayout mainLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostRef;
    private NotificationBadge likeBadge;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private String timeRemaining;
    private  Posts post;

    private ImageView imgLike, imgDisLike;
    private TextView txtPostLikeCount, txtDislikeCount;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ImageView imgBack;
    private int id;

    private FrameLayout frameLayoutNativeBellowFeatureImage;
    private FrameLayout frameLayoutNativeBellowWebview;
    private InterstitialAd mInterstitialAd;
    private boolean isLiked = false;
    private View adView;



    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;
    private AdView mAdView;
    private PostContinueDialog postContinueDialog;
    private RewardedAd rewardedAd;
    private Dialog dialog;
    private boolean reRequestFlag;
    private InterstitialAd dialogInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FacebookSdk.sdkInitialize(getApplicationContext());
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_ad_unit));

        dialogInterstitialAd = new InterstitialAd(this);
        dialogInterstitialAd.setAdUnitId(getString(R.string.admob_dialog_inst_ad_unit));

        rewardedAd = new RewardedAd(this,
                getString(R.string.admob_video_ad_unit));

        shareDialog = new ShareDialog(PostActivity.this);

        waitingDialog = new SpotsDialog(PostActivity.this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 1);

        mAuth = FirebaseAuth.getInstance();
        mPostRef = FirebaseDatabase.getInstance().getReference(FirebaseString.Post);
       /// Log.d("currentuserId",  mAuth.getCurrentUser().getUid());

        if (mAuth==null){
            Toast.makeText(this, "You Are Not Logged", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }



        imgBack = findViewById(R.id.imgBack);

        imgLike = findViewById(R.id.imgLike);
        imgDisLike = findViewById(R.id.imgDislike);

        txtPostLikeCount = findViewById(R.id.txtPostLikeCount);
        txtDislikeCount = findViewById(R.id.txtPostDisLikeCount);

        imgBack.setOnClickListener(this);
        mainLayout = findViewById(R.id.activityPostLay);
        mainLayout.setVisibility(View.GONE);






        txtPostTitle = findViewById(R.id.txtPostDetTitle);
        txtDate = findViewById(R.id.txtPostDetDate);
        txtAuthorName = findViewById(R.id.txtPostDetAuthorName);
        imgFeatureImg = findViewById(R.id.imgPostDetFeature);

        //likeBadge = findViewById(R.id.likeBadge);





        frameLayoutNativeBellowFeatureImage = findViewById(R.id.frameLayoutNativeBellowFeatureImage);
        frameLayoutNativeBellowWebview = findViewById(R.id.frameLayoutNativeBellowWebview);




        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressLikeDislike(R.id.imgLike);
            }
        });

        imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressLikeDislike(R.id.imgDislike);
            }
        });



        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);

        webView = findViewById(R.id.webview);

         //webView.setScrollContainer(false);

        mWebViewClient = new myWebViewClient();
        webView.setWebViewClient(mWebViewClient);

        mWebChromeClient = new myWebChromeClient();
        //webView.setWebChromeClient(new MyChrome());
        webView.setWebChromeClient(mWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Nullable
            @Override
            public Bitmap getDefaultVideoPoster() {
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image_placeholder);
                return icon;
            }
        });

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);




        loadData(id);
        postContinueDialog = new PostContinueDialog(this);
        //getLoggeduserData();

        //initLikeDislike();

       //loadFbNativeAd();
        loadInsAd();
        loadDialogInsAd();



       // loadVideoAd(false);


        // loadNativeAd(frameLayoutNativeBellowWebview);
        //getDateDiff();

    }

    private void loadDialogInsAd() {
        dialogInterstitialAd.loadAd(new AdRequest.Builder().build());

        dialogInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();

                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });


    }

    private void loadVideoAd(boolean reRequestFlag) {
        this.reRequestFlag = reRequestFlag;
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    private void showVideoAd(){
        rewardedAd.show(PostActivity.this, adCallback);
    }


    RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
        @Override
        public void onRewardedAdLoaded() {
            // Ad successfully loaded.

            Log.d("RewardedVideoAd", "VideoAdLoadedSuccessFully");

        }

        @Override
        public void onRewardedAdFailedToLoad(int errorCode) {
            // Ad failed to load.

            Log.d("RewardedVideoAd", "VideoAdLoadingError : Reason =>"+ errorCode);

        }
    };

    RewardedAdCallback adCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.

            if (waitingDialog.isShowing()){
                waitingDialog.dismiss();
            }
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
          //  onBackPressed();

            reRequestFlag = false;

            if (dialog !=null && dialog.isShowing()){

                if (waitingDialog.isShowing()){
                    waitingDialog.dismiss();
                }

                dialog.dismiss();
                onBackPressed();
            }
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.

            reRequestFlag = false;

            if (dialog !=null && dialog.isShowing()){

                if (waitingDialog.isShowing()){
                    waitingDialog.dismiss();
                }

                dialog.dismiss();
            }
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display.

            onBackPressed();
        }

    };

    private void initCountDown() {

       countDownTimer =  new CountDownTimer(60000, 1000) {


            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");

                if (countDownTimer != null){
                    showContinueDialog();
                }
            }
        }.start();
    }

    private void showContinueDialog() {
        dialog = new Dialog(PostActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_post_continue);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                webView.onPause();
            }
        });


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardedAd.isLoaded()){
                    showVideoAd();
                }else{
                   if (dialogInterstitialAd.isLoaded()){
                       dialogInterstitialAd.show();
                   }else {
                       dialog.dismiss();
                   }
                }
            }
        });


        

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if(isActivityRunning){
            dialog.show();
        }
    }


    private void initLikeDislike() {

        if (mAuth.getUid() != null) {
            final DatabaseReference likeRef = mPostRef.child(String.valueOf(id)).child("like");
            DatabaseReference thisPostLikeRef = likeRef.child(mAuth.getUid()).getRef();
            final DatabaseReference disLikeRef = mPostRef.child(String.valueOf(id)).child("dislike");
            DatabaseReference thisPostDislikeRef = disLikeRef.child(mAuth.getUid()).getRef();

            likeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    txtPostLikeCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            disLikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    txtDislikeCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            thisPostDislikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        imgDisLike.setImageDrawable(getDrawable(R.drawable.dislike_active));
                    } else {
                        imgDisLike.setImageDrawable(getDrawable(R.drawable.dislike));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            thisPostLikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        imgLike.setImageDrawable(getDrawable(R.drawable.like_active));
                    } else {
                        imgLike.setImageDrawable(getDrawable(R.drawable.like1));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void loadInsAd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void getLoggeduserData() {
        if (mAuth.getUid()!=null){
            FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                    .child(mAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Common.currentLoggedUser = dataSnapshot.getValue(User.class);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void pressLikeDislike(int btnId){
        if (btnId == R.id.imgLike){
            pressLikeButton(id);

        }else if (btnId == R.id.imgDislike){
            pressDislike(id);
        }
    }

    private void pressDislike(int postId) {
        final DatabaseReference thisPostRef =  mPostRef.child(String.valueOf(postId)).getRef();
        thisPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final DatabaseReference likeRef =   dataSnapshot.child("dislike").child(mAuth.getUid()).getRef();
                likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            //not like before so able to like thi user

                            mPostRef.child(String.valueOf(id)).child("like").
                            child(mAuth.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                dataSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                            FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                            if (userDataSnapshot.exists()){
                                                String loggedUserName = userDataSnapshot.child("fullName")
                                                        .getValue().toString();

                                                Like like = new Like();
                                                like.userId = mAuth.getUid();
                                                like.userName = loggedUserName;
                                                like.timestamp = Common.getCurrentTimeStamp();

                                                likeRef.setValue(like)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                // Toast.makeText(PostActivity.this, "You Liked It", Toast.LENGTH_SHORT).show();

                                                                // addLikeRewardTransaction();
                                                                initLikeDislike();

                                                            }
                                                        });



                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                        }else {
                            //already like
                           // Toast.makeText(PostActivity.this, "You can't like more than once", Toast.LENGTH_SHORT).show();

                            dataSnapshot.getRef().removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           // Toast.makeText(PostActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                                            initLikeDislike();
                                        }
                                    });
                            //pressLikeButton(id);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void pressLikeButton(int postid) {
       final DatabaseReference thisPostRef =  mPostRef.child(String.valueOf(postid)).getRef();
       thisPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 final DatabaseReference likeRef =   dataSnapshot.child("like").child(mAuth.getUid()).getRef();
                           likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                   if (!dataSnapshot.exists()){
                                       //not like before so able to like thi user
                                       mPostRef.child(String.valueOf(id)).child("dislike").
                                               child(mAuth.getUid())
                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       if (dataSnapshot.exists()){
                                                           dataSnapshot.getRef().removeValue();
                                                       }
                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }
                                               });

                                       FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid())
                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot userDataSnapshot) {
                                                    if (userDataSnapshot.exists()){
                                                        String loggedUserName = Objects.requireNonNull(userDataSnapshot.child("fullName")
                                                                .getValue()).toString();

                                                        Like like = new Like();
                                                        like.userId = mAuth.getUid();
                                                        like.userName = loggedUserName;
                                                        like.timestamp = Common.getCurrentTimeStamp();

                                                        likeRef.setValue(like)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        //Toast.makeText(PostActivity.this, "You Liked It", Toast.LENGTH_SHORT).show();

                                                                        // addLikeRewardTransaction();
                                                                        initLikeDislike();
                                                                    }
                                                                });

                                                    }



                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }
                                               });


                                   }else {
                                       //already like
                                       //Toast.makeText(PostActivity.this, "You can't like more than once", Toast.LENGTH_SHORT).show();

                                        dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                initLikeDislike();
                                            }
                                        });
                                       // pressDislike(id);
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void loadData(final int id) {

        waitingDialog.show();

        IRetrofitApiCall retrofitApiCall = RetrofitClient.getRetrofit().create(IRetrofitApiCall.class);
        Call<Posts> getPostById = retrofitApiCall.getPostById(id);

        getPostById.enqueue(new Callback<Posts>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.isSuccessful()){
                    if (response.code() == 200){

                         post = response.body();
                        webView.loadDataWithBaseURL(null, "<html><head><style>img{max-width: 100%; width:auto; height: auto;}" +
                                "iframe{max-width: 100%; min-width:100%; height: 250px;}</style></head><body>"+
                                response.body().getContent().getRendered()+
                                "</body></html>", "text/html", "UTF-8", null);

                            if (Common.isValidContextForGlide(getApplicationContext())){
                                Glide.with(getApplicationContext())
                                        .load(post.getJetpackFeaturedMediaUrl())
                                        .placeholder(R.drawable.image_placeholder)
                                        .into(imgFeatureImg);
                            }

                        txtPostTitle.setText(Html.fromHtml(post.getTitle().getRendered()));
                        txtDate.setText(Common.dateFormatter(post.getDate()));


                        mainLayout.setVisibility(View.VISIBLE);
                        dissmissWaitingDialog();






                   //https://digital-wb.firebaseio.com/Post/1405

                   /* DatabaseReference viewsRef = FirebaseDatabase.getInstance().getReference(FirebaseString.Post)
                                .child(String.valueOf(id))
                                .child(FirebaseString.Views);


                        com.psguide.uttam.Models.View view = new com.psguide.uttam.Models.View();
                        view.timestamp = Common.getCurrentTimeStamp();
                        if (Common.currentLoggedUser!= null){
                            view.userName = Common.currentLoggedUser.fullName == null ? "N/A" : Common.currentLoggedUser.fullName;
                            view.userId = mAuth.getUid() == null ? "N/A" : mAuth.getUid();
                        }else {
                            view.userId = "";
                            view.userName ="";
                        }



                                viewsRef.child(viewsRef.push().getKey())
                                        .setValue(view);*/



                        //checkLikeOrNot();

                        //setHitcount(getDateDiff());


                        //initCountDown();


                    }
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                //Log.d("PostRetriveError", t.getMessage());
                Toast.makeText(PostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                dissmissWaitingDialog();

            }
        });
    }






    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.imgBack){
           onBackPressed();
       }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();


            if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAuth!=null){
            Common.setOnlineStatus(mAuth.getUid(), false);
        }

        if (countDownTimer !=null){
            countDownTimer.cancel();
        }


        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }

        if (waitingDialog != null && waitingDialog.isShowing()){
            waitingDialog.dismiss();
        }
    }


    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        isActivityRunning = false;
        webView.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        isActivityRunning = true;
        webView.onResume();

    }



    @Override
    protected void onStart() {
        super.onStart();


       /* if (mAuth.getUid()!=null){
            Common.setOnlineStatus(mAuth.getUid(), true);
        }else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        if (inCustomView()) {
            hideCustomView();
        }
    }


    class myWebChromeClient extends WebChromeClient {



        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {

            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image_placeholder);
            return icon;
        }


    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d("LinkHost", Uri.parse(url).getHost());

            String linkHost = Uri.parse(url).getHost();
            Uri uri = Uri.parse(url);

            if (linkHost.equals("play.google.com")){
                String appId = uri.getQueryParameter("id");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id="+appId));
                startActivity(intent);

                return true;
            }else if(linkHost.equals("www.youtube.com")){
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

                return true;
            }

            return false;
        }
    }



}
