package com.moitbytes.coolieapp.Coolie;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moitbytes.coolieapp.ContactSupport;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.ScanQrActivity;
import com.moitbytes.coolieapp.UserBookingActivity;
import com.moitbytes.coolieapp.User_Profile;
import com.moitbytes.coolieapp.fcmTokenPojo;

import retrofit2.Retrofit;

public class Coolie_Dashboard extends AppCompatActivity
{
    TextView coolie_name;
    LinearLayout reach_to_customer;
    TextView total_orders, total_balance;
    SwitchMaterial status_switch;

    SharedPreferences preferences;
    String first_name, last_name;
    String usr_phone;
    float wallet_bal;
    int tot_order;
    Boolean status;
    DatabaseReference reference;
    String old_token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coolie__dashboard);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        old_token = preferences.getString("fcmToken", "");
        usr_phone = preferences.getString("phone", "");
        first_name = preferences.getString("first_name", "");
        last_name = preferences.getString("last_name", "");
        status = preferences.getBoolean("status", false);
        wallet_bal = preferences.getFloat("wallet_balance", 0.0f);
        tot_order = preferences.getInt("tot_orders", 0);

        coolie_name = findViewById(R.id.banner_coolie_name);
        total_orders = findViewById(R.id.total_orders);
        total_balance = findViewById(R.id.total_balance);
        status_switch = findViewById(R.id.status_switch);


        reach_to_customer = findViewById(R.id.reachToCustomer);

        coolie_name.setText(first_name+" "+last_name);
        total_orders.setText(""+tot_order);
        total_balance.setText("₹ "+wallet_bal);

        status_switch.setChecked(status);
        reference = FirebaseDatabase.getInstance().getReference("users");

        status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("status", isChecked);
                editor.apply();
                if(isChecked)
                {
                    reference.child(usr_phone).child("status").setValue(true);
                }
                else
                {
                    reference.child(usr_phone).child("status").setValue(false);
                }

            }
        });

        getNotifications();

    }



    public void openAllOrders(View view)
    {
        Intent i = new Intent(Coolie_Dashboard.this,
                UserBookingActivity.class);
        i.putExtra("type", "coolie");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void openContactSupport(View view)
    {
        Intent i = new Intent(
                Coolie_Dashboard.this, ContactSupport.class);
        i.putExtra("type", "coolie");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    public void ViewProfileActivity(View view)
    {
        Intent i = new Intent(Coolie_Dashboard.this,
                User_Profile.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }


    public void scanQR(View view)
    {
        Intent i = new Intent(Coolie_Dashboard.this,
                ScanQrActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void getNotifications()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotifications",
                    "MyNotifications",
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
                            Toast.makeText(Coolie_Dashboard.this,
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