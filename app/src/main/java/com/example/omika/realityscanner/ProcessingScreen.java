package com.example.omika.realityscanner;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class ProcessingScreen extends Fragment {


    public ProcessingScreen() {
        // Required empty public constructor
    }

    Spinner textOptions,saveOptions,translateOptions;

    List<AttributesData>textOptionsList,saveOptionsList,translateOptionsList;

    AttributesAdapter textOptionsAdapter,saveOptionsAdapter,translateOptionsAdapter;

    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getActivity();

        //processing screen to process the captured image.
        View rootview= inflater.inflate(R.layout.fragment_processing_screen, container, false);

        textOptions=rootview.findViewById(R.id.textFormat);

        saveOptions=rootview.findViewById(R.id.saveFormat);

        translateOptions=rootview.findViewById(R.id.translateOptions);

        textOptionsList=new ArrayList<>();
        saveOptionsList=new ArrayList<>();
        translateOptionsList=new ArrayList<>();

        textOptionsList.add(new AttributesData(R.drawable.ic_format_bold_black_24dp,"Bold"));
        textOptionsList.add(new AttributesData(R.drawable.ic_format_italic_black_24dp,"Italic"));
        textOptionsList.add(new AttributesData(R.drawable.ic_more_horiz_black_24dp,"More"));

        saveOptionsList.add(new AttributesData(R.drawable.ic_text_format_black_24dp,".txt"));
        saveOptionsList.add(new AttributesData(R.drawable.ic_image_black_24dp,".png"));
        saveOptionsList.add(new AttributesData(R.drawable.ic_more_horiz_black_24dp,"More"));

        translateOptionsList.add(new AttributesData(R.drawable.ic_more_horiz_black_24dp,"Add"));

        textOptionsAdapter=new AttributesAdapter(context,textOptionsList);
        textOptions.setAdapter(textOptionsAdapter);

        saveOptionsAdapter=new AttributesAdapter(context,saveOptionsList);
        saveOptions.setAdapter(saveOptionsAdapter);

        translateOptionsAdapter=new AttributesAdapter(context,translateOptionsList);
        translateOptions.setAdapter(translateOptionsAdapter);









        return rootview;
    }

}
