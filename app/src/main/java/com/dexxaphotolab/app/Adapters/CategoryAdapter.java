package com.dexxaphotolab.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.dexxaphotolab.app.Model.CategoryModel;
import com.dexxaphotolab.app.NewData.NewActivity.ActivitySingleCategoyList1;
import com.dexxaphotolab.app.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {
    private List<CategoryModel> mArrayList;
    private Context mContext;

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.mArrayList = list;
        this.mContext = context;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView ImgPopularTheme;

        RecyclerViewHolder(View view) {
            super(view);
            this.ImgPopularTheme = (ImageView) view.findViewById(R.id.imageview);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, final int i) {

        Log.e("SIZE2", this.mArrayList.size() + "");
        ((RequestBuilder) Glide.with(this.mContext).asBitmap().load(this.mArrayList.get(i).getCatImg()).placeholder((int) R.drawable.logo)).into(recyclerViewHolder.ImgPopularTheme);


        recyclerViewHolder.ImgPopularTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ActivitySingleCategoyList1.class);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        List<CategoryModel> list = this.mArrayList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
