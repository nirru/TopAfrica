package net.topafrica.Annuaire.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import net.topafrica.Annuaire.GeoSearchModel;
import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.adapter.CategoryListAdapter;
import net.topafrica.Annuaire.adapter.CategorySearchAdapter;
import net.topafrica.Annuaire.adapter.FavouriteListAdapter;
import net.topafrica.Annuaire.adapter.PlaceSearchAutoAdapter;
import net.topafrica.Annuaire.common.BaseActivity;
import net.topafrica.Annuaire.dummy.DummyContent;
import net.topafrica.Annuaire.modal.category.Businesse;
import net.topafrica.Annuaire.uiView.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LandingActivity extends BaseActivity {

    @Nullable
    @Bind(R.id.id_search_view)
    SearchView searchView;

    @Nullable
    @Bind(R.id.id_search_location)
    AppCompatAutoCompleteTextView autoCompleteTextView;

    @Nullable
    @Bind(R.id.id_recyleview)
    RecyclerView mRecyleView;

    @Nullable
    @Bind(R.id.id_weather_image)
    ImageView weather_image;

    @Nullable
    @Bind(R.id.id_weather_temp)
    TextView weather_temp;

    @Nullable
    @Bind(R.id.id_weather_location)
    TextView weather_location;

    @Nullable
    @Bind(R.id.id_recyleview_category)
    RecyclerView recyclerView;

    @Nullable
    @Bind(R.id.id_scroll_view)
    ScrollView scrollViewContainer;

    @Nullable
    @Bind(R.id.id_recyle_relative)
    RelativeLayout relativeRecyleContainer;



    CategorySearchAdapter categorySearchAdapter;
    PlaceSearchAutoAdapter mAdapter;
    FavouriteListAdapter favouriteListAdapter;

    public  static  List<Businesse> ITEMS ;

    public static String TIMESTAMP_KEY = null;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private static final int VERTICAL_ITEM_SPACE = 30;

    public static final String KEY = "key";

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }
//
    @Override
    protected void onResume() {
        super.onResume();
        resumeService();
    }
//
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices())
            buildGoogleApiClient();
        setContentView(R.layout.activity_landing);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        initInstance();
        initReclyleList();
        setupSearchView();
        placeSearch();
        queryFirebase("shop");
    }

    private void initInstance(){
        favouriteListAdapter = new FavouriteListAdapter(DummyContent.ITEMS,LandingActivity.this);
        favouriteListAdapter.setOnItemClickListener(new FavouriteListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });


