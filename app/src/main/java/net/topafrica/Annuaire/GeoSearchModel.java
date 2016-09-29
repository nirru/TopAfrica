package net.topafrica.Annuaire;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;


public class GeoSearchModel {

    public static String addressByLocation(LatLng location, Context context) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (geocoder.getFromLocation(location.latitude, location.longitude, 1) == null || geocoder.getFromLocation(location.latitude, location.longitude, 1).size() ==0)
                return address;
            Address geoAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1).get(0);
            address = geoAddress.getLocality() != null ? geoAddress.getLocality() : geoAddress.getAdminArea();
            address = address == null ? geoAddress.getCountryName() : address + ", " + geoAddress.getCountryName();
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return address;
        }
    }

    public static String fullAddressByLocation(LatLng location,Context context) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (geocoder.getFromLocation(location.latitude, location.longitude, 1) == null || geocoder.getFromLocation(location.latitude, location.longitude, 1).size() ==0)
                return address;
            Address geoAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1).get(0);
            for (int i = 0; i < geoAddress.getMaxAddressLineIndex(); i++) {
                address += geoAddress.getAddressLine(i) + " ";
            }
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return address;
        }
    }

    public static Address geoAddressByLocation(LatLng location,Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (geocoder.getFromLocation(location.latitude, location.longitude, 1) == null || geocoder.getFromLocation(location.latitude, location.longitude, 1).size() ==0)
                return null;
            return geocoder.getFromLocation(location.latitude, location.longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
