package com.lynnfield.listandmap;

public class Address {
    private String text;
    private double latitude;
    private double longitude;

    public Address(final String text, final double latitude, final double longitude) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }
}
