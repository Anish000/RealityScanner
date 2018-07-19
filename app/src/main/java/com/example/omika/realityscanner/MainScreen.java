package com.example.omika.realityscanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 */
public class MainScreen extends Fragment {


    public MainScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Camera interface to allow the image capturing.

        View rootview= inflater.inflate(R.layout.fragment_main_screen, container, false);

        return rootview;
    }

}
