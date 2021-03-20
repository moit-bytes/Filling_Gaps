package com.moitbytes.coolieapp.Coolie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moitbytes.coolieapp.ContactSupport;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.ScanQrActivity;
import com.moitbytes.coolieapp.User.User_Dashboard;
import com.moitbytes.coolieapp.UserBookingActivity;
import com.moitbytes.coolieapp.User_Profile;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coolie__dashboard);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}