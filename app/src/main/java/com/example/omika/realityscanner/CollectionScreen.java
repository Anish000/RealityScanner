package com.example.omika.realityscanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionScreen extends Fragment {


    public CollectionScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Collection screen to show all the saved files.


        View rootview= inflater.inflate(R.layout.fragment_collection_screen, container, false);

    return rootview;

    }

}
