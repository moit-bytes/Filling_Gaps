package com.moitbytes.coolieapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moitbytes.coolieapp.ContactSupport;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.UserBookingActivity;
import com.moitbytes.coolieapp.User_Profile;

public class User_Dashboard extends AppCompatActivity
{
    TextView user_banner;
    SharedPreferences preferences;
    String first_name, last_name;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__dashboard);
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
        first_name = preferences.getString("first_name", "");
        last_name = preferences.getString("last_name", "");

        user_banner = findViewById(R.id.user_booking);
        user_banner.setText(first_name+ " "+last_name);
    }

    public void BookYourCoolie(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, BookCoolieActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void BookYourWheelChair(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, BookWheelChairActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void CheckYourPNRStatus(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, CheckPNRActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void CheckAllBookings(View view)
    {
        Intent i = new Intent(User_Dashboard.this,
                UserBookingActivity.class);
        i.putExtra("type", "customer");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void ReportAnIssue(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, Report_Issue_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void ContactUs(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, ContactSupport.class);
        i.putExtra("type", "customer");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void ViewUserProfile(View view)
    {
        Intent i = new Intent(
                User_Dashboard.this, User_Profile.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}