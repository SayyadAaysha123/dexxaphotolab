package com.dexxaphotolab.app.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dexxaphotolab.app.ModelRetrofit.CategoriesData;
import com.dexxaphotolab.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterViewAllList extends RecyclerView.Adapter<AdapterViewAllList.ViewHolder> {

    Context context;
    ArrayList<CategoriesData> modelHomeChildList;
    View view;

    public AdapterViewAllList(Context context, ArrayList<CategoriesData> modelHomeChildList) {
        this.context = context;
        this.modelHomeChildList = modelHomeChildList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rv_viewall, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String childitemtittle = modelHomeChildList.get(position).getName();
        holder.tv_viewall_tittle.setText(childitemtittle);
        Picasso.get().load(modelHomeChildList.get(position).getImage_url()).placeholder(R.drawable.placeholder).into(holder.riv_viewallimage);
        final String catnameid = modelHomeChildList.get(position).getId();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (modelHomeChildList.get(position).getDetail_display().equals("Yes")) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(modelHomeChildList.get(position).getDetail_message())
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelHomeChildList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView riv_viewallimage;
        TextView tv_viewall_tittle;
        LinearLayout ll_viewall_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_viewallimage = itemView.findViewById(R.id.riv_viewallimage);
            tv_viewall_tittle = itemView.findViewById(R.id.tv_viewall_tittle);

        }
    }


}
