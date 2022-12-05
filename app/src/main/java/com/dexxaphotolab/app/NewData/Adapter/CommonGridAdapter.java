package com.dexxaphotolab.app.NewData.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dexxaphotolab.app.NewData.NewActivity.ActivitySingleCategoyList1;
import com.dexxaphotolab.app.NewData.NewActivity.ItemPosterActivity;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.LoginActivity;
import com.dexxaphotolab.app.NewData.model.CommonModels;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CommonGridAdapter extends RecyclerView.Adapter<CommonGridAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;
    private String status;
    ItemPosterActivity aa;
    EventListener mEventListener;


    public CommonGridAdapter(Context context, List<CommonModels> items, String status, ItemPosterActivity aa1) {
        this.items = items;
        ctx = context;
        aa = aa1;
        this.status = status;
    }



    @Override
    public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(OriginalViewHolder holder, final int position) {
        final CommonModels obj = items.get(position);

        new Admanager(ctx).loadAd();

        if (status.equals("festival")) {
            holder.releaseDateTv.setVisibility(View.VISIBLE);
            filter();
        } else {
            holder.releaseDateTv.setVisibility(View.GONE);
        }

        holder.qualityTv.setText(obj.getQuality());
        holder.releaseDateTv.setText(obj.getReleaseDate());
        holder.name.setText(obj.getTitle());

        Picasso.get().load(obj.getImageUrl()).placeholder(R.drawable.poster_placeholder).into(holder.image);


        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefsss = ctx.getSharedPreferences("subscibe11", MODE_PRIVATE);
                String substatus = prefsss.getString("subscribe", "0");

                if (substatus.equals("0")) {


                        new Admanager(ctx).showInterstitial(aa, new Admanager.GetBackPointer() {
                            @Override
                            public void returnAction() {

                                Log.e("xsx12", "returnAction: " + "xxxxxx");
                                if (PreferenceUtils.isLoggedIn(ctx)) {
                                    goToDetailsActivity(obj);
                                } else {
                                    ctx.startActivity(new Intent(ctx, LoginActivity.class));
                                }
                            }
                        });

                } else {

                    new Admanager(ctx).showInterstitial(aa, new Admanager.GetBackPointer() {
                        @Override
                        public void returnAction() {

                            Log.e("xsx12", "returnAction: " + "xxxxxx");
                            if (PreferenceUtils.isLoggedIn(ctx)) {
                                goToDetailsActivity(obj);
                            } else {
                                ctx.startActivity(new Intent(ctx, LoginActivity.class));
                            }
                        }
                    });

                }


            }
        });

//        setAnimation(holder.itemView, position);

    }


    private void goToDetailsActivity(CommonModels obj) {

        String id = obj.getId();
        String image = obj.getImageUrl();
        Log.e("mmmmmm", "goToDetailsActivity: " + image);
        Intent i = new Intent(ctx, ActivitySingleCategoyList1.class);
        i.putExtra("id", id);
        i.putExtra("image", obj.getImageUrl());
        ctx.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, qualityTv, releaseDateTv;
        public MaterialRippleLayout lyt_parent;
        LinearLayout ll_premium;
        public View view;
        public CardView cardView;

        public OriginalViewHolder(View v) {
            super(v);
            view = v;
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            qualityTv = v.findViewById(R.id.quality_tv);
            releaseDateTv = v.findViewById(R.id.release_date_tv);
            cardView = v.findViewById(R.id.top_layout);
            ll_premium = v.findViewById(R.id.ll_premium);
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }

        });

        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    private void filter() {
        Collections.sort(items, CommonModels.comparator);
    }

}