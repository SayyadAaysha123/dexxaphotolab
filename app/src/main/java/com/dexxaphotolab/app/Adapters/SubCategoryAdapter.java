package com.dexxaphotolab.app.Adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.dexxaphotolab.app.Activities.ActivitySingleVideoList;
import com.dexxaphotolab.app.Model.SubCategoryModel;
import com.dexxaphotolab.app.NewData.NewActivity.ActivitySingleCategoyList1;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.ItemAnimation;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.RecyclerViewHolder> {
    private List<SubCategoryModel> mArrayList;
    private Context mContext;
    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;
    ActivitySingleCategoyList1 aa;
    EventListener mEventListener;

    public SubCategoryAdapter(Context context, List<SubCategoryModel> list, ActivitySingleCategoyList1 aa1) {
        this.mArrayList = list;
        this.mContext = context;
        aa = aa1;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView ImgPopularTheme;
        LinearLayout ll_premium;
        ImageView video;
        ImageView poster;

        RecyclerViewHolder(View view) {
            super(view);
            this.ImgPopularTheme = (ImageView) view.findViewById(R.id.imageview);
            ll_premium = view.findViewById(R.id.ll_premium);
            video = view.findViewById(R.id.ic_video);
            poster = view.findViewById(R.id.ic_poster);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, final int i) {

        Log.e("SIZE2", this.mArrayList.size() + "");
        ((RequestBuilder) Glide.with(this.mContext).asBitmap().load(this.mArrayList.get(i).getSubcatImg()).placeholder((int) R.drawable.poster_placeholder)).into(recyclerViewHolder.ImgPopularTheme);

        if(mArrayList.get(i).getPaid().equalsIgnoreCase("paid")) {
            recyclerViewHolder.ll_premium.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHolder.ll_premium.setVisibility(View.GONE);
        }

   if(mArrayList.get(i).getFileType().equals("Poster")) {
            recyclerViewHolder.poster.setVisibility(View.VISIBLE);
            recyclerViewHolder.video.setVisibility(View.GONE);
        } else {
            recyclerViewHolder.video.setVisibility(View.VISIBLE);
            recyclerViewHolder.poster.setVisibility(View.GONE);
        }


        recyclerViewHolder.ImgPopularTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(mContext).showInterstitial(aa, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {
                        if (mArrayList.get(i).getPaid().equalsIgnoreCase("paid")) {
                            if (PreferenceUtils.isActivePlan(mContext)) {

                                if(mArrayList.get(i).getFileType().equals("Video")){
//                                    ActivitySingleVideoList.getInstance().setselectedvideo(mArrayList.get(i).getFileUrl());
                                    Intent intent = new Intent(mContext, ActivitySingleVideoList.class);
                                    intent.putExtra("video", mArrayList.get(i).getFileUrl());
                                    intent.putExtra("childitemtittle", mArrayList.get(i).getSubcatName());
                                    Log.e("555555", "returnAction: " + mArrayList.get(i).getFileUrl());
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                } else {

                                    ActivitySingleCategoyList1.getInstance().setselectedimage(mArrayList.get(i).getSubcatImg());
                                    ActivitySingleCategoyList1.getInstance().adapter.notifyDataSetChanged();
                                }
                            } else if (mEventListener != null) {
                                mEventListener.onItemViewClick(i);
                            }
                        }else {
                            if(mArrayList.get(i).getFileType().equals("Video")){
                                Intent intent = new Intent(mContext, ActivitySingleVideoList.class);
                                intent.putExtra("video", mArrayList.get(i).getFileUrl());
                                intent.putExtra("childitemtittle", mArrayList.get(i).getSubcatName());
                                Log.e("555555", "returnAction: " + mArrayList.get(i).getSubcatName());
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            } else {

                                ActivitySingleCategoyList1.getInstance().setselectedimage(mArrayList.get(i).getSubcatImg());
                                ActivitySingleCategoyList1.getInstance().adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


            }
        });

//        setAnimation(recyclerViewHolder.itemView, i);



    }

    public interface EventListener {

        void onItemViewClick(int position);

    }


    @Override
    public int getItemCount() {
        List<SubCategoryModel> list = this.mArrayList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void filterList(ArrayList<SubCategoryModel> filterList) {
        mArrayList = filterList;
        notifyDataSetChanged();
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
}
