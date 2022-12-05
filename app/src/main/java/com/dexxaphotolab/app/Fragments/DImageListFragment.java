package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dexxaphotolab.app.Adapters.AdapterMyPostList;
import com.dexxaphotolab.app.R;
import com.dexxaphotolab.app.Utills.Constance;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.ArrayList;

public class DImageListFragment extends Fragment {
    View view;
    RecyclerView rv_mypostlist;
    AdapterMyPostList adapterMyPostList;
    public static RelativeLayout rl_imagenotfound;
    private ArrayList<File> fileArrayList;
    Context context;
    ProgressBar pb_post_image;
    Tracker mTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_d_image_list, container, false);

        context = getContext();
        bindView();

        pb_post_image.setVisibility(View.VISIBLE);

        rv_mypostlist.setHasFixedSize(true);
        rv_mypostlist.setLayoutManager(new GridLayoutManager(context, 3));
        return view;
    }

    public void bindView() {
        rv_mypostlist = view.findViewById(R.id.rv_mypostlist);
        rl_imagenotfound = view.findViewById(R.id.rl_imagenotfound);
        pb_post_image = view.findViewById(R.id.pb_post_image);


    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();

        if (!Constance.FileSaveDirectory.exists()) {
            pb_post_image.setVisibility(View.GONE);
            Constance.FileSaveDirectory.mkdir();
            Log.d("check_file", "FileSaveDirectory :" + Constance.FileSaveDirectory);

        } else {
            Log.d("jjjjj", "Constance.FileDirectory : " + Constance.FileSaveDirectory);
            File[] files = Constance.FileSaveDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    Log.d("check_file", "file 2 :" + file);

                    if (file.getName().endsWith(".png")) {
                        Log.d("check_file", "Png:");

                        fileArrayList.add(file);
                        Log.d("check_file", "fileArrayList Png:" + fileArrayList.size());

                    } else {
                        Log.d("check_file", "Not Png:");

                    }
                }

                for (int i = 0; i < fileArrayList.size(); i++) {
                    if (fileArrayList.get(i).isDirectory()) {
                        fileArrayList.remove(i);
                    }
                }
                adapterMyPostList = new AdapterMyPostList(context, fileArrayList, "image");
                adapterMyPostList.notifyDataSetChanged();
                rv_mypostlist.setAdapter(adapterMyPostList);
                pb_post_image.setVisibility(View.GONE);
            } else {
                pb_post_image.setVisibility(View.GONE);
                Toast.makeText(context, "Data Not Found", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFiles();
        if (fileArrayList.size() == 0) {
            rl_imagenotfound.setVisibility(View.VISIBLE);
        } else {
            rl_imagenotfound.setVisibility(View.GONE);
        }
    }

}