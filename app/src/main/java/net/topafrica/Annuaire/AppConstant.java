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
}
