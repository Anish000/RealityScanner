package com.example.omika.realityscanner;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    ViewPager viewPager;
    screenswiper swiper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







     //This will allow to swipe along the screens.
     swiper=new screenswiper(getSupportFragmentManager());

     viewPager =findViewById(R.id.allscreenscontainer);
     viewPager.setAdapter(swiper);

     //Assigning MainScreen as default screen(Screen to show when the app is opened).
     viewPager.setCurrentItem(1);

    }
}
