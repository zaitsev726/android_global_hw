package com.example.android_global_hw;

import android.graphics.Bitmap;

import java.util.Objects;

public class Marker {
    private int markerID;
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

    public void setMarkerID(int id){markerID = id;}

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

    public int getMarkerID(){return markerID;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marker marker = (Marker) o;
        return Objects.equals(icon, marker.icon) &&
                Objects.equals(link, marker.link) &&
                Objects.equals(header, marker.header) &&
                Objects.equals(description, marker.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, link, header, description);
    }
}
