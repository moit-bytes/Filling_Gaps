package com.moitbytes.coolieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.User.User_Dashboard;

public class ContactSupport extends AppCompatActivity
{

    String support_bot = "https://t.me/Coolie_support_bot'";
    String support_call = "8074010350";
    String support_email = "coolie@gmail.com";
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);
        Intent i = getIntent();
        type = i.getExtras().getString("type", "");

    }

    public void goToUrl(String s)
    {
        Uri uri = Uri.parse(s);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        if(type.equals("customer"))
        {
            Intent i = new Intent(ContactSupport.this,
                    User_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(ContactSupport.this,
                    Coolie_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(0, 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
    }

    public void MakeCall(View view)
    {
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                    {
                        String s = "tel:"+support_call;
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse(s));
                        startActivity(i);

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

    public void startBOT(View view)
    {
        goToUrl(support_bot);
    }
}