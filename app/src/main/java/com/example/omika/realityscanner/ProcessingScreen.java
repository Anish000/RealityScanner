package com.example.omika.realityscanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessingScreen extends Fragment {


    public ProcessingScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //processing screen to process the captured image.


        View rootview= inflater.inflate(R.layout.fragment_processing_screen, container, false);
        return rootview;
    }

}
