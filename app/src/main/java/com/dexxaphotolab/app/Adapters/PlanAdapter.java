package com.dexxaphotolab.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.payment.Network.models.Package;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

        Context ctx;
        List<Package> plans;

        public PlanAdapter(Context context, List<Package> list) {
                ctx = context;
                plans = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plans, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                final Package list = plans.get(position);

                holder.name.setText(list.getName());
                holder.day.setText(list.getDay());
                holder.amount.setText(list.getPrice());

        }

        @Override
        public int getItemCount() {
                return plans.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
                TextView name;
                TextView day;
                TextView amount;
                CardView cardView;

                ViewHolder(View view) {
                        super(view);
                        name = view.findViewById(R.id.plan_name);
                        day = view.findViewById(R.id.days);
                        amount = view.findViewById(R.id.plan_amount);
                        cardView = view.findViewById(R.id.card_view);
                }
        }
}
