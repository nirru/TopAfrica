package net.topafrica.Annuaire.activity.createbusiness;

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
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.topafrica.Annuaire.AppConstant;
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
import net.topafrica.Annuaire.rx.ReverseGeocodeObservable;
import net.topafrica.Annuaire.rx.firebase.RxFirebaseDatabase;
import net.topafrica.Annuaire.rx.firebase.RxFirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    @Bind(R.id.id_card_business_email)
    EditText text_business_email;

    @Nullable
    @Bind(R.id.id_card_business_telephone)
    EditText text_business_phone;

    @Nullable
    @Bind(R.id.id_card_business_fax)
    EditText text_business_fax;

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

    public static final String TAG_CAMERA = "Camera";
    public static final String TAG_CHOOSE_FROM_LIBRARY = "Gallery";
    public static final String TAG_CANCEL = "Cancel";
    public static final String TAG_ADD_PHOTO = "Choose Photo From";
    public static final String TAG_SELECT_FILE = "Select File";
    static int i = 0;
    File file;
    Uri logo_uri = null;
    Uri pic_uri =  null;

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
//                        mStartTimeView.setText(time);
                targetView.setText("" + time);
            }
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

        timePickerDialog.show(getFragmentManager(),"Timepickerdialog");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
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
        _subscription.unsubscribe();
        _storage_subscription.unsubscribe();
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
           initializeMap(location);
        }
    }

    private void displayLocationOnDragerEnd(double latitude,double longitude,float mZoom) {
        try {
//            mMap.addMarker(markerOptions.position(new LatLng(latitude, longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), mZoom));
            getStringAdress(latitude,longitude);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getStringAdress(double lat, double lng){
        Observable<List<Address>> reverseGeocodeObservable = ReverseGeocodeObservable.createObservable(CreateBusiness.this, Locale.getDefault(), lat, lng, 1);
        reverseGeocodeObservable
                .map(new Func1<List<Address>, Address>() {
                    @Override
                    public Address call(List<Address> addresses) {
                        return addresses != null && !addresses.isEmpty() ? addresses.get(0) : null;
                    }
                })
                .map(new AddressToStringListFunc(lat,lng))
                .subscribeOn(Schedulers.io())               // use I/O thread to query for addresses
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisplayAddressOnViewAction(country,city,address,cordinate),new ErrorHandler(CreateBusiness.this));
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
        initInstance();
        init();
        queryFirebase();
        setupObservables1();
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
                if(checkBox.isChecked())
                {
                    CategoryAdapter.SELECTED_POSITION =  position;
                }
                else{
                    CategoryAdapter.SELECTED_POSITION = -1;
                }

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
                            setPic(file.getAbsolutePath(),logo_camera_image);
                            logo_uri = Uri.fromFile(file);
                        }
                        else{
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
                 if (Build.VERSION.SDK_INT < 19)
                    realPath = Utils.getRealPathFromURI_API11to18(this, data.getData());
                else
                    realPath = Utils.getRealPathFromURI_API19(this, data.getData());

//                Log.e("REAL PATH", "" + realPath);
                if (i==1){
                    logo_uri = data.getData();
                    setPic(realPath,logo_camera_image);
                }
                else{
                    pic_uri = data.getData();
                    setPic(realPath,pic_camera_image);
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
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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
        Firebase ref = new Firebase("https://top-africa.firebaseio.com/categories");

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
                    Log.e("FIRST SELECTION", "" + "FIRST");
                }else if(integer == R.id.id_radio_button_2){
                    Log.e("SECOND SELECTION", "" + "SECOND");
                }else if(integer == R.id.id_radio_button_3){
                    Log.e("Third SELECTION", "" + "THIRD");
                }
            }
        });
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
                        ValidationResult result = validateUsername(s.toString());
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
                        ValidationResult result = validateUsername(s.toString());
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


       _subscription = Observable.combineLatest(nameObservable, categoryObservable, emailObservable, phoneObservable, faxObservable, new Func5<Boolean, Boolean, Boolean, Boolean, Boolean, Boolean>() {
           @Override
           public Boolean call(Boolean validName, Boolean validType, Boolean validEmail, Boolean validPhone, Boolean validFax) {
//               Log.i(TAG, "email: " + validEmail + ", username: " + validUsername + ", phone: " + validPhone);
               return validName && validType && validEmail && validPhone && validFax;
           }
       }).subscribe(new Action1<Boolean>() {
           @Override
           public void call(Boolean aBoolean) {
                 next_btn_step_1.setEnabled(aBoolean);
           }
       });
    }

    private ValidationResult<String> validateEmail(@NonNull String email) {
        return ValidationUtils.isValidEmailAddress(email);
    }

    private ValidationResult<String> validateUsername(@NonNull String username) {
        return ValidationUtils.isValidUsername(username);
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
        final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://top-africa.appspot.com");

        _storage_subscription = RxFirebaseStorage.putFile(storageRef.child("images/"+logo_uri.getLastPathSegment()),logo_uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<UploadTask.TaskSnapshot, UploadTask.TaskSnapshot>() {
                    @Override
                    public UploadTask.TaskSnapshot call(UploadTask.TaskSnapshot taskSnapshot) {
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
//                        saveDataToFirebaseDatabase();
                    }
                });
    }

    private void saveDataToFirebaseDatabase(){
        try {
            ArrayList<String> dfcd = new ArrayList<>();
            dfcd.add("www.google.com");
            Firebase ref = new Firebase("https://top-africa.firebaseio.com/businesses");
//            Firebase busiRefs = ref.child("businesses");
            Businesse businesse = new Businesse();
            businesse.setNumberEmployes("50");
            businesse.setCity("Jaipur");
            businesse.setCountry("India");
            businesse.setEmail("nirmalgit@gmail.com");
            businesse.setListingtype("Profressional");
            businesse.setName("Nirmal");
            businesse.setOfficeLocation("22" + "," + "23");
            businesse.setPictures(dfcd);
            businesse.setLogo("www.htl.com");
            businesse.setWebsite("www.goole.com");
            businesse.setRoad("adsad");
            businesse.setSuburb("dfcdfcd");
            List<Annotation> anot = new ArrayList<>();
            Annotation ss = new Annotation();
            ss.setLatitude(20.00);
            ss.setLongitude(25.00);
            ss.setTitle("MY TEST");
            anot.add(ss);
            Mapdata mapdata = new Mapdata();
            mapdata.setAnnotations(anot);
            businesse.setMapdata(mapdata);
            List<Day> daylist = new ArrayList<>();
            Day day = new Day();
            day.setDay("MONDAY");
            day.setOpenAt(147513605);
            day.setCloseAt(23232325);
            daylist.add(day);
            Openhours openhours = new Openhours();
            openhours.setDays(daylist);
            openhours.setZone(1);
            businesse.setOpenhours(openhours);
            Category ca = new Category();
            ca.setCategoryname("TEST");
            ref.push().setValue(businesse, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
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
//        uploadFiles();
        saveDataToFirebaseDatabase();
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
