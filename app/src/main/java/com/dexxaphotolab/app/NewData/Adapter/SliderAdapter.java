package com.dexxaphotolab.app.NewData.Adapter;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dexxaphotolab.app.NewData.NewActivity.ActivitySingleCategoyList1;
import com.dexxaphotolab.app.payment.Utils.PreferenceUtils;
import com.dexxaphotolab.app.payment.activity.LoginActivity;
import com.dexxaphotolab.app.Model.home_content.Slide;
import com.github.islamkhsh.CardSliderAdapter;
import com.dexxaphotolab.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SliderAdapter extends CardSliderAdapter<Slide> {

        ArrayList<Slide> slider = new ArrayList<>();
    public SliderAdapter(@NotNull ArrayList<Slide> items) {
        super(items);
slider = items;
    }

    @Override
    public void bindView(int i, @NotNull View view, @Nullable final Slide slide) {
        if (slide != null){
            TextView textView = view.findViewById(R.id.textView);

            textView.setText(slide.getTitle());
            RoundedImageView imageView = view.findViewById(R.id.imageview);
            Picasso.get().load(slide.getImageLink()).into(imageView);
            View lyt_parent = view.findViewById(R.id.lyt_parent);

                if(slide.getPublication().equals("0")){
                        lyt_parent.setVisibility(View.GONE);
                } else {
                        lyt_parent.setVisibility(View.VISIBLE);
                }

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slide.getActionType().equalsIgnoreCase("tvseries") || slide.getActionType().equalsIgnoreCase("movie")){

                            if (PreferenceUtils.isLoggedIn(view.getContext())){
                                    Intent intent = new Intent(view.getContext(), ActivitySingleCategoyList1.class);
                                    intent.putExtra("vType", slide.getActionType());
                                    intent.putExtra("id", slide.getActionId());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    view.getContext().startActivity(intent);
                            }else {
                                view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
                            }
//                        }else {
//                            Intent intent=new Intent(view.getContext(), ActivitySingleCategoyList1.class);
//                            intent.putExtra("vType",slide.getActionType());
//                            intent.putExtra("id",slide.getActionId());
//
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            view.getContext().startActivity(intent);
//                        }

                    } else if(slide.getActionType().equalsIgnoreCase("webview")) {
                            if (PreferenceUtils.isLoggedIn(view.getContext())){
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(slide.getActionUrl()));
                                    view.getContext().startActivity(intent);
                            }else {
                                    view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
                            }
                    }

                }
            });
        }
    }


    @Override
    public int getItemContentLayout(int i) {
        return R.layout.slider_item;
    }
}
