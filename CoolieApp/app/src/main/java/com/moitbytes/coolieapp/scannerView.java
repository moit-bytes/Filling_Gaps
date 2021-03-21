package com.moitbytes.coolieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    ZXingScannerView scannerview;
    SharedPreferences preferences;
    String usr_phone, customer_phone;
    float amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
        usr_phone = preferences.getString("phone", "");
        customer_phone = getIntent().getExtras().getString("cust_phone", "");
        amount = getIntent().getExtras().getFloat("amount", 0.0f);
        scannerview = new ZXingScannerView(this);
        setContentView(scannerview);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                    {
                        scannerview.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest,
                                                                   PermissionToken permissionToken)
                    {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult)
    {
//        rawResult.getText

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("users");
        Query checkUser = databaseReference.orderByChild("phone").equalTo(usr_phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Float amount1 = snapshot.child(usr_phone).child("wallet_balance").
                            getValue(Float.class);
                    Integer tot_order = snapshot.child(usr_phone).child("tot_orders").
                            getValue(Integer.class);
                    databaseReference.child(usr_phone).child("order_completed").setValue(true);
                    databaseReference.child(usr_phone).child("tot_orders").setValue(
                            tot_order+1);
                    databaseReference.child(usr_phone).child("wallet_balance").setValue(
                            amount1+amount);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("wallet_balance", amount1+amount);
                    editor.putInt("tot_orders", tot_order+1);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query checkUser2 = databaseReference.orderByChild("phone").equalTo(customer_phone);
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {

                    Integer tot_order = snapshot.child(customer_phone).child("tot_orders").
                            getValue(Integer.class);
                    databaseReference.child(usr_phone).child("order_completed").setValue(true);
                    databaseReference.child(customer_phone).child("tot_orders").setValue(
                            tot_order+1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this,
                "Order Completed", Toast.LENGTH_LONG).show();
        Intent i = new Intent(scannerView.this, Coolie_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerview.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerview.setResultHandler(this);
        scannerview.startCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}