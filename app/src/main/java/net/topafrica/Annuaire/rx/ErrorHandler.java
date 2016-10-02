package net.topafrica.Annuaire.rx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import rx.functions.Action1;

public class ErrorHandler implements Action1<Throwable> {
    private Context mContext;
    public ErrorHandler(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public void call(Throwable throwable) {
//        Toast.makeText(mContext, "Error occurred.", Toast.LENGTH_SHORT).show();
        Log.e("MainActivity", "Error occurred", throwable);
    }
}