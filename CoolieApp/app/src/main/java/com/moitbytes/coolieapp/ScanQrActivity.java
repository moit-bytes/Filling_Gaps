package com.moitbytes.coolieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.Coolie.Coolie_Order_Pojo;
import com.moitbytes.coolieapp.User.AvailableCoolies;
import com.moitbytes.coolieapp.User.Coolie_List_Adater;
import com.moitbytes.coolieapp.User.User_Dashboard;
import com.moitbytes.coolieapp.User.model;

public class ScanQrActivity extends AppCompatActivity
{

    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    TextView no_order;
    ProgressBar progressBar;
    String coolie_phone;
    SharedPreferences preferences;
    ScanQrAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
        coolie_phone = preferences.getString("phone", "");

        rv = findViewById(R.id.scanQrRecycler);
        no_order = findViewById(R.id.no_scan_order);
        no_order.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_scan);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("coolie_orders");
        Query queries = ref.orderByChild("phone").equalTo(coolie_phone);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {

                    //Toast.makeText(AvailableCoolies.this,
                    //        "data exists",Toast.LENGTH_SHORT).show();
                    no_order.setVisibility(View.GONE);
                    layoutManager = new LinearLayoutManager(
                            ScanQrActivity.this,
                            LinearLayoutManager.VERTICAL, false);
                    rv.setLayoutManager(layoutManager);

                    FirebaseRecyclerOptions<Coolie_Order_Pojo> options = new
                            FirebaseRecyclerOptions.Builder<Coolie_Order_Pojo>()
                            .setQuery(queries, Coolie_Order_Pojo.class)
                            .build();

                    adapter = new ScanQrAdapter(options);
                    adapter.startListening();
                    progressBar.setVisibility(View.GONE);
                    rv.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(ScanQrActivity.this,
                            "No Pending Orders Found",Toast.LENGTH_SHORT).show();
                    no_order.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GoBackToCoolieDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ScanQrActivity.this,
                Coolie_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }
}