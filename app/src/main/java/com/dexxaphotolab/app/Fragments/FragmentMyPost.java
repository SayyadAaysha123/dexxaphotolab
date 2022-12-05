package com.dexxaphotolab.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dexxaphotolab.app.Adapters.PagerAdapter;
import com.dexxaphotolab.app.R;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyPost extends Fragment {

    Context context;
    View view;
    TabLayout tl_mypost;
    ViewPager vp_mypost;
    ProgressBar pb_post;
    Tracker mTracker;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragmen_mypost, container, false);
        context = getContext();
        bindView();
//        setupViewPager(vp_mypost);
        setUpTabLayout(savedInstanceState);
//        tl_mypost.setupWithViewPager(vp_mypost);

        return view;
    }

    public void bindView() {
        tl_mypost=view.findViewById(R.id.tl_mypost);
        vp_mypost=view.findViewById(R.id.vp_mypost);
        pb_post=view.findViewById(R.id.pb_post);


    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("tabsCount", this.pagerAdapter.getCount());
        bundle.putStringArray("titles", (String[]) this.pagerAdapter.getTitles().toArray(new String[0]));
    }

    private void setUpTabLayout(Bundle bundle) {
        this.viewPager = (ViewPager) this.view.findViewById(R.id.vp_mypost);
        this.pagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
        if (bundle == null) {
            this.pagerAdapter.addFragment(new DVideoListFragment(), "Videos");
            this.pagerAdapter.addFragment(new DImageListFragment(), "Images");

        } else {
            Integer valueOf = bundle.getInt("tabsCount");
            String[] stringArray = bundle.getStringArray("titles");
            for (int i = 0; i < valueOf.intValue(); i++) {
                this.pagerAdapter.addFragment(getFragment(i, bundle), stringArray[i]);
            }
        }
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setOffscreenPageLimit(3);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
            }
        });
        this.tl_mypost = (TabLayout) this.view.findViewById(R.id.tl_mypost);
        this.tl_mypost.setTabGravity(0);
        this.tl_mypost.setupWithViewPager(this.viewPager);
    }

    private Fragment getFragment(int i, Bundle bundle) {
        return bundle == null ? this.pagerAdapter.getItem(i) : getChildFragmentManager().findFragmentByTag(getFragmentTag(i));
    }

    private String getFragmentTag(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android:switcher:2131296670:");
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new DVideoListFragment(), "Videos");
        adapter.addFragment(new DImageListFragment(), "Images");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
//        setupViewPager(vp_mypost);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
