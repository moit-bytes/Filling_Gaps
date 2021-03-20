package com.moitbytes.coolieapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.moitbytes.coolieapp.User.OrderDataPojo;

public class User_All_order_Adapter extends FirebaseRecyclerAdapter<OrderDataPojo,
        User_All_order_Adapter.myViewHolder>
{
    Context ct;
    public User_All_order_Adapter(@NonNull FirebaseRecyclerOptions<OrderDataPojo> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull User_All_order_Adapter.myViewHolder holder,
                                    int position, @NonNull OrderDataPojo model)
    {
        if(model.getType().equals("coolie"))
        {
            holder.order_id.setText(model.getType());
            holder.cust_name.setText(model.getCoolie_first_name()+" "+model.getCoolie_last_name());
            holder.arr_time.setText("Arrival Time: "+model.getOrder_time());
            holder.arr_date.setText("Arrival Date: "+model.getOrder_date());
            holder.amount.setText("Amount: "+model.getAmount());
            holder.n_trolley.setText("Number of Trolleys: "+model.getN_trolley());
            holder.n_bag.setText("Number of Bags: "+model.getN_bags());
            holder.n_cont.setText("Number of Containers: "+model.getN_containers());
        }
        else
        {
            holder.coolie_tag.setVisibility(View.GONE);
            holder.call_customer.setVisibility(View.GONE);
            holder.scan_qr.setVisibility(View.GONE);
            holder.cust_name.setVisibility(View.GONE);
            holder.order_id.setText(model.getType());
            holder.arr_time.setText("Arrival Time: "+model.getOrder_time());
            holder.arr_date.setText("Arrival Date: "+model.getOrder_date());
            holder.amount.setText("Amount: "+model.getAmount());
            holder.n_trolley.setText("Number of Wheelchairs: "+model.getN_wheelchair());
            holder.n_bag.setVisibility(View.GONE);
            holder.n_cont.setVisibility(View.GONE);

        }

        holder.call_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Dexter.withContext(ct).withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                            {
                                String s = "tel:"+model.getCoolie_phone();
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse(s));
                                ct.startActivity(i);

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
        });

        holder.scan_qr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ct, ShowQRActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ct.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public User_All_order_Adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ct = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_all_item,
                parent, false);
        User_All_order_Adapter.myViewHolder hold = new User_All_order_Adapter.myViewHolder(v);
        return hold;
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView order_id, cust_name, arr_time, arr_date;
        TextView n_trolley, n_cont, n_bag, amount;
        Button call_customer, scan_qr;
        CardView cardView;
        TextView coolie_tag;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            cust_name = itemView.findViewById(R.id.customer_name);
            arr_time = itemView.findViewById(R.id.arrival_time);
            arr_date = itemView.findViewById(R.id.order_date);

            call_customer = itemView.findViewById(R.id.callCustomer);
            scan_qr = itemView.findViewById(R.id.scanCustQR);
            cardView = itemView.findViewById(R.id.CurrentOrderCard);
            n_trolley = itemView.findViewById(R.id.n_trolley);
            n_bag = itemView.findViewById(R.id.n_bag);
            n_cont = itemView.findViewById(R.id.n_container);
            amount = itemView.findViewById(R.id.amount);
            coolie_tag = itemView.findViewById(R.id.coolie_tag);
        }
    }
}
