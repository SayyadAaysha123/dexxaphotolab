package com.dexxaphotolab.app.NewData.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dexxaphotolab.app.NewData.NewActivity.ItemPosterActivity;
import com.dexxaphotolab.app.NewData.model.CommonModels;
import com.dexxaphotolab.app.NewData.model.GenreModel;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Admanager;
import com.dexxaphotolab.app.Utills.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class GenreHomeAdapter extends RecyclerView.Adapter<GenreHomeAdapter.OriginalViewHolder> {

    private List<GenreModel> items = new ArrayList<>();
    private List<CommonModels> listData = new ArrayList<>();
    private Context ctx;
    EventListener mEventListener;
    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;
    private Activity aa;


    public GenreHomeAdapter(Context context, List<GenreModel> items, Activity aa1) {
        this.items = items;
        ctx = context;
        aa = aa1;
    }

    public interface EventListener {

        void onItemViewClick(int position);


    }


    @Override
    public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_genre_home, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OriginalViewHolder holder, final int position) {

        HomePageAdapter adapter;

        final GenreModel obj = items.get(position);
        Log.e("nnnnn", "onBindViewHolder: " + position + "//" + obj.getName());
        holder.name.setText(obj.getName());

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));

        if(obj.getName().equalsIgnoreCase("festival")){
            adapter = new HomePageAdapter(ctx, obj.getList(), aa, "festival");
        } else {
            adapter = new HomePageAdapter(ctx, obj.getList(), aa,"");
        }

        holder.recyclerView.setAdapter(adapter);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Admanager(ctx).showInterstitial(aa, new Admanager.GetBackPointer() {
                    @Override
                    public void returnAction() {

                        Intent intent = new Intent(ctx, ItemPosterActivity.class);
                        intent.putExtra("id", obj.getId());
                        Log.e("0000", "returnAction: " +  obj.getId() +"---- " + obj.getName());
                        intent.putExtra("title", obj.getName());
                        intent.putExtra("type", "genre");
                        ctx.startActivity(intent);
                    }
                });

            }
        });

//        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        RecyclerView recyclerView;
        Button btnMore;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            recyclerView = v.findViewById(R.id.recyclerView);
            btnMore = v.findViewById(R.id.btn_more);
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



}
