package net.topafrica.Annuaire.activity.createbusiness;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.topafrica.Annuaire.AppConstant;
import net.topafrica.Annuaire.AppController;
import net.topafrica.Annuaire.PermissionUtils;
import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.Utils;
import net.topafrica.Annuaire.ValidationResult;
import net.topafrica.Annuaire.ValidationUtils;
import net.topafrica.Annuaire.adapter.CategoryAdapter;
import net.topafrica.Annuaire.common.BaseDrawerActivity;
import net.topafrica.Annuaire.modal.category.Annotation;
import net.topafrica.Annuaire.modal.category.Businesse;
import net.topafrica.Annuaire.modal.category.Category;
import net.topafrica.Annuaire.modal.category.Day;
import net.topafrica.Annuaire.modal.category.Mapdata;
import net.topafrica.Annuaire.modal.category.Openhours;
import net.topafrica.Annuaire.rx.AddressToStringListFunc;
import net.topafrica.Annuaire.rx.DisplayAddressOnViewAction;
import net.topafrica.Annuaire.rx.ErrorHandler;
import net.topafrica.Annuaire.rx.FallbackReverseGeocodeObservable;
import net.topafrica.Annuaire.rx.firebase.RxFirebaseStorage;
import net.topafrica.Annuaire.uiView.SpineerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.schedulers.Schedulers;

public class CreateBusiness extends BaseDrawerActivity implements GoogleMap.OnMarkerDragListener,GoogleMap.OnCameraIdleListener{

    @Nullable
    @Bind(R.id.id_search_view)
    SearchView searchView;

    @Nullable
    @Bind(R.id.id_radio_group)
    RadioGroup radioGroup;

    @Nullable
    @Bind(R.id.id_card_business_name)
    EditText text_business_name;

    @Nullable
    @Bind(R.id.id_card_business_type)
    EditText text_business_type;

    @Nullable
    @Bind(R.id.id_number_of_employee)
    Spinner spinner;

    @Nullable
    @Bind(R.id.id_card_business_email)
    EditText text_business_email;

    @Nullable
    @Bind(R.id.id_card_business_sec_email)
    EditText text_business_sec_email;

    @Nullable
    @Bind(R.id.id_card_business_telephone)
    EditText text_business_phone;

    @Nullable
    @Bind(R.id.id_card_business_fax)
    EditText text_business_fax;

    @Nullable
    @Bind(R.id.id_country_code)
    TextView text_country_code;

    @Nullable
    @Bind(R.id.id_fax_code)
    TextView text_fax_code;

    @Nullable
    @Bind(R.id.id_next_btn_step_1)
    AppCompatButton next_btn_step_1;

    @Nullable
    @Bind(R.id.id_include_1)
    View steps_one_view;

    @Nullable
    @Bind(R.id.id_include_2)
    View steps_two_view;

    @Nullable
    @Bind(R.id.id_include_3)
    View steps_three_view;

    @Nullable
    @Bind(R.id.id_include_4)
    View steps_four_view;

    @Nullable
    @Bind(R.id.id_recyleview)
    RecyclerView recyclerView;

    @Nullable
    @Bind(R.id.id_country)
    TextView country;

    @Nullable
    @Bind(R.id.id_city)
    TextView city;

    @Nullable
    @Bind(R.id.id_address)
    TextView address;

    @Nullable
    @Bind(R.id.id_lat)
    TextView cordinate;

    @Nullable
    @Bind(R.id.id_mor_mon_opn_time)
    TextView textView_mor_mon_opn_time;

    @Nullable
    @Bind(R.id.id_next_btn_steps_three)
    AppCompatButton next_steps_btn_three;

    @OnClick(R.id.id_mor_mon_opn_time)
    public void mondayMorningOpenTime(View v){
        Log.e("TIME1","" + "sadfd");
        openTimePicker(textView_mor_mon_opn_time);
    }

    @Nullable
    @Bind(R.id.id_mor_mon_close_time)
    TextView textView_mor_mon_close_time;
    @OnClick(R.id.id_mor_mon_close_time)
    public void mondayMorningCloseTime(View v){
        Log.e("TIME2","" + "sadfd");
        openTimePicker(textView_mor_mon_close_time);
    }

    @Nullable
    @Bind(R.id.id_mor_tue_opn_time)
    TextView textView_mor_tue_opn_time;
    @OnClick(R.id.id_mor_tue_opn_time)
    public void TuesMorningOpenTime(View v){
        Log.e("TIME3","" + "sadfd");
        openTimePicker(textView_mor_tue_opn_time);
    }

    @Nullable
    @Bind(R.id.id_mor_tue_close_time)
    TextView textView_mor_tue_close_time;
    @OnClick(R.id.id_mor_tue_close_time)
    public void TuesMorningCloseTime(View v){
        Log.e("TIME4","" + "sadfd");
        openTimePicker(textView_mor_tue_close_time);
    }

    @Nullable
    @Bind(R.id.id_mor_wed_opn_time)
    TextView textView_mor_wed_opn_time;
    @OnClick(R.id.id_mor_wed_opn_time)
    public void WedMorningOpenTime(View v){
        Log.e("TIME5","" + "sadfd");
        openTimePicker(textView_mor_wed_opn_time);
    }

