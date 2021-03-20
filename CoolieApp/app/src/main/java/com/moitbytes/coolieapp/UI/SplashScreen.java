package com.moitbytes.coolieapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.Login.LoginActivity;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.User.User_Dashboard;

public class SplashScreen extends AppCompatActivity
{
    ImageView app_logo, app_background;
    TextView app_name;
    LottieAnimationView lottieAnimationView;
    SharedPreferences preferences;


    private static final int NUM_PAGE = 3;
    private ViewPager viewPager;
    private screenPageAdapter pageAdapter;

    Animation fade_in;

    public int SPLASH_TIME = 4098;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        app_background = findViewById(R.id.bg_img);
        app_logo = findViewById(R.id.app_logo);
        app_name = findViewById(R.id.appName);
        lottieAnimationView = findViewById(R.id.lotte_anim);

        viewPager = findViewById(R.id.liquidPager);
        pageAdapter = new screenPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        viewPager.startAnimation(fade_in);

        app_background.animate().translationY(-2800).setDuration(1000).setStartDelay(4000);
        app_logo.animate().translationY(2600).setDuration(1000).setStartDelay(4000);
        app_name.animate().translationY(2600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(2600).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
                boolean isFirstTime = preferences.getBoolean("first_time", true);
                boolean store_cred = preferences.getBoolean("stored_credentials", false);
                if(isFirstTime)
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("first_time", false);
                    editor.apply();
                }
                else
                {
                    if(store_cred)
                    {
                        boolean coolie = preferences.getBoolean("coolie", false);
                        if(coolie)
                        {
                            Intent i = new Intent(SplashScreen.this,
                                    Coolie_Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            overridePendingTransition(0, 0);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Intent i = new Intent(SplashScreen.this,
                                    User_Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            overridePendingTransition(0, 0);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }

                }
            }
        },SPLASH_TIME);

    }

    private class screenPageAdapter extends FragmentStatePagerAdapter
    {

        public screenPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    OnBoardingFrag1 tab1 = new OnBoardingFrag1();
                    return tab1;
                case 1:
                    OnBoardingFrag2 tab2 = new OnBoardingFrag2();
                    return tab2;
                case 2:
                    OnBoardingFrag3 tab3 = new OnBoardingFrag3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGE;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}