package com.moitbytes.coolieapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moitbytes.coolieapp.ContactSupport;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.UserBookingActivity;
import com.moitbytes.coolieapp.User_Profile;
import com.moitbytes.coolieapp.fcmTokenPojo;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class User_Dashboard extends AppCompatActivity
{
    TextView user_banner;
    SharedPreferences preferences;
    String first_name, last_name;
    String old_token;
    String usr_phone;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__dashboard);
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        old_token = preferences.getString("fcmToken", "");
        usr_phone = preferences.getString("phone", "");

        first_name = preferences.getString("first_name", "");
        last_name = preferences.getString("last_name", "");

        user_banner = findViewById(R.id.user_booking);
        user_banner.setText(first_name+ " "+last_name);

        getNotifications();
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

    private void getNotifications()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            String token = task.getResult().getToken();
                            Log.i("MyToken", token);
                            if(old_token.equals("NoValue") || !(old_token.equals(token)))
                            {
                                //Toast.makeText(MainActivity.this, ""+old_token, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("fcmToken", token);
                                editor.apply();
                                sendFCMTokenToAPI(token);
                            }

                        }
                        else
                        {
                            Toast.makeText(User_Dashboard.this,
                                    "Token Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
    }

    private void sendFCMTokenToAPI(String token)
    {
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("fcmTokens");
        fcmTokenPojo pojo = new fcmTokenPojo(usr_phone, token);
        reference.child(usr_phone).setValue(pojo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}