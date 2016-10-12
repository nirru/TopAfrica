package net.topafrica.Annuaire.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import net.topafrica.Annuaire.AppConstant;
import net.topafrica.Annuaire.AppController;
import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.activity.createbusiness.CreateBusiness;
import net.topafrica.Annuaire.common.BaseDrawerActivity;
import net.topafrica.Annuaire.modal.review.Review;

import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class RateMyBusinessActivity extends BaseDrawerActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private static final int TWEET_COMPOSER_REQUEST_CODE = 100;
    private String key = "sdf";
    private String key_image = "sdf";
    private String key_name = "sdf";
    private float rating_float;
    @Nullable
    @Bind(R.id.id_rate_business_image)
    ImageView bussiness_image;

    @Nullable
    @Bind(R.id.id_card_business_rating)
    AppCompatRatingBar ratingBar;

    @Nullable
    @Bind(R.id.id_leave_comment)
    EditText leave_comment;

    @Nullable
    @Bind(R.id.id_cancel)
    TextView cancel;

    @Nullable
    @Bind(R.id.login_progress)
    View mProgressView;

    @Nullable
    @Bind(R.id.login_form)
    View login_form;

    @Nullable
    @Bind(R.id.id_post)
    TextView post;

    @OnClick(R.id.id_cancel)
    public void cancel(View v){
    }
    @OnClick(R.id.id_post)
    public void post(View v){
        if (firebaseUser != null) {
            String provider = firebaseUser.getProviders().get(0);
            Log.e("sdfd","" + provider);
            if (provider.equals("facebook.com")){
                shareOnFacebook();
            }else if (provider.equals("google.com")){
                shareOnGoogle();
            }else if (provider.equals("twitter.com")){
                shareOnTwitter();
            }else{
            }

//            savingTODatabase();
        }
    }

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
        setContentView(R.layout.activity_rate_my_business);
        listenFacebookHandling();
        if (getIntent()!=null){
            key = getIntent().getStringExtra(CheckAndRateActivity.KEY);
            key_image = getIntent().getStringExtra(CheckAndRateActivity.KEY_IMAGE);
            key_name = getIntent().getStringExtra(CheckAndRateActivity.KEY_NAME);
        }

        ratingBarClick();
    }

    private void ratingBarClick(){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating_float = ratingBar.getRating();
            }
        });
    }

    private void listenFacebookHandling(){
        shareDialog = new ShareDialog(RateMyBusinessActivity.this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
//                Toast.makeText(RateMyBusinessActivity.this,"Post shared successfully",Toast.LENGTH_SHORT).show();
                savingTODatabase();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
               error.printStackTrace();
            }
        });
    }

    private void shareOnFacebook(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
            .setContentTitle(key_name)
            .setImageUrl(Uri.parse(key_image))
            .setContentDescription(leave_comment.getText().toString())
            .setContentUrl(Uri.parse(key_image))
            .build();
            shareDialog.show(linkContent);  // Show facebook ShareDialog<br />
        }
    }

    private void shareOnGoogle(){
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(leave_comment.getText().toString())
                .setContentUrl(Uri.parse(key_image))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    private void shareOnTwitter(){
        Intent intent = new TweetComposer.Builder(RateMyBusinessActivity.this)
                .text(leave_comment.getText().toString())
                .createIntent();

        startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TWEET_COMPOSER_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                savingTODatabase();
            } else if(resultCode == Activity.RESULT_CANCELED) {
            }
        }else if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK) {
                savingTODatabase();
            } else if(resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private void savingTODatabase(){
        showProgress(true);
        Firebase ref = new Firebase(AppConstant.FIREBASE_DATABSE_REFRENCE_URL+"reviews");
        Review review = new Review();
        review.setAuthor(firebaseUser.getDisplayName());
        review.setBusiness(key);
        review.setComment(leave_comment.getText().toString());
        review.setDate(Calendar.getInstance().getTimeInMillis());
        review.setRate(rating_float);
        ref.push().setValue(review, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                showProgress(false);
                AppController.getInstance().okAlert(RateMyBusinessActivity.this,"","Upload Successful");
                if (firebaseError != null) {
                    Toast.makeText(RateMyBusinessActivity.this,"Data saved sucessfully",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RateMyBusinessActivity.this,"Data couldn't saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

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


}
