package com.example.android_global_hw;

import android.graphics.Bitmap;

public class Marker {
    private Bitmap icon;
    private String description;
    private String marker;

    public Marker(String marker, String description, Bitmap bitmap){
        this.icon = bitmap;
        this.description = description;
        this.marker = marker;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getMarker() {
        return marker;
    }
}
