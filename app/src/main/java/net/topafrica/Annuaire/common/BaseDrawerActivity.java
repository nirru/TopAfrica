package net.topafrica.Annuaire.common;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;


import net.topafrica.Annuaire.R;

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
    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
         viewGroup = (FrameLayout) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
//      setupHeader();
        setUpDrawable();
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
        drawerLayout.setDrawerListener(mDrawerToggle);

    }

}
