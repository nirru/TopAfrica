package net.topafrica.Annuaire;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

import static net.topafrica.Annuaire.AppConstant.TWITTER_KEY;
import static net.topafrica.Annuaire.AppConstant.TWITTER_SECRET;

/**
 * Created by ericbasendra on 16/09/16.
 */
public class AppController extends Application{

    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public boolean isOnline(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public void showInternetConnectionDialog(final Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                        ((AppCompatActivity)mContext).finishAffinity();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void showSettingsAlert(final Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(getString(R.string.setting_dialog_title));
        builder.setMessage(getString(R.string.setting_dialog_message));

        // On pressing Settings button
        builder.setPositiveButton(mContext.getString(R.string.agree), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.disagree), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void okAlert(final Context mContext,String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setMessage(message);

        // On pressing Settings button
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                ((AppCompatActivity)mContext).finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

}
