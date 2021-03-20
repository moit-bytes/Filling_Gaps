package com.moitbytes.coolieapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.moitbytes.coolieapp.R;

public class AvailableCoolies extends AppCompatActivity
{

    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    Coolie_List_Adater adapter;
    TextView no_coolie;
    ProgressBar progressBar;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_coolies);
        Intent i = getIntent();

        code = i.getExtras().getString("code", "");

        rv = findViewById(R.id.available_coolie_recycler);
        no_coolie = findViewById(R.id.no_coolie);
        no_coolie.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_b);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query queries = ref.orderByChild("station_name").equalTo(code);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {

                    //Toast.makeText(AvailableCoolies.this,
                    //        "data exists",Toast.LENGTH_SHORT).show();
                    no_coolie.setVisibility(View.GONE);
                    layoutManager = new LinearLayoutManager(
                            AvailableCoolies.this,
                            LinearLayoutManager.VERTICAL, false);
                    rv.setLayoutManager(layoutManager);

                    FirebaseRecyclerOptions<model> options = new
                            FirebaseRecyclerOptions.Builder<model>()
                            .setQuery(queries, model.class)
                            .build();

                    adapter = new Coolie_List_Adater(options);
                    adapter.startListening();
                    progressBar.setVisibility(View.GONE);
                    rv.setAdapter(adapter);
                }
                else
                    {
                    Toast.makeText(AvailableCoolies.this,
                            "No Coolies Found",Toast.LENGTH_SHORT).show();
                    no_coolie.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void GoBackToCoolieSearch(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}