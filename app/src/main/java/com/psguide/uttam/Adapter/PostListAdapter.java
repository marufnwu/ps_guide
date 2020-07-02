package com.psguide.uttam.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psguide.uttam.Common.Common;
import com.psguide.uttam.Interface.OnPostItemClickListener;
import com.psguide.uttam.Models.Post.Posts;
import com.psguide.uttam.R;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    private static final int MENU_ITEM_VIEW_TYPE = 2;
    private  InterstitialAd mInterstitialAd;
    List<Object> postsList;
    Context context;
    private Integer postId;
    OnPostItemClickListener onPostItemClickListener;

    public PostListAdapter(List<Object> postsList, Context context, OnPostItemClickListener onPostItemClickListener) {
        this.postsList = postsList;
        this.context = context;
        this.onPostItemClickListener = onPostItemClickListener;
        Log.d("itemSize", ""+postsList.size());


       /* mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        initIntAdListener();
        if (!mInterstitialAd.isLoaded()){
           loadIntAd();
        }*/
    }



    private void loadIntAd() {
       if (!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()){
           loadIntAd();
       }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(context).inflate(R.layout.layout_native_ad,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                // Fall through.
            default:
                View view = LayoutInflater.from(context).inflate(
                        R.layout.layout_post_item, parent, false);
                return new PostItemViewHolder(view);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) postsList.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) viewHolder).getAdView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                final PostItemViewHolder holder = (PostItemViewHolder) viewHolder;

                final Posts post = (Posts) postsList.get(position);
                Log.d("onViewholder", post.getId().toString());
                Glide.with(context)
                        .load(post.getJetpackFeaturedMediaUrl())
                        .placeholder(R.drawable.image_placeholder)
                        .into(holder.featureImg);

                String title = (post.getTitle().getRendered());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.txtTitle.setText(Html.fromHtml("<p>"+title+"</p>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.txtTitle.setText(Html.fromHtml("<p>"+title+"</p>"));
                }

                if (Common.dateFormatter(post.getDate()) != null){
                    holder.txtDate.setText(Common.dateFormatter(post.getDate()));
                }

                holder.postItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        postId = post.getId();

                        onPostItemClickListener.onItemClick(postId);


                    }
                });



                break;

                default:



        }



    }

    private void seView(final TextView txtPostViews, Integer postId) {
        FirebaseDatabase.getInstance().getReference()
                .child("Post")
                .child(String.valueOf(postId))
                .child("Views")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                           long count =  dataSnapshot.getChildrenCount();

                           txtPostViews.setText((int) count);
                            Log.d("Views ", ""+count);

                        }else{
                            Log.d("Views ", "not exits");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Views ", databaseError.getMessage());

                    }
                });
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = postsList.get(position);

        if (recyclerViewItem instanceof UnifiedNativeAd) {
            Log.d("ViewType", "UNIFIED_NATIVE_AD_VIEW_TYPE");

            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        Log.d("ViewType", "MENU_ITEM_VIEW_TYPE");

        return MENU_ITEM_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        Log.d("itemSize", ""+postsList.size());
        return postsList.size();
    }



    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

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

    public void addItem(List<Object> postsList) {

        int prevSize = this.postsList.size();

        this.postsList.addAll(postsList);
        notifyDataSetChanged();
        //this.notifyItemRangeInserted(prevSize, this.postsList.size() -prevSize);
    }

   public class  PostItemViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtDate, txtAuthorName, txtPostViews;
        ImageView authorImage, featureImg;
        LinearLayout postItemLayout;


        public PostItemViewHolder(@NonNull View itemView) {
            super(itemView);



            txtTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostViews = itemView.findViewById(R.id.txtPostViews);
            // txtAuthorName = itemView.findViewById(R.id.txtPostAuthor);
            txtDate = itemView.findViewById(R.id.txtPostDate);

            // authorImage = itemView.findViewById(R.id.imgPostAuthor);
            featureImg = itemView.findViewById(R.id.imgPostFeature);

            postItemLayout = itemView.findViewById(R.id.postItemLayout);




        }

    }

   public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getAdView() {
            return adView;
        }

        UnifiedNativeAdViewHolder(View view) {
            super(view);
            adView = (UnifiedNativeAdView) view.findViewById(R.id.ad_view);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
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
        }
    }

    private void initIntAdListener() {

    }
}



