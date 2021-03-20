package com.moitbytes.coolieapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.RetrofitApiService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CheckPNRActivity extends AppCompatActivity
{
    EditText user_pnr;
    TextView pnr_valid_or_not;
    TextView source, destination, start_time, dest_time, date_of_journey,
            chart_status, train_class, train_name;

    CardView cardView;

    String PNR;
    Retrofit retrofit;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_p_n_r);

        user_pnr = findViewById(R.id.userPNR);

        pnr_valid_or_not = findViewById(R.id.PNR_details_switch);
        pnr_valid_or_not.setVisibility(View.GONE);
        cardView = findViewById(R.id.pnr_detail_card);
        cardView.setVisibility(View.GONE);

        source = findViewById(R.id.source_station);
        destination = findViewById(R.id.destination_station);
        start_time = findViewById(R.id.start_time);
        dest_time = findViewById(R.id.destination_time);
        date_of_journey = findViewById(R.id.date_of_journey);
        chart_status = findViewById(R.id.status);
        train_class = findViewById(R.id.train_class);
        train_name = findViewById(R.id.train_name);
        pd = new ProgressDialog(CheckPNRActivity.this);

    }

    public boolean checkInputFields()
    {
        if(user_pnr.getText().toString().isEmpty())
        {
            user_pnr.setError("Enter your PNR");
            return false;
        }
        else
        {
            PNR = user_pnr.getText().toString();
        }
        return true;
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CheckPNRActivity.this,
                User_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    public void SearchPNRDetails(View view)
    {
        closeKeyboard();
        if(checkInputFields())
        {
            pd.setMessage("Fetching PNR Details");
            pd.setCancelable(false);
            pd.show();
            String base_url = "https://1ldf3h.deta.dev";
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(ScalarsConverterFactory.create()).build();

            RetrofitApiService service = retrofit.create(RetrofitApiService.class);
            Call<String> response = service.getPNRDetails(PNR);
            response.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response)
                {
                    pd.dismiss();
                    if(response.isSuccessful())
                    {
                        try
                        {
                            assert response.body() != null;
                            JSONObject jsonObject = new JSONObject(response.body());
                            JSONObject data = jsonObject.getJSONObject("valid");
                            Boolean valid = data.getBoolean("valid");

                            if(valid)
                            {
                                String src = data.getString("src");
                                String dst = data.getString("dst");
                                String st_time = data.getString("start_time");
                                String dt_time = data.getString("dest_time");
                                String doj = data.getString("date_of_jrny");
                                String stat = data.getString("status");
                                String cl = data.getString("class");
                                String train_n = data.getString("train_name");

                                source.setText("Source: "+src);
                                destination.setText("Destination: "+dst);
                                start_time.setText("Start Time: "+st_time);
                                dest_time.setText("Destination Time: "+dt_time);
                                date_of_journey.setText("Date of Journey: "+doj);
                                chart_status.setText("Chart Status: "+stat);
                                train_class.setText("Train Class: "+cl);
                                train_name.setText("Train-Name :"+train_n);

                                cardView.setVisibility(View.VISIBLE);
                                pnr_valid_or_not.setText("PNR Details Found");
                                pnr_valid_or_not.setVisibility(View.VISIBLE);
                                pnr_valid_or_not.setTextColor(Color.parseColor("#4CAF50"));
                            }
                            else
                            {
                                cardView.setVisibility(View.GONE);
                                pnr_valid_or_not.setText("PNR Details Not Found");
                                pnr_valid_or_not.setVisibility(View.VISIBLE);
                                pnr_valid_or_not.setTextColor(Color.parseColor("#FF5722"));

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(CheckPNRActivity.this,
                                "Couldn't Fetch PNR Details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t)
                {
                    Toast.makeText(CheckPNRActivity.this,
                            "Couldn't Fetch PNR Details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void closeKeyboard()
    {
        View view = getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}