    @Nullable
    @Bind(R.id.id_mor_wed_close_time)
    TextView textView_mor_wed_close_time;
    @OnClick(R.id.id_mor_wed_close_time)
    public void WedMorningCloseTime(View v){
        Log.e("TIME6","" + "sadfd");
        openTimePicker(textView_mor_wed_close_time);
    }

    @Nullable
    @Bind(R.id.id_mor_thrus_opn_time)
    TextView textView_mor_thrus_opn_time;
    @OnClick(R.id.id_mor_thrus_opn_time)
    public void thrusMorningOpenTime(View v){
        Log.e("TIME7","" + "sadfd");
        openTimePicker(textView_mor_thrus_opn_time);
    }

    @Nullable
    @Bind(R.id.id_mor_thrus_close_time)
    TextView textView_mor_thrus_close_time;
    @OnClick(R.id.id_mor_thrus_close_time)
    public void thrusMorningCloseTime(View v){
        Log.e("TIME8","" + "sadfd");
        openTimePicker(textView_mor_thrus_close_time);
    }

    @Nullable
    @Bind(R.id.id_mor_friday_opn_time)
    TextView textView_mor_friday_opn_time;
    @OnClick(R.id.id_mor_friday_opn_time)
    public void fridayMorningOpenTime(View v){
        Log.e("TIME9","" + "sadfd");
        openTimePicker(textView_mor_friday_opn_time);
    }
    @Nullable
    @Bind(R.id.id_mor_friday_close_time)
    TextView textView_mor_friday_close_time;
    @OnClick(R.id.id_mor_friday_close_time)
    public void fridayMorningCloseTime(View v){
        Log.e("TIME10","" + "sadfd");
        openTimePicker(textView_mor_friday_close_time);
    }
    @Nullable
    @Bind(R.id.id_mor_sat_opn_time)
    TextView textView_mor_sat_opn_time;
    @OnClick(R.id.id_mor_sat_opn_time)
    public void satMorningOpenTime(View v){
        Log.e("TIME11","" + "sadfd");
        openTimePicker(textView_mor_sat_opn_time);
    }

    @Nullable
    @Bind(R.id.id_mor_sat_close_time)
    TextView textView_mor_sat_close_time;
    @OnClick(R.id.id_mor_sat_close_time)
    public void satMorningCloseTime(View v){
        Log.e("TIME12","" + "sadfd");
        openTimePicker(textView_mor_sat_close_time);
    }
    @Nullable
    @Bind(R.id.id_mor_sun_opn_time)
    TextView textView_mor_sun_opn_time;
    @OnClick(R.id.id_mor_sun_opn_time)
    public void sundayMorningOpenTime(View v){
        Log.e("TIME13","" + "sadfd");
        openTimePicker(textView_mor_sun_opn_time);
    }
    @Nullable
    @Bind(R.id.id_mor_sun_close_time)
    TextView textView_mor_sun_close_time;
    @OnClick(R.id.id_mor_sun_close_time)
    public void sundayMorningCloseTime(View v){
        Log.e("TIME14","" + "sadfd");
        openTimePicker(textView_mor_sun_close_time);
    }

    @Nullable
    @Bind(R.id.id_even_mon_opn_time)
    TextView textView_even_mon_opn_time;
    @OnClick(R.id.id_even_mon_opn_time)
    public void mondayEveningOpenTime(View v){
        Log.e("TIME15","" + "sadfd");
        openTimePicker(textView_even_mon_opn_time);
    }

