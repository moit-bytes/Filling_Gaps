package com.moitbytes.coolieapp.Login;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moitbytes.coolieapp.Register.Signup_Fragment;

public class LoginAdapter extends FragmentPagerAdapter
{
    private Context context;
    int total_tabs;

    public LoginAdapter(FragmentManager fm, Context ct, int total_tabs)
    {
        super(fm);
        context = ct;
        this.total_tabs = total_tabs;
    }

    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                Login_Fragment lf = new Login_Fragment();
                return lf;
            case 1:
                Signup_Fragment sf = new Signup_Fragment();
                return sf;
        }
        return null;
    }

    @Override
    public int getCount() {
        return total_tabs;
    }
}
