package com.moitbytes.coolieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moitbytes.coolieapp.Coolie.Coolie_All_Orders_Adapter;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.Coolie.Coolie_Order_Pojo;
import com.moitbytes.coolieapp.User.OrderDataPojo;
import com.moitbytes.coolieapp.User.User_Dashboard;

public class UserBookingActivity extends AppCompatActivity
{
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    String type;
    TextView no_booking;
    ProgressBar progressBar;
    SharedPreferences preferences;
    String usr_phone;
    User_All_order_Adapter adapter1;
    Coolie_All_Orders_Adapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking);
        Intent i = getIntent();
        type = i.getExtras().getString("type", "");
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        usr_phone = preferences.getString("phone", "");
        rv = findViewById(R.id.all_orders_recycler);
        no_booking = findViewById(R.id.no_bookings);
        no_booking.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(
                UserBookingActivity.this,
                LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.progress_b);
        progressBar.setVisibility(View.VISIBLE);

        if(type.equals("customer"))
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user_orders");
            Query queries = ref.orderByChild("phone").equalTo(usr_phone);
            queries.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        no_booking.setVisibility(View.GONE);

                        FirebaseRecyclerOptions<OrderDataPojo> options = new
                                FirebaseRecyclerOptions.Builder<OrderDataPojo>()
                                .setQuery(queries, OrderDataPojo.class)
                                .build();

                        adapter1 = new User_All_order_Adapter(options);
                        adapter1.startListening();
                        progressBar.setVisibility(View.GONE);
                        rv.setAdapter(adapter1);
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        no_booking.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("coolie_orders");
            Query queries = ref.orderByChild("phone").equalTo(usr_phone);
            queries.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        no_booking.setVisibility(View.GONE);

                        FirebaseRecyclerOptions<Coolie_Order_Pojo> options = new
                                FirebaseRecyclerOptions.Builder<Coolie_Order_Pojo>()
                                .setQuery(queries, Coolie_Order_Pojo.class)
                                .build();

                        adapter2 = new Coolie_All_Orders_Adapter(options);
                        adapter2.startListening();
                        progressBar.setVisibility(View.GONE);
                        rv.setAdapter(adapter2);
                    }
                    else
                    {
                        no_booking.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }

    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(type.equals("customer"))
        {
            Intent i = new Intent(UserBookingActivity.this,
                    User_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(UserBookingActivity.this,
                    Coolie_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
    }
}