//        categorySearchAdapter = new CategorySearchAdapter(ITEMS,LandingActivity.this);
//        categorySearchAdapter.setOnItemClickListener(new CategorySearchAdapter.MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//
//            }
//        });
    }

    private void initReclyleList(){
        //add ItemDecoration
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LandingActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mRecyleView.setLayoutManager(new LinearLayoutManager(LandingActivity.this,LinearLayoutManager.HORIZONTAL,false));
        mRecyleView.setAdapter(favouriteListAdapter);
    }


    private void setupSearchView()
    {
        // search hint
        searchView.setQueryHint(getString(R.string.search_hint));
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
                            if (charSequence.length()!=0){
                                relativeRecyleContainer.setVisibility(View.VISIBLE);
                                scrollViewContainer.setVisibility(View.GONE);
                                queryFirebase(charSequence);
                            }else{
                                categorySearchAdapter.clearItem();
                                relativeRecyleContainer.setVisibility(View.GONE);
                                scrollViewContainer.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                });
    }

    private void queryFirebase(CharSequence charSequence){
        ITEMS = new ArrayList<Businesse>();
        categorySearchAdapter = new CategorySearchAdapter(ITEMS,LandingActivity.this);
        recyclerView.setAdapter(categorySearchAdapter);

        Firebase ref = new Firebase("https://top-africa.firebaseio.com/businesses");

        Query mQuery = ref.orderByChild("category").startAt(charSequence.toString());
//
        mQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.e("" + dataSnapshot.getKey() + " ==","" + dataSnapshot.getValue());
                TIMESTAMP_KEY = dataSnapshot.getKey();
                Businesse businesse = dataSnapshot.getValue(Businesse.class);
                categorySearchAdapter.addItem(businesse);
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



    private void placeSearch(){
        mAdapter = new PlaceSearchAutoAdapter(LandingActivity.this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        autoCompleteTextView.setAdapter(mAdapter);
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i("MAP", "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(LandingActivity.this, "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i("MAP", "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("MAP", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            stopLocationUpdates();

            // Get the Place object from the buffer.
            final Place place = places.get(0);


            // Format details of the place for display and show it in a TextView.
//            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));

            Log.i("MAP", "Place details received: " + place.getName());

            places.release();
        }
    };

    @Override
    public void onConnected(final Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(LandingActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            } else {
                Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                        .setResultCallback(new ResultCallback<WeatherResult>() {
                            @Override
                            public void onResult(@NonNull WeatherResult weatherResult) {
                                if (!weatherResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get weather.");
                                    return;
                                }
                                Weather weather = weatherResult.getWeather();
//                                Log.e(TAG, "Weather: " + weather);
                                setWeatherReport(weather,location);
                            }
                        });
            }
        } else {
            Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<WeatherResult>() {
                        @Override
                        public void onResult(@NonNull WeatherResult weatherResult) {
                            if (!weatherResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get weather.");
                                return;
                            }
                            Weather weather = weatherResult.getWeather();
//                            Log.e(TAG, "Weather: " + weather);
                            setWeatherReport(weather,location);
                        }
                    });
        }

    }

    private void setWeatherReport(Weather weather,Location location){
        assert weather_image != null;
        weather_image.setImageResource(getWeatherIcon(weather));
        assert weather_temp != null;
        weather_temp.setText("Temp:" + weather.getTemperature(1) + "F");
        assert weather_location != null && location !=null;
        weather_location.setText(GeoSearchModel.addressByLocation(new LatLng(location.getLatitude(),location.getLongitude()),LandingActivity.this));
    }

    @OnClick(R.id.id_business_card_1)
    public void wheatherClick(View v){
        Log.e("ssdd","" + "sadfd");
    }
    @OnClick(R.id.id_business_card_2)
    public void restaurentClick(View v){
        startNextActivity("Shop");
    }
    @OnClick(R.id.id_business_card_3)
    public void barClicked(View v){
      startNextActivity("Shop");
    }
    @OnClick(R.id.id_business_card_4)
    public void hotelClicked(View v){
      startNextActivity("Shop");
    }
    @OnClick(R.id.id_business_card_5)
    public void fuelClicked(View v){
       startNextActivity("Shop");
    }
    @OnClick(R.id.id_business_card_6)
    public void parkingClicked(View v){
      startNextActivity("Shop");
    } @OnClick(R.id.id_business_card_7)
    public void pharmaciesClicked(View v){
      startNextActivity("Shop");
    } @OnClick(R.id.id_business_card_8)
    public void hospitalClicked(View v){
     startNextActivity("Shop");
    }
    @OnClick(R.id.id_business_card_9)
    public void medicinesClicked(View v){
      startNextActivity("Shop");
    }


    private void startNextActivity(String type){
        Intent i = new Intent(this, CategoryActivity.class);
        i.putExtra(KEY,type);
        startActivity(i);
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public  void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        try{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
//                recyclerView.animate().setDuration(shortAnimTime).alpha(
//                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
//                    }
//                });
//
//                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                mProgressView.animate().setDuration(shortAnimTime).alpha(
//                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                    }
//                });
//            } else {
//                // The ViewPropertyAnimator APIs are not available, so simply show
//                // and hide the relevant UI components.
//                recyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
//                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//
//    }

}
