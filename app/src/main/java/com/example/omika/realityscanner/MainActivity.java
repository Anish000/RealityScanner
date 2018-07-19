package com.example.omika.realityscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For now we will only show MainScreen. Once we implement the slide feature, we will include all the screens.


      //To load MainScreen.
     getSupportFragmentManager().beginTransaction().replace(R.id.Allscreenscontainer,new MainScreen(),"MainScreen").commit();
    }
}
