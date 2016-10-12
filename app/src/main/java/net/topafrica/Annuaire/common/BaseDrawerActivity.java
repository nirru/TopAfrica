package net.topafrica.Annuaire.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.activity.LandingActivity;
import net.topafrica.Annuaire.activity.LoginActivity;
import net.topafrica.Annuaire.activity.SplashActivity;
import net.topafrica.Annuaire.activity.createbusiness.CreateBusiness;

import butterknife.Bind;

/**
 * Created by Nirmal Kumar on 1.09.16.
 */
public class BaseDrawerActivity extends BaseActivity {

    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;

    @Bind(R.id.vNavigation)
    NavigationView vNavigation;

    private ActionBarDrawerToggle mDrawerToggle;
    private float lastScale = 1.0f;

    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;
    FrameLayout viewGroup;
    public FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
         viewGroup = (FrameLayout) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
//      setupHeader();
        setUpDrawable();
        onGlobalMenuItemClick();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    @Override
    public void onConnected(Location location) {

    }

    @Override
    public void fireBaseAuthication(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public void onGlobalMenuItemClick(){
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                int id = item.getItemId();
                switch (id){
                    case R.id.id_menu_login:
                         login();
                        break;
                    case R.id.id_menu_logout:
                        logout();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void login(){
        if (firebaseUser!=null){
            Toast.makeText(BaseDrawerActivity.this,"You are already Loged In",Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(BaseDrawerActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    }
    private void logout(){
        showDialog();
        if (firebaseUser != null) {
            String provider = firebaseUser.getProviders().get(0);
            Log.e("sdfd","" + provider);
            if (provider.equals("facebook.com")){
                logoutFromFacebook();
            }else if (provider.equals("google.com")){
                signOutFromGoogle();
            }else if (provider.equals("twitter.com")){
                 logoutTwitter();
            }else{
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
            }
        }
    }

    private void showDialog(){
        progressDialog = new ProgressDialog(BaseDrawerActivity.this);
        progressDialog.setMessage("Please wait while we are signing you out...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }


    private void signOutFromGoogle(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        FirebaseAuth.getInstance().signOut();
                        startLoginActivity();
                    }
                });
    }

    public void logoutFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();

            }
        }).executeAsync();
    }

     public void logoutTwitter() {
         TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
            if (twitterSession != null) {
                ClearCookies(getApplicationContext());
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
            }
      }

    public  void ClearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr= CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void startLoginActivity(){
        Intent i = new Intent(BaseDrawerActivity.this, LoginActivity.class);
        startActivity(i);
        finishAffinity();
        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }


//    private void setupHeader() {
//        View headerView = vNavigation.getHeaderView(0);
//        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
//        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onGlobalMenuHeaderClick(v);
//            }
//        });
//
//        ivMenuUserProfilePhoto.setImageResource(R.drawable.ic_profile);
//
//    }

//    public void onGlobalMenuHeaderClick(final View v) {
//        drawerLayout.closeDrawer(Gravity.LEFT);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 200);
//    }

    private void setUpDrawable(){
        mDrawerToggle = new ActionBarDrawerToggle(BaseDrawerActivity.this,drawerLayout,R.string.app_name,R.string.app_name){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float min = 0.85f;
                float max = 1.0f;
                float scaleFactor = (max - ((max - min) * slideOffset));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    viewGroup.setScaleX(scaleFactor);
                    viewGroup.setScaleY(scaleFactor);
                }
                else
                {
                    ScaleAnimation anim = new ScaleAnimation(lastScale, scaleFactor, lastScale, scaleFactor, viewGroup.getWidth()/2, viewGroup.getHeight()/2);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    viewGroup.startAnimation(anim);

                    lastScale = scaleFactor;
                }
                viewGroup.setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        try {
            drawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }catch (Exception ex){
            ex.printStackTrace();

        }
    }

}
