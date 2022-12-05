package com.dexxaphotolab.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dexxaphotolab.app.Activities.ActivityHome;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.dexxaphotolab.app.Utills.SharedPrefrenceConfig;

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {

    private Context context;
    LayoutInflater layoutInflater;
    String frameName[];
    SharedPrefrenceConfig sharedprefconfig;
    String type;

    public AdapterCategoryList(Context context, String frameName[], String type) {
        this.context = context;
        this.type = type;
        this.frameName = frameName;
        this.layoutInflater = LayoutInflater.from(context);
        sharedprefconfig = new SharedPrefrenceConfig(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.dropdwon_raw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.tv_dd_cat_name.setText(frameName[position]);
        holder.tv_dd_cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (frameName[position].equals("All")) {
                    sharedprefconfig.saveStringPreferance(context, Constance.language, "Gujarati,Hindi,English");

                } else {
                    sharedprefconfig.saveStringPreferance(context, Constance.language,frameName[position]);
                }

                if (type.equals("home")) {
                    ActivityHome.getInstance().popupWindowHelper.dismiss();
                }




            }
        });
    }

    @Override
    public int getItemCount() {
        return frameName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dd_cat_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_dd_cat_name = itemView.findViewById(R.id.tv_dd_cat_name);


        }
    }
}
