package soluces.com.pennontautocars.com.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import soluces.com.pennontautocars.R;
import soluces.com.pennontautocars.com.adapter.TabPagerItem;
import soluces.com.pennontautocars.com.adapter.ViewPagerAdapter;


public class ViewPagerFragment extends Fragment {
	private List<TabPagerItem> mTabs = new ArrayList<>();


    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

    public static ViewPagerFragment newInstance(String text){
        ViewPagerFragment mFragment = new ViewPagerFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createTabPagerItem();
    }

    private void createTabPagerItem(){
       if(getArguments().getString(TEXT_FRAGMENT).equalsIgnoreCase("messages")){
            mTabs.add(new TabPagerItem(getString(R.string.discussion), DiscussionsFragment.newInstance()));
            mTabs.add(new TabPagerItem(getString(R.string.groupe), GroupFragment.newInstance()));
        }else{
        mTabs.add(new TabPagerItem(getString(R.string.encours), EncoursFragment.newInstance()));
        mTabs.add(new TabPagerItem(getString(R.string.depart), MissionAncienneFragment.newInstance()));
        mTabs.add(new TabPagerItem(getString(R.string.arrive), MissionFuturFragment.newInstance()));
       }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
    	
    	mViewPager.setOffscreenPageLimit(mTabs.size());
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));
        TabLayout mSlidingTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(15);
        }
        mSlidingTabLayout.setupWithViewPager(mViewPager);
    }
}