    @Nullable
    @Bind(R.id.id_even_mon_close_time)
    TextView textView_even_mon_close_time;
    @OnClick(R.id.id_even_mon_close_time)
    public void mondayEveningCloseTime(View v){
        Log.e("TIME16","" + "sadfd");
        openTimePicker(textView_even_mon_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_tue_opn_time)
    TextView textView_even_tue_opn_time;
    @OnClick(R.id.id_even_tue_opn_time)
    public void tuesEveningOpenTime(View v){
        Log.e("TIME17","" + "sadfd");
        openTimePicker(textView_even_tue_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_tue_close_time)
    TextView textView_even_tue_close_time;
    @OnClick(R.id.id_even_tue_close_time)
    public void tuesEveningCloseTime(View v){
        Log.e("TIME18","" + "sadfd");
        openTimePicker(textView_even_tue_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_wed_opn_time)
    TextView textView_even_wed_opn_time;
    @OnClick(R.id.id_even_wed_opn_time)
    public void wedEveningOpenTime(View v){
        Log.e("TIME19","" + "sadfd");
        openTimePicker(textView_even_wed_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_wed_close_time)
    TextView textView_even_wed_close_time;
    @OnClick(R.id.id_even_wed_close_time)
    public void wedEveningCloseTime(View v){
        Log.e("TIME20","" + "sadfd");
        openTimePicker(textView_even_wed_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_thrus_opn_time)
    TextView textView_even_thrus_opn_time;
    @OnClick(R.id.id_even_thrus_opn_time)
    public void thruEveningOpenTime(View v){
        Log.e("TIME21","" + "sadfd");
        openTimePicker(textView_even_thrus_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_thrus_close_time)
    TextView textView_even_thrus_close_time;
    @OnClick(R.id.id_even_thrus_close_time)
    public void thruEveningCloseTime(View v){
        Log.e("TIME22","" + "sadfd");
        openTimePicker(textView_even_thrus_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_friday_opn_time)
    TextView textView_even_friday_opn_time;
    @OnClick(R.id.id_even_friday_opn_time)
    public void fridayEveningOpenTime(View v){
        Log.e("TIME23","" + "sadfd");
        openTimePicker(textView_even_friday_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_friday_close_time)
    TextView textView_even_friday_close_time;
    @OnClick(R.id.id_even_friday_close_time)
    public void fridayEveningCloseTime(View v){
        Log.e("TIME24","" + "sadfd");
        openTimePicker(textView_even_friday_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_sat_opn_time)
    TextView textView_even_sat_opn_time;
    @OnClick(R.id.id_even_sat_opn_time)
    public void saturdayEveningOpenTime(View v){
        Log.e("TIME25","" + "sadfd");
        openTimePicker(textView_even_sat_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_sat_close_time)
    TextView textView_even_sat_close_time;
    @OnClick(R.id.id_even_sat_close_time)
    public void saturdayEveningCloseTime(View v){
        Log.e("TIME26","" + "sadfd");
        openTimePicker(textView_even_sat_close_time);
    }
    @Nullable
    @Bind(R.id.id_even_sun_opn_time)
    TextView textView_even_sun_opn_time;
    @OnClick(R.id.id_even_sun_opn_time)
    public void sundayEveningOpenTime(View v){
        Log.e("TIME27","" + "sadfd");
        openTimePicker(textView_even_sun_opn_time);
    }
    @Nullable
    @Bind(R.id.id_even_sun_close_time)
    TextView textView_even_sun_close_time;
    @OnClick(R.id.id_even_sun_close_time)
    public void sundayEveningCloseTime(View v){
        Log.e("TIME28","" + "sadfd");
        openTimePicker(textView_even_sun_close_time);
    }

    @Nullable
    @Bind(R.id.id_image_bus_logo_gallery)
    ImageView logo_camera_image;

    @OnClick(R.id.id_image_bus_logo_gallery)
    public void chooseLogoFromCamera(View v){
        i=1;
        Log.e("1===","" + "" + i);
        selectImage();
    }

    @Nullable
    @Bind(R.id.id_image_bus_pic_camere)
    ImageView pic_camera_image;

    @OnClick(R.id.id_image_bus_pic_camere)
    public void choosePicFromCamera(View v){
        i=2;
        Log.e("2===","" + "" + i);
        selectImage();
    }

    @Nullable
    @Bind(R.id.id_include_morning)
    View view_morning;

    @Nullable
    @Bind(R.id.id_include_evening)
    View view_evening;

    @Nullable
    @Bind(R.id.id_morning_schdule)
    TextView morning_sch;

    @Nullable
    @Bind(R.id.id_evening_schdule)
    TextView evening_sch;

    @Nullable
    @Bind(R.id.login_progress)
    View mProgressView;

    @Nullable
    @Bind(R.id.login_form)
    View login_form;

    @OnClick(R.id.id_morning_schdule)
    public void morningSchduleClick(View v){
        morning_sch.setBackgroundResource(R.color.colorAccent);
        evening_sch.setBackgroundResource(R.color.white);
        morning_sch.setTextColor(getResources().getColor(R.color.white));
        evening_sch.setTextColor(getResources().getColor(R.color.gray_500));
        view_morning.setVisibility(View.VISIBLE);
        view_evening.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.id_evening_schdule)
    public void eveningSchduleClick(View v){
        morning_sch.setBackgroundResource(R.color.white);
        evening_sch.setBackgroundResource(R.color.colorAccent);
        morning_sch.setTextColor(getResources().getColor(R.color.gray_500));
        evening_sch.setTextColor(getResources().getColor(R.color.white));
        view_morning.setVisibility(View.INVISIBLE);
        view_evening.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Bind(R.id.mapview)
    MapView mapView;

    private Subscription _subscription,_storage_subscription;
    static List<Category> LIST;
    CategoryAdapter categoryAdapter;

    private boolean mapsSupported = true;
    private GoogleMap mMap;
    private double lat_double, lng_double;

    public static final String TAG_CAMERA = "Camera";
    public static final String TAG_CHOOSE_FROM_LIBRARY = "Gallery";
    public static final String TAG_CANCEL = "Cancel";
    public static final String TAG_ADD_PHOTO = "Choose Photo From";
    public static final String TAG_SELECT_FILE = "Select File";
    static int i = 0;
    File file;
    Uri logo_uri = null;
    Uri pic_uri =  null;

    String logo_url = "";
    String picture_url = "";
    String category = "";
    String listing = "Enterprise";
    String number_of_employee = "1-5";

    Day _dayMonday = new Day();
    Day _dayTuesday = new Day();
    Day _dayWednesday = new Day();
    Day _dayThrusday = new Day();
    Day _dayFriday = new Day();
    Day _daySaturday = new Day();
    Day _daySunday = new Day();

    Day _dayMondayEvening = new Day();
    Day _dayTuesdayEvening = new Day();
    Day _dayWednesdayEvening = new Day();
    Day _dayThrusdayEvening = new Day();
    Day _dayFridayEvening = new Day();
    Day _daySaturdayEvening = new Day();
    Day _daySundayEvening = new Day();

    Annotation _annotation = new Annotation();

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public  void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                login_form.setVisibility(show ? View.GONE : View.VISIBLE);
                login_form.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        login_form.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                login_form.setVisibility(show ? View.VISIBLE : View.GONE);
                login_form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void selectImage()  {
        final CharSequence[] items = {TAG_CAMERA, TAG_CHOOSE_FROM_LIBRARY,
                TAG_CANCEL};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateBusiness.this,R.style.MyDialogTheme);
        builder.setTitle(TAG_ADD_PHOTO);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (items[item].equals(TAG_CAMERA)) {
                        captureImageFromCamera();
                    } else if (items[item].equals(TAG_CHOOSE_FROM_LIBRARY)) {
                        captureImageFromGallery();
                    } else if (items[item].equals(TAG_CANCEL)) {
                        dialog.dismiss();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void captureImageFromCamera() throws IOException {
        if(!PermissionUtils.hasPermissions(CreateBusiness.this, AppConstant.PERMISSIONS)){
            ActivityCompat.requestPermissions(CreateBusiness.this,AppConstant.PERMISSIONS, AppConstant.PERMISSION_ALL);
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's aon camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, AppConstant.REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void captureImageFromGallery() throws IOException {
        if(!PermissionUtils.hasPermissions(CreateBusiness.this, AppConstant.PERMISSIONS)){
            ActivityCompat.requestPermissions(CreateBusiness.this,AppConstant.PERMISSIONS, AppConstant.PERMISSION_ALL);
        }else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstant.PICK_IMAGE_REQUEST);
        }
    }

    private void openTimePicker(final TextView targetView){
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                String secondString = second < 10 ? "0" + second : "" + second;
                String time = hourString + "h" + minuteString + "m" + secondString + "s";
                targetView.setText("" + time);
                gettimeList(targetView,hourOfDay,minute,second);
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

        timePickerDialog.show(getFragmentManager(),"Timepickerdialog");
    }

    public void gettimeList(TextView textView,int hour,int min,int sec){
       switch (textView.getId()){
           case R.id.id_mor_mon_opn_time:
               openTime(_dayMonday,"Monday",hour,min,sec);
               break;
           case R.id.id_mor_mon_close_time:
               closeTime(_dayMonday,"Monday",hour,min,sec);
               break;
           case R.id.id_mor_tue_opn_time:
               openTime(_dayTuesday,"Tuesday",hour,min,sec);
               break;
           case R.id.id_mor_tue_close_time:
               closeTime(_dayTuesday,"Tuesday",hour,min,sec);
               break;
           case R.id.id_mor_wed_opn_time:
               openTime(_dayWednesday,"Wednesday",hour,min,sec);
               break;
           case R.id.id_mor_wed_close_time:
               closeTime(_dayWednesday,"Wednesday",hour,min,sec);
               break;
           case R.id.id_mor_thrus_opn_time:
               openTime(_dayThrusday,"Thursday",hour,min,sec);
               break;
           case R.id.id_mor_thrus_close_time:
               closeTime(_dayThrusday,"Thursday",hour,min,sec);
               break;
           case R.id.id_mor_friday_opn_time:
               openTime(_dayFriday,"Friday",hour,min,sec);
               break;
           case R.id.id_mor_friday_close_time:
               closeTime(_dayFriday,"Friday",hour,min,sec);
               break;
           case R.id.id_mor_sat_opn_time:
               openTime(_daySaturday,"Saturday",hour,min,sec);
               break;
           case R.id.id_mor_sat_close_time:
               closeTime(_daySaturday,"Saturday",hour,min,sec);
               break;
           case R.id.id_mor_sun_opn_time:
               openTime(_daySunday,"Sunday",hour,min,sec);
               break;
           case R.id.id_mor_sun_close_time:
               closeTime(_daySunday,"Sunday",hour,min,sec);
               break;
           case R.id.id_even_mon_opn_time:
               openTime(_dayMondayEvening,"Monday",hour,min,sec);
               break;
           case R.id.id_even_mon_close_time:
               closeTime(_dayMondayEvening,"Monday",hour,min,sec);
               break;
           case R.id.id_even_tue_opn_time:
               openTime(_dayTuesdayEvening,"Tuesday",hour,min,sec);
               break;
           case R.id.id_even_tue_close_time:
               closeTime(_dayTuesdayEvening,"Tuesday",hour,min,sec);
               break;
           case R.id.id_even_wed_opn_time:
               openTime(_dayWednesdayEvening,"Wednesday",hour,min,sec);
               break;
           case R.id.id_even_wed_close_time:
               closeTime(_dayWednesdayEvening,"Wednesday",hour,min,sec);
               break;
           case R.id.id_even_thrus_opn_time:
               openTime(_dayThrusdayEvening,"Thursday",hour,min,sec);
               break;
           case R.id.id_even_thrus_close_time:
               closeTime(_dayThrusdayEvening,"Thursday",hour,min,sec);
               break;
           case R.id.id_even_friday_opn_time:
               openTime(_dayFridayEvening,"Friday",hour,min,sec);
               break;
           case R.id.id_even_friday_close_time:
               closeTime(_dayFridayEvening,"Friday",hour,min,sec);
               break;
           case R.id.id_even_sat_opn_time:
               openTime(_daySaturdayEvening,"Saturday",hour,min,sec);
               break;
           case R.id.id_even_sat_close_time:
               closeTime(_daySaturdayEvening,"Saturday",hour,min,sec);
               break;
           case R.id.id_even_sun_opn_time:
               openTime(_daySundayEvening,"Sunday",hour,min,sec);
               break;
           case R.id.id_even_sun_close_time:
               closeTime(_daySundayEvening,"Sunday",hour,min,sec);
               break;
           default:
               Toast.makeText(CreateBusiness.this,"No Matching value find",Toast.LENGTH_SHORT).show();
               break;

       }
    }

    private Day openTime(Day _day,String day,int hr,int min, int second){
        _day.setDay(day);
        _day.setOpenAt(Utils.getTimeInMilli(hr,min,second));
        return _day;
    }

    private Day closeTime(Day _day,String day,int hr,int min, int second){
        _day.setDay(day);
        _day.setCloseAt(Utils.getTimeInMilli(hr,min,second));
        return _day;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        observeState();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        resumeService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView!=null)
            mapView.onDestroy();
        if (_subscription !=null)
        _subscription.unsubscribe();
        if (_storage_subscription != null)
        _storage_subscription.unsubscribe();
        if (observeSignInSubscription !=null)
         observeSignInSubscription.unsubscribe();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView!=null)
            mapView.onLowMemory();
    }

    @Override
    public void onConnected(Location location) {
        super.onConnected(location);
        if(location != null){
            lat_double = location.getLatitude();
            lng_double = location.getLongitude();
           initializeMap(location);
        }
    }

    private void displayLocationOnDragerEnd(double latitude,double longitude,float mZoom) {
        try {
            lat_double = latitude;
            lng_double = longitude;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), mZoom));
            getStringAdress(latitude,longitude);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getStringAdress(double lat, double lng){
        FallbackReverseGeocodeObservable.createObservable(Locale.getDefault(),lat,lng,1)
                .map(new Func1<List<Address>, Address>() {
                    @Override
                    public Address call(List<Address> addresses) {
                        return addresses != null && !addresses.isEmpty() ? addresses.get(0) : null;
                    }
                })
                .map(new AddressToStringListFunc(lat,lng))
                .subscribeOn(Schedulers.io())               // use I/O thread to query for addresses
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new DisplayAddressOnViewAction(country,city,text_country_code,text_fax_code,address,cordinate),new ErrorHandler(CreateBusiness.this));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices())
            buildGoogleApiClient();
        setContentView(R.layout.activity_create_business);
        try {
            MapsInitializer.initialize(CreateBusiness.this);
            if (mapView != null) {
                mapView.onCreate(savedInstanceState);
            }

        } catch (Exception e) {
            mapsSupported = false;
        }
        next_steps_btn_three.setEnabled(false);
        pic_camera_image.setTag(1);
        logo_camera_image.setTag(1);
        setUpSpinner();
        initInstance();
        init();
        setTimeAsZeroIfNoTimeIsSelectedForDay();
        queryFirebase();
        setupObservables1();
        setupObservables3();
        radioCheckd();
        setupSearchView();
    }

    private void initializeMap(final Location location) {
        if (mMap == null && mapsSupported) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    try {
                        mMap = googleMap;
                        mMap.setOnCameraIdleListener(CreateBusiness.this);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void setUpSpinner(){
       SpineerView spineerView = new SpineerView(CreateBusiness.this, spinner, new SpineerView.OnSpinnerSelected() {
           @Override
           public void OnItemSelected(String item) {
               number_of_employee = item;
           }
       });
        spineerView.openSpinner();
    }
    private void init(){
        //add ItemDecoration
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateBusiness.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initInstance(){
        LIST = new ArrayList<Category>();
        categoryAdapter = new CategoryAdapter(LIST,CreateBusiness.this);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) v.findViewById(R.id.id_check_box_category);
                if(checkBox.isChecked() && CategoryAdapter.COUNT <3){
                    CategoryAdapter.SELECTED_POSITION.set(position,position);
                    CategoryAdapter.LAST_POSTION = position;
                    CategoryAdapter.COUNT++;
                }else{
                    if (checkBox.isChecked()){
                        CategoryAdapter.SELECTED_POSITION.set(CategoryAdapter.LAST_POSTION,-1);
                        CategoryAdapter.SELECTED_POSITION.set(position,position);
                        CategoryAdapter.COUNT = 3;
                    }
                    else{
                        CategoryAdapter.SELECTED_POSITION.set(position,-1);
                        CategoryAdapter.COUNT--;
                    }

                    CategoryAdapter.LAST_POSTION = position;
                }

                category = LIST.get(CategoryAdapter.SELECTED_POSITION.get(position)).getCategoryname();
                categoryAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == AppConstant.REQUEST_TAKE_PHOTO) {
                try {
                    if(data!=null){
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        file =  Utils.saveImageToExternalStorage(photo);
                        if (i==1){
                            logo_camera_image.setTag(2);
                            setPic(file.getAbsolutePath(),logo_camera_image);
                            logo_uri = Uri.fromFile(file);
                        }
                        else{
                            pic_camera_image.setTag(2);
                            setPic(file.getAbsolutePath(),pic_camera_image);
                            pic_uri = Uri.fromFile(file);
                        }

                        galleryAddPic(file.getAbsolutePath());

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (requestCode == AppConstant.PICK_IMAGE_REQUEST) {
                // SDK >= 11 && SDK < 19
                String realPath = "";
//                 if (Build.VERSION.SDK_INT < 19)
//                    realPath = Utils.getRealPathFromURI_API11to18(this, data.getData());
//                else
//                    realPath = Utils.getRealPathFromURI_API19(this, data.getData());

//                Log.e("REAL PATH", "" + realPath);
                if (i==1){
                    logo_camera_image.setTag(2);
                    logo_uri = data.getData();
//                    logo_camera_image.setImageURI(logo_uri);
                    setPic(logo_uri,logo_camera_image);
                }
                else{
                    pic_camera_image.setTag(2);
                    pic_uri = data.getData();
//                    pic_camera_image.setImageURI(pic_uri);
                    setPic(pic_uri,pic_camera_image);
                }
            }
        }
    }

    private void setPic(String mCurrentPhotoPath ,ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
//
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void setPic(Uri uri ,ImageView imageView) {
        // Get the dimensions of the View
        try {
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,bmOptions);
            imageView.setImageBitmap(bitmap);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


    private void setupSearchView()
    {
        // search hint
        searchView.setQueryHint(getString(R.string.search_cat_hint));
        RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        if(charSequence!=null){
                            // Here you can get the text
                            categoryAdapter.filter(charSequence.toString());
                        }
                    }
                });
    }

    private void queryFirebase(){
        Firebase ref = new Firebase("https://top-africa-annuaire-1361.firebaseio.com/categories");

        Query mQuery = ref.orderByValue();
//
        mQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String cat = dataSnapshot.getValue().toString();
                Category category = new Category();
                category.setCategoryname(cat);
                category.setChecked(false);

//                List<Category> as = new ArrayList<Category>();
//                as.add(category);
                categoryAdapter.addItems(category);
                categoryAdapter.addItem(category);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void radioCheckd(){
        RxRadioGroup.checkedChanges(radioGroup).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                if (integer == R.id.id_radio_button_1){
                    listing = "Enterprise";
                }else if(integer == R.id.id_radio_button_2){
                    listing = "Professional";
                }else if(integer == R.id.id_radio_button_3){
                    listing = "Personnel";
                }
            }
        });
    }

    private void setTimeAsZeroIfNoTimeIsSelectedForDay(){
            openTime(_dayMonday,"Monday",0,0,0);
            closeTime(_dayMonday,"Monday",0,0,0);
            openTime(_dayTuesday,"Tuesday",0,0,0);
            closeTime(_dayTuesday,"Tuesday",0,0,0);
            openTime(_dayWednesday,"Wednesday",0,0,0);
            closeTime(_dayWednesday,"Wednesday",0,0,0);
            openTime(_dayThrusday,"Thrusday",0,0,0);
            closeTime(_dayThrusday,"Thrusday",0,0,0);
            openTime(_dayFriday,"Friday",0,0,0);
            closeTime(_dayFriday,"Friday",0,0,0);
            openTime(_daySaturday,"Saturday",0,0,0);
            closeTime(_daySaturday,"Saturday",0,0,0);
            openTime(_daySunday,"Saturday",0,0,0);
            closeTime(_daySunday,"Saturday",0,0,0);
            openTime(_dayMondayEvening,"Monday",0,0,0);
            closeTime(_dayMondayEvening,"Monday",0,0,0);
            openTime(_dayTuesdayEvening,"Tuesday",0,0,0);
            closeTime(_dayTuesdayEvening,"Tuesday",0,0,0);
            openTime(_dayWednesdayEvening,"Wednesday",0,0,0);
            closeTime(_dayWednesdayEvening,"Wednesday",0,0,0);
            openTime(_dayThrusdayEvening,"Thrusday",0,0,0);
            closeTime(_dayThrusdayEvening,"Thrusday",0,0,0);
            openTime(_dayFridayEvening,"Friday",0,0,0);
            closeTime(_dayFridayEvening,"Friday",0,0,0);
            openTime(_daySaturdayEvening,"Saturday",0,0,0);
            closeTime(_daySaturdayEvening,"Saturday",0,0,0);
            openTime(_daySundayEvening,"Sunday",0,0,0);
            closeTime(_daySundayEvening,"Sunday",0,0,0);
    }

    // Validate input data with debounce
    private void setupObservables1() {

        // Debounce is coming in very handy here.
        // What I had understood before is that if I use debounce, it will emit event after the give
        // time period regardless of other events.
        // But now I am realizing that this is not the case.
        // Let's say debounce interval is 200 milliseconds. Once an event is emitted, RxJava clock starts
        // ticking. Once 200 ms is up, debounce operator will emit that event.
        // One more event comes to debounce and it will start the clock for 200 ms. If another event comes
        // in 100 ms, debounce operator will reset the clock and start to count 200 ms again.
        // So let's say if you continue emitting events at 199 ms intervals, this debounce operator
        // will never emit any event.

        // Also, debounce by default goes on Scheduler thread, so it is important to add observeOn
        // and observe it on main thread.

        Observable<Boolean> nameObservable = RxTextView.textChanges(text_business_name)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validateBusiness(s.toString());
                        text_business_name.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> categoryObservable = RxTextView.textChanges(text_business_type)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validateWebsite(s.toString());
                        text_business_type.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> emailObservable = RxTextView.textChanges(text_business_email)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        Log.i(TAG, "validate email: " + s);
                        ValidationResult result = validateEmail(s.toString());
                        text_business_email.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> sec_emailObservable = RxTextView.textChanges(text_business_sec_email)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        Log.i(TAG, "validate email: " + s);
                        ValidationResult result = validateEmail(s.toString());
                        text_business_sec_email.setError(result.getReason());
                        return result.isValid();
                    }
                });

        Observable<Boolean> phoneObservable = RxTextView.textChanges(text_business_phone)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validatePhone(s.toString());
                        text_business_phone.setError(result.getReason());
                        return result.isValid();
                    }
                });
//
        Observable<Boolean> faxObservable = RxTextView.textChanges(text_business_fax)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validatePhone(s.toString());
                        text_business_fax.setError(result.getReason());
                        return result.isValid();
                    }
                });


       _subscription = Observable.combineLatest(nameObservable, categoryObservable, emailObservable, sec_emailObservable, new Func4<Boolean, Boolean, Boolean, Boolean, Boolean>() {
           @Override
           public Boolean call(Boolean validName, Boolean validType, Boolean validEmail, Boolean validSecEmail) {
//               Log.i(TAG, "email: " + validEmail + ", username: " + validUsername + ", phone: " + validPhone);
               return validName && validType && validEmail && validSecEmail;
           }
       }).subscribe(new Action1<Boolean>() {
           @Override
           public void call(Boolean aBoolean) {
                 next_btn_step_1.setEnabled(aBoolean);
           }
       });
    }

    // Validate input data with debounce
    private void setupObservables3() {

        // Debounce is coming in very handy here.
        // What I had understood before is that if I use debounce, it will emit event after the give
        // time period regardless of other events.
        // But now I am realizing that this is not the case.
        // Let's say debounce interval is 200 milliseconds. Once an event is emitted, RxJava clock starts
        // ticking. Once 200 ms is up, debounce operator will emit that event.
        // One more event comes to debounce and it will start the clock for 200 ms. If another event comes
        // in 100 ms, debounce operator will reset the clock and start to count 200 ms again.
        // So let's say if you continue emitting events at 199 ms intervals, this debounce operator
        // will never emit any event.

        // Also, debounce by default goes on Scheduler thread, so it is important to add observeOn
        // and observe it on main thread.

        Observable<Boolean> phoneObservable = RxTextView.textChanges(text_business_phone)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validatePhone(s.toString());
                        text_business_phone.setError(result.getReason());
                        return result.isValid();
                    }
                });
