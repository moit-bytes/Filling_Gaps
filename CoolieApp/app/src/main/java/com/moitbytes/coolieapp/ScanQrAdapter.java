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
import com.moitbytes.coolieapp.Coolie.Coolie_Order_Pojo;
import com.moitbytes.coolieapp.User.Coolie_List_Adater;

public class ScanQrAdapter extends FirebaseRecyclerAdapter<Coolie_Order_Pojo, ScanQrAdapter.myViewHolder>
{
    Context ct;
    public ScanQrAdapter(@NonNull FirebaseRecyclerOptions<Coolie_Order_Pojo> options)
    {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position,
                                    @NonNull Coolie_Order_Pojo model)
    {
        if(!(model.isOrder_completed()))
        {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.order_id.setText(model.getOrder_id());
            holder.cust_name.setText(model.getCustomer_first_name()+" "+model.getCustomer_last_name());
            holder.train_name.setText("Train Name: "+model.getTrain_name());
            holder.arr_time.setText("Arrival Time: "+model.getOrder_time());
            holder.arr_date.setText("Arrival Date: "+model.getOrder_date());
            holder.ord_status.setVisibility(View.VISIBLE);
            holder.amount.setText("Amount: "+model.getAmount());
            holder.n_trolley.setText("Number of Trolleys: "+model.getN_trolley());
            holder.n_bag.setText("Number of Bags: "+model.getN_bags());
            holder.n_cont.setText("Number of Containers: "+model.getN_containers());
        }
        else
        {
            holder.cardView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = holder.cardView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            holder.cardView.setLayoutParams(params);
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
                                String s = "tel:"+model.getCustomer_phone();
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
                Intent i = new Intent(ct, scannerView.class);
                i.putExtra("amount", model.getAmount());
                i.putExtra("cust_phone", model.getCustomer_phone());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_qr_item,
                parent, false);
        ScanQrAdapter.myViewHolder hold = new ScanQrAdapter.myViewHolder(v);
        return hold;
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView order_id, cust_name, train_name, arr_time, arr_date, ord_status;
        TextView n_trolley, n_cont, n_bag, amount;
        Button call_customer, scan_qr;
        CardView cardView;
        
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            cust_name = itemView.findViewById(R.id.customer_name);
            train_name = itemView.findViewById(R.id.train_name);
            arr_time = itemView.findViewById(R.id.arrival_time);
            arr_date = itemView.findViewById(R.id.order_date);
            ord_status = itemView.findViewById(R.id.order_status);

            call_customer = itemView.findViewById(R.id.callCustomer);
            scan_qr = itemView.findViewById(R.id.scanCustQR);
            cardView = itemView.findViewById(R.id.CurrentOrderCard);
            n_trolley = itemView.findViewById(R.id.n_trolley);
            n_bag = itemView.findViewById(R.id.n_bag);
            n_cont = itemView.findViewById(R.id.n_container);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
