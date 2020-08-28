package com.psguide.uttam;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DatabaseReference;
import com.inmobi.ads.AdMetaInfo;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;
import com.psguide.uttam.Adapter.PostListAdapter;
import com.psguide.uttam.Common.Common;
import com.psguide.uttam.Common.FirebaseString;
import com.psguide.uttam.Common.SpaceItemDecoration;

import com.psguide.uttam.Common.UserStatusService;
import com.psguide.uttam.Interface.ITemClickListener;
import com.psguide.uttam.Interface.OnPostItemClickListener;
import com.psguide.uttam.Models.Activity;
import com.psguide.uttam.Models.InMobi;
import com.psguide.uttam.Models.Post.Posts;
import com.psguide.uttam.Models.User;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.psguide.uttam.Retrofit.IRetrofitApiCall;
import com.psguide.uttam.Retrofit.RetrofitClient;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import dmax.dialog.SpotsDialog;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ITemClickListener, OnPostItemClickListener {

    boolean doubleBackToExitPressedOnce = false;
    // The number of native ads to load.
    public static final int NUMBER_OF_ADS = 3;

    // The AdLoader used to load ads.
    private AdLoader adLoader;

    // List of MenuItems and native ads that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    private static final String TOPIC_ALL_USER = "ALL_USER";
    private RecyclerView recyclerRecentPost;
    private ImageView imgDrawer;
    private boolean isdrawerOpen = false;

    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    ImageView imgNavProfile;
    TextView txtNavUserName, txtNavUserEmail;

    int view_thresold = 10;
    private LinearLayoutManager layoutManager;
    int pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;

    private int PAGE = 1, PAGE_SIZE = 10, TOTAL_PAGE = 0;
    boolean isLoading = true;
    private PostListAdapter adapter;
    private SpotsDialog waitingDialog;
    private InterstitialAd mInterstitialAd;
    private SpinKitView spinKitView;
    ProgressBar postListProgress;
    private Integer postId;
    private LinearLayout layoutReload;
    private Button btnReload;
    public InterstitialAdEventListener mInterstitialAdEventListener;
    private InMobiInterstitial inMobiinterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        mInterstitialAdEventListener = new InterstitialAdEventListener() {
            @Override
            public void onAdFetchFailed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdFetchFailed(inMobiInterstitial, inMobiAdRequestStatus);
                Log.d("InMobi", "onAdFetchFailed");
            }

            @Override
            public void onAdWillDisplay(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdWillDisplay(inMobiInterstitial);
                Log.d("InMobi", "onAdWillDisplay");

            }

            @Override
            public void onAdDisplayed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
                super.onAdDisplayed(inMobiInterstitial, adMetaInfo);
                Log.d("InMobi", "onAdDisplayed");

            }

            @Override
            public void onAdDisplayFailed(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdDisplayFailed(inMobiInterstitial);
                Log.d("InMobi", "onAdDisplayFailed");

            }

            @Override
            public void onAdDismissed(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdDismissed(inMobiInterstitial);
                Log.d("InMobi", "onAdDismissed");
                inMobiInterstitial.load();

                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.putExtra("id", postId);
                startActivity(intent);

            }

            @Override
            public void onUserLeftApplication(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onUserLeftApplication(inMobiInterstitial);
            }

            @Override
            public void onRewardsUnlocked(@NonNull InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                super.onRewardsUnlocked(inMobiInterstitial, map);
            }

            @Override
            public void onAdLoadSucceeded(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
                super.onAdLoadSucceeded(inMobiInterstitial, adMetaInfo);
                Log.d("InMobi", "onAdLoadSucceeded");

            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus);
                Log.d("InMobi", "onAdLoadFailed "+inMobiAdRequestStatus.getMessage());

            }
        };
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        inMobiinterstitialAd = new InMobiInterstitial(MainActivity.this, InMobi.interstitial, mInterstitialAdEventListener);



        /*if (BuildConfig.DEBUG) {
            AdSettings.setTestMode(true);
        }*/

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_ad_unit));

        inMobiinterstitialAd.load();
        loadIntsAd();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        imgDrawer = findViewById(R.id.imgDrawerArrow);


        View navH = navigationView.getHeaderView(0);
        imgNavProfile = navH.findViewById(R.id.navImgProfile);
        txtNavUserName = navH.findViewById(R.id.txtNavUserName);
        txtNavUserEmail = navH.findViewById(R.id.textNavUserEmail);

        spinKitView =  findViewById(R.id.spin_kit);
        setKitView();
        layoutReload = findViewById(R.id.layoutReload);
        btnReload = findViewById(R.id.btnReloadPostList);
        postListProgress = findViewById(R.id.postListProgress);


        recyclerRecentPost = findViewById(R.id.recyclerAllPosts);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerRecentPost.setLayoutManager(layoutManager);
        recyclerRecentPost.addItemDecoration(new SpaceItemDecoration(4));
        recyclerRecentPost.hasFixedSize();

        mAuth = FirebaseAuth.getInstance();

       /* Intent intent = new Intent(MainActivity.this, UserStatusService.class);
        startService(intent);*/

        setFcmToken();
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_ALL_USER);

        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (isdrawerOpen){
                   drawer.closeDrawer(Gravity.LEFT);
               }else {
                   drawer.closeDrawer((int)Gravity.START);

               }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.menuHome){
                    drawer.closeDrawer(GravityCompat.START);
                }/*else if (id == R.id.menuProfile){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }*/else if (id == R.id.menuShare){
                    startActivity(new Intent(MainActivity.this, AppShareActivity.class));

                }

                return false;
            }
        });




        waitingDialog = new SpotsDialog(MainActivity.this);
        waitingDialog.setCancelable(false);



        //getUserProfile();

        //loadRecentPost();
        //Common.deleteMyAccount(mAuth.getUid());



        getPost();
        loadIntsAd();
        //loadNativeAds();




        recyclerRecentPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (dy>0){

                   if (!isLoading ){

                       if (PAGE <= TOTAL_PAGE){
                           Log.d("Pagination", "Total Page "+TOTAL_PAGE);
                           Log.d("Pagination", "Page "+PAGE);
                           if ( (visibleItemCount + pastVisibleItem) >= totalItemCount)
                           {
                               postListProgress.setVisibility(View.VISIBLE);
                               isLoading = true;
                               Log.v("...", "Last Item Wow !");
                               //Do pagination.. i.e. fetch new data
                               PAGE++;
                               if (PAGE<=TOTAL_PAGE){
                                   doPagination();
                               }else {
                                   isLoading = false;
                                   postListProgress.setVisibility(View.GONE);
                               }
                           }
                       }else{

                           //postListProgress.setVisibility(View.GONE);
                           Log.d("Pagination", "End of page");
                       }
                   }
                }
            }
        });


        //printHashKey(this);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                loadIntsAd();

                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.putExtra("id", postId);
                startActivity(intent);
            }
        });

        mInterstitialAdEventListener = new InterstitialAdEventListener() {
            @Override
            public void onAdFetchFailed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdFetchFailed(inMobiInterstitial, inMobiAdRequestStatus);
                Log.d("InMobi", "onAdFetchFailed");
            }

            @Override
            public void onAdWillDisplay(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdWillDisplay(inMobiInterstitial);
                Log.d("InMobi", "onAdWillDisplay");

            }

            @Override
            public void onAdDisplayed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
                super.onAdDisplayed(inMobiInterstitial, adMetaInfo);
                Log.d("InMobi", "onAdDisplayed");

            }

            @Override
            public void onAdDisplayFailed(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdDisplayFailed(inMobiInterstitial);
                Log.d("InMobi", "onAdDisplayFailed");

            }

            @Override
            public void onAdDismissed(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onAdDismissed(inMobiInterstitial);
                Log.d("InMobi", "onAdDismissed");

            }

            @Override
            public void onUserLeftApplication(@NonNull InMobiInterstitial inMobiInterstitial) {
                super.onUserLeftApplication(inMobiInterstitial);
            }

            @Override
            public void onRewardsUnlocked(@NonNull InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                super.onRewardsUnlocked(inMobiInterstitial, map);
            }

            @Override
            public void onAdLoadSucceeded(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull AdMetaInfo adMetaInfo) {
                super.onAdLoadSucceeded(inMobiInterstitial, adMetaInfo);
                Log.d("InMobi", "onAdLoadSucceeded");

            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiInterstitial inMobiInterstitial, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus);
                Log.d("InMobi", "onAdLoadFailed "+inMobiAdRequestStatus.getMessage());

            }
        };


        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("IsOnline", "online");
                } else {
                    Log.d("IsOnline", "offline");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });


    }




    private void setKitView() {
        Sprite animation;
        Random random = new Random();
        int item = random.nextInt(12 - 1 + 1) + 1;

        switch (item){
            case 1:
                animation = new Wave();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                break;
            case 2:
                animation = new RotatingPlane();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.red));

                break;
            case 3:
                animation = new DoubleBounce();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.red));

                break;
            case 4:
                animation = new WanderingCubes();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

                break;
            case 5:
                animation = new Pulse();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.aqua));

                break;
            case 6:
                animation = new ChasingDots();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.purple));

                break;

            case 7:
                animation = new ThreeBounce();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.olive));

                break;
            case 8:
                animation = new Circle();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.purple));

                break;
            case 9:
                animation = new CubeGrid();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.aqua));

                break;
            case 10:
                animation = new FadingCircle();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.lime));

                break;
            case 11:
                animation = new FoldingCube();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.fuchsia));

                break;
            case 12:
                animation = new RotatingCircle();
                spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.yellow));

                break;
                default:
                    animation = new Pulse();
                    spinKitView.setColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));





        }




        spinKitView.setIndeterminateDrawable(animation);
    }

    private void loadIntsAd() {
        if(!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private void doPagination() {
        postListProgress.setVisibility(View.VISIBLE);

        IRetrofitApiCall iRetrofitApiCall = RetrofitClient.getRetrofit().create(IRetrofitApiCall.class);
        iRetrofitApiCall.getPosts(PAGE, PAGE_SIZE)
                .enqueue(new Callback<List<Posts>>() {
                    @Override
                    public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                        if (response.body()!=null){

                            Headers header = response.headers();
                            int totalPage = Integer.parseInt(header.get("X-WP-TotalPages"));


                            List<Posts> posts = response.body();

                            if (posts.isEmpty()){
                                postListProgress.setVisibility(View.GONE);
                            }

                            List<Object> objectList = new ArrayList<>();
                            objectList.addAll(posts);
                            //adapter.addItem(objectList);

                           loadNativeAds(objectList);


                        }


                    }

                    @Override
                    public void onFailure(Call<List<Posts>> call, Throwable t) {
                        isLoading = false;
                        postListProgress.setVisibility(View.GONE);
                    }
                });

    }

    private void getPost() {


        spinKitView.setVisibility(View.VISIBLE);
        //postListProgress.setVisibility(View.VISIBLE);

        IRetrofitApiCall iRetrofitApiCall = RetrofitClient.getRetrofit().create(IRetrofitApiCall.class);
        iRetrofitApiCall.getPosts(PAGE, PAGE_SIZE)
                .enqueue(new Callback<List<Posts>>() {
                    @Override
                    public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                        if (response.body()!=null){

                            if (response.body().size()>0){

                                Headers header = response.headers();
                                TOTAL_PAGE = Integer.parseInt(header.get("X-WP-TotalPages"));
                                 List<Object> postsList = new ArrayList<>();


                                List<Posts> posts = response.body();
                                postsList.addAll(posts);

                                Log.d("postListSize", postsList.size()+"");
                                //adapter = new PostListAdapter(postsList, MainActivity.this);

                                loadNativeAds(postsList);

                                //updateItemWithNativeAd(postsList);

                                //recyclerRecentPost.setAdapter(adapter);
                            }else {
                                showReloadLayout();
                            }

                        }else {
                            showReloadLayout();
                        }


                    }

                    @Override
                    public void onFailure(Call<List<Posts>> call, Throwable t) {
                        spinKitView.setVisibility(View.GONE);

                        isLoading = false;
                        postListProgress.setVisibility(View.GONE);
                        showReloadLayout();
                    }
                });

    }

    private void showReloadLayout() {
        layoutReload.setVisibility(View.VISIBLE);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutReload.setVisibility(View.GONE);
                isLoading = true;
                postListProgress.setVisibility(View.VISIBLE);
                getPost();
            }
        });
    }

    private void getUserProfile() {

        if (mAuth.getUid()!=null){
            FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                    .child(mAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                User user = new User();
                                user = dataSnapshot.getValue(User.class);

                               if (!isFinishing()){
                                   Glide.with(getApplicationContext()).load(user.photoUrl).into(imgNavProfile);
                               }
                                txtNavUserName.setText(user.fullName);
                                txtNavUserEmail.setText(user.email);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }



    @Override
    public void onClick(View view) {



    }


    @Override
    public void onItemClick(int pos, int id) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private void setFcmToken() {


        if (mAuth.getUid()!=null){

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {

                    String fcmToken = "";

                    if (task.isSuccessful()){
                        fcmToken = task.getResult().getToken();

                        final String finalFcmToken = fcmToken;
                        FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                                .child(mAuth.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            FirebaseDatabase.getInstance().getReference(FirebaseString.User)
                                                    .child(mAuth.getUid())
                                                    .child(FirebaseString.fcmToken)
                                                    .setValue(finalFcmToken);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }



                }
            });
        }


    }

    private void loadNativeAds(final List<Object> objectList) {
        mNativeAds.clear();
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_ad_unit));

        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.

                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            updateItemWithNativeAd(objectList);
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("NativeAdFaild", errorCode+"");
                        //updateItemWithNativeAd(objectList);
                        if (!adLoader.isLoading()) {
                            updateItemWithNativeAd(objectList);
                        }else {
                            Log.e("NativeAdFaild", "Ad loading");

                        }
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }

    private void updateItemWithNativeAd(List<Object> objectList) {
        Log.d("ObjectListSize", ""+objectList.size());
        if (mNativeAds.size() <= 0) {
            Log.d("NativeAdSize", mNativeAds.size()+"");

            loadRecycler(objectList);
        }else {
            Log.d("NativeAdSize", mNativeAds.size()+"");
            int offset = 6;
            int index = 1;
            for (UnifiedNativeAd ad : mNativeAds) {
                if (index<objectList.size()){
                    objectList.add(index, ad);
                    index = index + offset;
                }else {
                    break;
                }
            }

            loadRecycler(objectList);

        }


    }

    private void loadRecycler(List<Object> objectList) {
        spinKitView.setVisibility(View.GONE);

        if (PAGE == TOTAL_PAGE){
            postListProgress.setVisibility(View.GONE);
        }

        if (adapter==null){
            isLoading = false;
            postListProgress.setVisibility(View.GONE);
            adapter = new PostListAdapter(objectList, MainActivity.this, MainActivity.this);
            recyclerRecentPost.setAdapter(adapter);
        }else {

            adapter.addItem(objectList);
            isLoading = false;
            postListProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("hashkey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("hashkey", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("hashkey", "printHashKey()", e);
        }
    }


    @Override
    public void onItemClick(Integer postId) {

       this.postId = postId;

        if (mInterstitialAd.isLoaded()){
            Log.d("InterStitialAd", "Admob Interstitial Ad Loaded");

            mInterstitialAd.show();
        }else {
            Log.d("InterStitialAd", "Admob Interstitial Ad Not Loaded");

            loadIntsAd();

            if (inMobiinterstitialAd.isReady()){
                inMobiinterstitialAd.show();

            }else {
                inMobiinterstitialAd.load();
                Log.d("InterStitialAd", "Inmobi Interstitial  Ad not Loaded");
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.putExtra("id", postId);
                startActivity(intent);
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAuth!=null){
            Common.setOnlineStatus(mAuth.getUid(), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* if (mAuth.getUid()!=null){
            Common.setOnlineStatus(mAuth.getUid(), true);
        }else {
            startActivity(new Intent(this, PostActivity.class));
            finish();
        }*/
    }

}