//
        Observable<Boolean> faxObservable = RxTextView.textChanges(text_business_fax)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence s) {
                        ValidationResult result = validatePhone(s.toString());
                        text_business_fax.setError(result.getReason());
                        return result.isValid();
                    }
                });


        _subscription = Observable.combineLatest(phoneObservable, faxObservable, new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean validPhone, Boolean validFax) {
                return validPhone && validFax;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                next_steps_btn_three.setEnabled(aBoolean);
            }
        });
    }


    private ValidationResult<String> validateEmail(@NonNull String email) {
        return ValidationUtils.isValidEmailAddress(email);
    }

    private ValidationResult<String> validateUsername(@NonNull String username) {
        return ValidationUtils.isValidUsername(username);
    }

    private ValidationResult<String> validateBusiness(@NonNull String username) {
        return ValidationUtils.isValidBusinessName(username);
    }
    private ValidationResult<String> validateWebsite(@NonNull String username) {
        return ValidationUtils.isValidWebsite(username);
    }

    private ValidationResult validatePhone(@NonNull String phone) {
        if (phone.isEmpty()) {
            return ValidationResult.failure(null, phone);
        }

        boolean isValid = ValidationUtils.isValidMobileNumber(phone);
        if (isValid) {
            return ValidationResult.success(phone);
        }

        return ValidationResult.failure("Phone should be exactly 10 numbers", phone);
    }

    private void uploadFiles(){
        final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(AppConstant.FIREBASE_STORAGE_REFRENCE_URL);

        _storage_subscription = RxFirebaseStorage.putFile(storageRef.child("images/"+logo_uri.getLastPathSegment()),logo_uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<UploadTask.TaskSnapshot, UploadTask.TaskSnapshot>() {
                    @Override
                    public UploadTask.TaskSnapshot call(UploadTask.TaskSnapshot taskSnapshot) {
                        logo_url = taskSnapshot.getDownloadUrl().toString();
                        System.out.println("LOGO URL is " + taskSnapshot.getDownloadUrl());
                        return taskSnapshot;
                    }
                })
                .flatMap(new Func1<UploadTask.TaskSnapshot, Observable<UploadTask.TaskSnapshot>>() {
                    @Override
                    public Observable<UploadTask.TaskSnapshot > call(UploadTask.TaskSnapshot taskSnapshot) {
                        return RxFirebaseStorage.putFile(storageRef.child("images/"+pic_uri.getLastPathSegment()),pic_uri);
                    }
                })
                .map(new Func1<UploadTask.TaskSnapshot, UploadTask.TaskSnapshot>() {
                    @Override
                    public UploadTask.TaskSnapshot call(UploadTask.TaskSnapshot taskSnapshot) {
                        picture_url = taskSnapshot.getDownloadUrl().toString();
                        System.out.println("PIC URL is " + taskSnapshot.getDownloadUrl());
                        return taskSnapshot;
                    }
                })
                .subscribe(new Subscriber<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        saveDataToFirebaseDatabase();
                    }
                });
    }

    private void saveDataToFirebaseDatabase(){
        try {
            Mapdata mapdata = new Mapdata();
            List<Day> TIMELIST = new ArrayList<>();
            ArrayList<Annotation> annonationList = new ArrayList<>();
            ArrayList<String> pictures = new ArrayList<>();
            pictures.add(picture_url);
            _annotation.setLatitude(lat_double);
            _annotation.setLongitude(lng_double);
            _annotation.setTitle("Title" + 1);
            TIMELIST.add(_dayMonday);
            TIMELIST.add(_dayTuesday);
            TIMELIST.add(_dayWednesday);
            TIMELIST.add(_dayThrusday);
            TIMELIST.add(_dayFriday);
            TIMELIST.add(_daySaturday);
            TIMELIST.add(_daySunday);
            TIMELIST.add(_dayMondayEvening);
            TIMELIST.add(_dayTuesdayEvening);
            TIMELIST.add(_dayWednesdayEvening);
            TIMELIST.add(_dayThrusdayEvening);
            TIMELIST.add(_dayFridayEvening);
            TIMELIST.add(_daySaturdayEvening);
            TIMELIST.add(_daySundayEvening);
            Openhours openhours = new Openhours();
            openhours.setDays(TIMELIST);
            openhours.setZone(1);
            Firebase ref = new Firebase(AppConstant.FIREBASE_DATABSE_REFRENCE_URL+"businesses");
            Businesse businesse = new Businesse();
            businesse.setNumberEmployes("50");
            businesse.setCategory(category);
            businesse.setCity(city.getText().toString());
            businesse.setCountry(country.getText().toString());
            businesse.setEmail(text_business_email.getText().toString());
            businesse.setListingtype(listing);
            businesse.setLogo(logo_url);
            annonationList.add(_annotation);
            mapdata.setAnnotations(annonationList);
            businesse.setName(text_business_name.getText().toString());
            businesse.setOfficeLocation(cordinate.getText().toString());
            businesse.setOpenhours(openhours);
            businesse.setPhoneNumber(text_business_phone.getText().toString());
            businesse.setPictures(pictures);
            businesse.setRoad(address.getText().toString());
            businesse.setState(city.getText().toString());
            businesse.setSuburb(city.getText().toString());
            businesse.setWebsite(text_business_type.getText().toString());
            ref.push().setValue(businesse, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    showProgress(false);
                    AppController.getInstance().okAlert(CreateBusiness.this,"","Upload Successful");
                    if (firebaseError != null) {
                        System.out.println("Data could not be saved. " + firebaseError.getMessage());
                    } else {
                        System.out.println("Data saved successfully.");
                    }
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void enableFirstState(){
        steps_one_view.setVisibility(View.VISIBLE);
        steps_two_view.setVisibility(View.GONE);
        steps_three_view.setVisibility(View.GONE);
        steps_four_view.setVisibility(View.GONE);
    }
    private void enableSecondstate(){
        steps_one_view.setVisibility(View.GONE);
        steps_two_view.setVisibility(View.VISIBLE);
        steps_three_view.setVisibility(View.GONE);
        steps_four_view.setVisibility(View.GONE);
    }
    private void enableThirdState(){
        steps_one_view.setVisibility(View.GONE);
        steps_two_view.setVisibility(View.GONE);
        steps_three_view.setVisibility(View.VISIBLE);
        steps_four_view.setVisibility(View.GONE);
    }
    private void enableFourthState(){
        steps_one_view.setVisibility(View.GONE);
        steps_two_view.setVisibility(View.GONE);
        steps_three_view.setVisibility(View.GONE);
        steps_four_view.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.id_next_btn_step_1)
    public void stepFirstNextButtonClick(View v){
        enableSecondstate();
    }
    @OnClick(R.id.id_prev_btn_steps_two)
    public void stepSecondPrevButtonClick(View v){
        enableFirstState();
    }

    @OnClick(R.id.id_next_btn_steps_two)
    public void stepSecondNextButtonClick(View v){
        enableThirdState();
    }

    @OnClick(R.id.id_prev_btn_steps_three)
    public void stepThirdPrevButtonClick(View v){
        enableSecondstate();
    }

    @OnClick(R.id.id_next_btn_steps_three)
    public void stepThirdNextButtonClick(View v){
        enableFourthState();
    }

    @OnClick(R.id.id_prev_btn_steps_four)
    public void stepFourthPrevButtonClick(View v){
        enableThirdState();
    }


    @OnClick(R.id.id_next_btn_steps_four)
    public void saveToStorage(View v){
        if (pic_camera_image.getTag().equals(2) && logo_camera_image.getTag().equals(2)){
            showProgress(true);
            uploadFiles();
        }else{
            Toast.makeText(CreateBusiness.this,"Please choose business Logo & image",Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        assert marker != null;
        displayLocationOnDragerEnd(marker.getPosition().latitude,marker.getPosition().longitude,15.0f);
    }

    @Override
    public void onCameraIdle() {
        // Cleaning all the markers.
        if (mMap !=null){
            LatLng mPosition = mMap.getCameraPosition().target;
            float mZoom = mMap.getCameraPosition().zoom;
            displayLocationOnDragerEnd(mPosition.latitude,mPosition.longitude,mZoom);
        }
    }
}
