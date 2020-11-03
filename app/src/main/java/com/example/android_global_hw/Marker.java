package com.example.android_global_hw;

import android.graphics.Bitmap;

public class Marker {
    private Bitmap icon;
    private String description;
    private String link;

    public Marker(String link, String description, Bitmap bitmap){
        this.icon = bitmap;
        this.description = description;
        this.link = link;
    }

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
}
