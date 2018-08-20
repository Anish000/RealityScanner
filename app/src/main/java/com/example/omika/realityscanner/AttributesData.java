package com.example.omika.realityscanner;

public class AttributesData {

    private int Image;
    private String Text;

    public AttributesData(int image, String text) {
        Image = image;
        Text = text;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
