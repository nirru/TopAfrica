package net.topafrica.Annuaire;

import android.*;

/**
 * Created by ericbasendra on 25/09/16.
 */

public class AppConstant {

    public static final String BASE_URL = "http://api.openweathermap.org/";
    public static final String WEATHER_URL = "data/2.5/weather";
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int PICK_IMAGE_REQUEST = 2;
    public static final int PERMISSION_ALL = 113;
    public static final String[] PERMISSIONS = { android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA};
    public static final String TWITTER_KEY = "V5y1zn3V3YLH9juA1QPhbVI7E";
    public static final String TWITTER_SECRET = "p34ldpGO6FvbF7QjpOzLmDdc8vzYoBZOpUYHdwtTxTVBQRtRea";
    public static final String FIREBASE_STORAGE_REFRENCE_URL = "gs://top-africa-annuaire-1361.appspot.com";
    public static final String FIREBASE_DATABSE_REFRENCE_URL = "https://top-africa-annuaire-1361.firebaseio.com/";
}
