package com.dexxaphotolab.app.NewData.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dexxaphotolab.app.NewData.NewActivity.ActivitySingleCategoyList1;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.NewData.model.CommonModels;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.OriginalViewHolder> {

        Activity aa;
        String status;
        private List<CommonModels> list = new ArrayList<>();
        private List<CommonModels> expireList = new ArrayList<>();
        private Context ctx;
        private int lastPosition = -1;
        private boolean on_attach = true;
        private int animation_type = 24;


        public HomePageAdapter(Context context, List<CommonModels> items, Activity aa1, String name) {
                this.list = items;
                ctx = context;
                aa = aa1;
                status = name;

        }


        @Override
        public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                OriginalViewHolder vh;
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_view, parent, false);
                vh = new OriginalViewHolder(v);
                return vh;
        }

        private void filter() {
                Collections.sort(list, CommonModels.comparator);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final OriginalViewHolder holder, final int position) {

                final CommonModels obj = list.get(position);
                holder.name.setText(obj.getTitle());
                Log.e("xxxxx", "onBindViewHolder: " + obj.getImageUrl());
                Picasso.get().load(obj.getImageUrl()).placeholder(R.drawable.poster_placeholder).into(holder.image);

                if (status.equals("festival")) {
                        holder.releaseDateTv.setVisibility(View.VISIBLE);
                        filter();
                } else {
                        holder.releaseDateTv.setVisibility(View.GONE);
                }

                holder.qualityTv.setText(obj.getQuality());
                holder.releaseDateTv.setText(obj.getReleaseDate());
                Log.e("00000", "onBindViewHolder: " + obj.getReleaseDate());
                Log.e("00000", "onBindViewHolder: " + obj.getTitle());
                holder.tv_name.setText(obj.getTitle());

                holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Log.e("12121", "onClick: " + PreferenceUtils.isActivePlan(ctx));
                                SharedPreferences prefsss = ctx.getSharedPreferences("subscibe11", MODE_PRIVATE);
                                String substatus = prefsss.getString("subscribe", "0");

                                if (substatus.equals("0")) {

                                        String id = obj.getId();
                                        Log.e("hhhh", "onClick: " + id);
                                        Intent i = new Intent(ctx, ActivitySingleCategoyList1.class);
                                        i.putExtra("id", id);
                                        i.putExtra("image", obj.getImageUrl());
                                        i.putExtra("cat_name", obj.getTitle());
                                        ctx.startActivity(i);
                                } else {
                                        new Admanager(ctx).showInterstitial(aa, new Admanager.GetBackPointer() {
                                                @Override
                                                public void returnAction() {

                                                        String id = obj.getId();
                                                        Log.e("hhhh", "onClick: " + id);
                                                        Intent i = new Intent(ctx, ActivitySingleCategoyList1.class);
                                                        i.putExtra("id", id);
                                                        i.putExtra("image", obj.getImageUrl());
                                                        i.putExtra("cat_name", obj.getTitle());
                                                        ctx.startActivity(i);
                                                }
                                        });
                                }


                        }
                });

//        setAnimation(holder.itemView, position);
        }


        @Override
        public int getItemCount() {
                return list.size();
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

        public class OriginalViewHolder extends RecyclerView.ViewHolder {

                public ImageView image;
                public TextView name, qualityTv, releaseDateTv, tv_name;
                public MaterialRippleLayout lyt_parent;
                LinearLayout ll_premium;
                LinearLayout llMain;
                CountDownTimer timer;

                public OriginalViewHolder(View v) {
                        super(v);
                        image = v.findViewById(R.id.image);
                        name = v.findViewById(R.id.name);
                        tv_name = v.findViewById(R.id.tv_name);
                        lyt_parent = v.findViewById(R.id.lyt_parent);
                        llMain = v.findViewById(R.id.ll_main);
                        qualityTv = v.findViewById(R.id.quality_tv);
                        releaseDateTv = v.findViewById(R.id.release_date_tv);
                        ll_premium = v.findViewById(R.id.ll_premium);
                }
        }


}
