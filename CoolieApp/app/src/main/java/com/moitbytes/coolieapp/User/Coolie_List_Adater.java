package com.moitbytes.coolieapp.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.moitbytes.coolieapp.R;

import static android.content.Context.MODE_PRIVATE;

public class Coolie_List_Adater extends FirebaseRecyclerAdapter<model, Coolie_List_Adater.myViewHolder>
{
    Context ct;

    public Coolie_List_Adater(@NonNull FirebaseRecyclerOptions<model> options)
    {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,
                                    int position, @NonNull model model)
    {
        SharedPreferences preferences = ct.getSharedPreferences("coolie_shared", MODE_PRIVATE);
        if(model.isStatus())
        {
            int qty_trolley = preferences.getInt("qty_trolley", 0);
            int qty_container = preferences.getInt("qty_container", 0);
            int qty_bag = preferences.getInt("qty_bag", 0);

            float amount = qty_trolley*model.getTrolleyRate() +
                    qty_container*model.getContainerRate() +
                    qty_bag*model.getBagRate();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("amount", amount);
            editor.apply();

            holder.cardView.setVisibility(View.VISIBLE);
            holder.coolie_name.setText(model.getFirst_name()+" "+model.getLast_name());
            holder.amount.setText("₹ "+amount);
        }
        else
        {
            holder.cardView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.cardView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            holder.cardView.setLayoutParams(params);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ct, CoolieBookingActivity.class);
                i.putExtra("type", "coolie");
                i.putExtra("coolie_name", model.getFirst_name() + " "+
                        model.getLast_name());
                i.putExtra("phone", model.getPhone());
                i.putExtra("station_name", model.getStation_name());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ct.startActivity(i);

            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ct = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coolie_list_item, parent, false);
        Coolie_List_Adater.myViewHolder hold = new Coolie_List_Adater.myViewHolder(v);
        return hold;
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView coolie_name, amount;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cardView = itemView.findViewById(R.id.coolie_list_card);
            coolie_name = itemView.findViewById(R.id.coolie_name);
            amount = itemView.findViewById(R.id.total_bill);
        }
    }

}
