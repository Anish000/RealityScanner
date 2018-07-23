package com.example.omika.realityscanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


//This adapter allows us to swipe between the fragments.
public class screenswiper extends FragmentPagerAdapter {
    private static final int screens=3;
    public screenswiper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position==0){
            fragment=new ProcessingScreen();
          return fragment;
        }
        if (position==1){
            fragment=new MainScreen();
            return fragment;
        }
        if (position==2){
            fragment=new CollectionScreen();
            return fragment;
        }

       return null;
    }

    @Override
    public int getCount() {
        return screens;
    }
}
