package com.dexxaphotolab.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dexxaphotolab.app.Activities.ActivityCreatePost;
import com.dexxaphotolab.app.Model.StickersModel;
import com.dexxaphotolab.app.R;

public class StickersListAdapter extends RecyclerView.Adapter<StickersListAdapter.ViewHolder> {

    private Context context;
    private StickersModel[] stickerlist;
    LayoutInflater layoutInflater;

    public StickersListAdapter(Context context, StickersModel[] stickers, String popup) {
        this.context = context;
        this.stickerlist = stickers;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.stickerlist_raw_item, parent, false);
        return new StickersListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(stickerlist[position].getImgId()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCreatePost.getInstance().setEmoji(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickerlist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_framlist_raw);

        }
    }
}
