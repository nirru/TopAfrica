package net.topafrica.Annuaire.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.activity.createbusiness.CreateBusiness;
import net.topafrica.Annuaire.adapter.RatingAdapter;
import net.topafrica.Annuaire.common.BaseDrawerActivity;
import net.topafrica.Annuaire.modal.category.Businesse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class CheckAndRateActivity extends BaseDrawerActivity {

    @Nullable
    @Bind(R.id.id_search_view)
    SearchView searchView;

    @Nullable
    @Bind(R.id.id_recyleview)
    RecyclerView recyclerView;

    public  static List<Businesse> ITEMS ;
    RatingAdapter ratingAdapter;
    public static final String KEY = "key";
    public static final String KEY_IMAGE = "key_image";
    public static final String KEY_NAME = "key_name";

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        observeState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeService();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (observeSignInSubscription !=null)
            observeSignInSubscription.unsubscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices())
            buildGoogleApiClient();
        setContentView(R.layout.activity_rate);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckAndRateActivity.this));
        setupSearchView();
        queryFirebase("");
    }

    private void setupSearchView() {
        // search hint
        searchView.setQueryHint("Search business by location and rate it");
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
                                queryFirebase(charSequence);
                            }else{
                                ratingAdapter.clearItem();
                                ratingAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    private void queryFirebase(CharSequence charSequence){
        ITEMS = new ArrayList<Businesse>();
        ratingAdapter = new RatingAdapter(ITEMS,CheckAndRateActivity.this);
        ratingAdapter.setOnItemClickListener(new RatingAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.e("PSOOOOO","" + position);
                Intent i = new Intent(CheckAndRateActivity.this, RateMyBusinessActivity.class);
                i.putExtra(KEY,ITEMS.get(position).getKey());
                i.putExtra(KEY_IMAGE,ITEMS.get(position).getLogo());
                i.putExtra(KEY_NAME,ITEMS.get(position).getName());
                startActivity(i);
                overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
            }
        });
        recyclerView.setAdapter(ratingAdapter);

        Firebase ref = new Firebase("https://top-africa-annuaire-1361.firebaseio.com/businesses");

        Query mQuery = ref.orderByChild("city").startAt(charSequence.toString());
//
        mQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Businesse businesse = dataSnapshot.getValue(Businesse.class);
                businesse.setKey(dataSnapshot.getKey());
                ratingAdapter.addItem(businesse);
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

}
