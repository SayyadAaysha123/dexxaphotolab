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

public class DVideoListFragment extends Fragment {

    View view;
    RecyclerView rv_mypostlist;
    AdapterMyPostList adapterMyPostList;
    public static RelativeLayout rl_imagenotfound;
    private ArrayList<File> fileArrayList;
    Context context;
    ProgressBar pb_post_video;
    Tracker mTracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_d_video_list, container, false);
        context = getContext();
        bindView();
        pb_post_video.setVisibility(View.VISIBLE);
        rv_mypostlist.setHasFixedSize(true);
        rv_mypostlist.setLayoutManager(new GridLayoutManager(context, 2));
        return view;
    }

    public void bindView() {
        rv_mypostlist = view.findViewById(R.id.rv_mypostlist);
        rl_imagenotfound = view.findViewById(R.id.rl_imagenotfound);
        pb_post_video = view.findViewById(R.id.pb_post_video);


    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();

        if (!Constance.FileSaveVideoDirectory.exists()) {
            Constance.FileSaveVideoDirectory.mkdir();
            Log.d("check_file", "FileSaveDirectory :" + Constance.FileSaveVideoDirectory);
            pb_post_video.setVisibility(View.GONE);
        } else {
            Log.d("jjjjj", "Constance.FileDirectory : " + Constance.FileSaveVideoDirectory);
            File[] files = Constance.FileSaveVideoDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    Log.d("check_file", "file 2 :" + file);

                    if (file.getName().endsWith(".mp4")&& !file.getName().equals("VideoDemo.mp4")) {
                        Log.d("check_file", "Video:");

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
                adapterMyPostList = new AdapterMyPostList(context, fileArrayList,"video");

                adapterMyPostList.notifyDataSetChanged();
                rv_mypostlist.setAdapter(adapterMyPostList);
                pb_post_video.setVisibility(View.GONE);
            } else {
                pb_post_video.setVisibility(View.GONE);
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