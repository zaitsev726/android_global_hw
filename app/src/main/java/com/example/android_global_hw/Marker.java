package com.example.android_global_hw;

import android.graphics.Bitmap;

public class Marker {
    private Bitmap icon;
    private String link;
    private String header;
    private String description;
    public Marker(String link, String description, Bitmap bitmap){
        this.icon = bitmap;
        this.description = description;
        this.link = link;
    }

    public void setHeader(String header){this.header = header;}

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getHeader(){return header;}
}
