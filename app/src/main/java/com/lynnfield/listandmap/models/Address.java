package com.lynnfield.listandmap.models;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Address address = (Address) o;

        return Double.compare(address.latitude, latitude) == 0 &&
                Double.compare(address.longitude, longitude) == 0 &&
                (text != null ? text.equals(address.text) : address.text == null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = text != null ? text.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
