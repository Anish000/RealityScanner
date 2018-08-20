package com.example.omika.realityscanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AttributesAdapter extends ArrayAdapter{


    private Context context;
    private List<AttributesData>attributesData;

    public AttributesAdapter(@NonNull Context context, List<AttributesData> attributesData) {
        super(context, 0, attributesData);
        this.context = context;
        this.attributesData = attributesData;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.attributesoptionsholder, parent, false
        );


        ImageView ImageHolder = convertView.findViewById(R.id.imageholder);
        TextView textHolder = convertView.findViewById(R.id.textholder);

        Glide.with(context).load(attributesData.get(position).getImage()).into(ImageHolder);

        textHolder.setText(attributesData.get(position).getText());


        return convertView;
    }


}
