package com.moitbytes.coolieapp.User;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.RetrofitApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BookCoolieActivity extends AppCompatActivity
{
    TextView TrolleyQty, ContQty, BagQty;
    EditText user_pnr;
    TextView select_station;
    String PNR;
    Dialog dialog;
    Retrofit retrofit;
    ProgressDialog pd;
    String station_name;
    TextView dateTextView, timeTextView;
    String date_of_booking, time_of_booking;
    String prev_pnr = "";
    List<String> station_names;
    int quant_trolley=0, quant_cont=0, quant_bag=0;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_coolie);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        user_pnr = findViewById(R.id.userPNR);
        select_station = findViewById(R.id.text_view);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        TrolleyQty = findViewById(R.id.numberTrolley);
        ContQty = findViewById(R.id.numberContainer);
        BagQty = findViewById(R.id.numberBags);


        station_names = new ArrayList<>();
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    public void SelectDate(View view)
    {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                dateTextView.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();


    }

    public void SelectTime(View view)
    {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                timeTextView.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
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
            if(!PNR.equals(prev_pnr))
            {
                select_station.setText(null);
                Toast.makeText(this,
                        "Please select railway station",
                        Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        if(select_station.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please Select Station",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            station_name = select_station.getText().toString();
        }
        if(dateTextView.getText().toString().equals("Select Date"))
        {
            Toast.makeText(this,
                    "Please Select Date of Booking", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            date_of_booking = dateTextView.getText().toString();
        }

        if(timeTextView.getText().toString().equals("Select Time"))
        {
            Toast.makeText(this,
                    "Please Select your expected Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            time_of_booking = timeTextView.getText().toString();
        }

        int q1 = Integer.parseInt(TrolleyQty.getText().toString());
        int q2 = Integer.parseInt(ContQty.getText().toString());
        int q3 = Integer.parseInt(BagQty.getText().toString());
        if(q1 == 0 && q2==0 && q3==0)
        {
            Toast.makeText(this,
                    "Please keep your luggage details", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            quant_trolley = q1;
            quant_cont = q2;
            quant_bag = q3;
        }
        return true;
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void SelectYourStation(View view)
    {
        closeKeyboard();
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Fetching Stations in the route");
        pd.show();
        if(user_pnr.getText().toString().isEmpty())
        {
            pd.dismiss();
            user_pnr.setError("First Enter PNR");
        }
        else
        {
            user_pnr.clearFocus();
            PNR = user_pnr.getText().toString();
            if(station_names.size()==0 || !(PNR.equals(prev_pnr)))
            {
                select_station.setText(null);
                prev_pnr = PNR;
                station_names.clear();
                String base_url = "https://1ldf3h.deta.dev";
                retrofit = new Retrofit.Builder()
                        .baseUrl(base_url)
                        .addConverterFactory(ScalarsConverterFactory.create()).build();

                RetrofitApiService service = retrofit.create(RetrofitApiService.class);
                Call<String> response = service.getStationRoutes(PNR);
                response.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Boolean valid = jsonObject.getBoolean("valid");
                            if(valid)
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("output");
                                for(int i = 0; i<jsonArray.length(); i++)
                                {
                                    String res = jsonArray.getString(i);
                                    station_names.add(res);
                                }
                                Toast.makeText(BookCoolieActivity.this,
                                        "Train Routes Fetched. You can now select", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(BookCoolieActivity.this,
                                        "Invalid PNR Please Try again", Toast.LENGTH_SHORT).show();
                                station_names.clear();
                            }
                            pd.dismiss();

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(BookCoolieActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }

        }

        if(station_names.size()>0)
        {
            pd.dismiss();
            dialog = new Dialog(BookCoolieActivity.this);
            dialog.setContentView(R.layout.dialog_searchable_spinner);
            dialog.getWindow().setLayout(850, 1200);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText text = dialog.findViewById(R.id.edit_text);
            ListView listView = dialog.findViewById(R.id.list_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(BookCoolieActivity.this,
                    android.R.layout.simple_list_item_1, station_names);
            listView.setAdapter(adapter);

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    select_station.setText(adapter.getItem(position));
                    dialog.dismiss();
                }
            });
        }
    }

    public void SearchForCoolies(View view)
    {
        closeKeyboard();
        if(checkInputFields())
        {
            int k = station_name.length()-1;
            String code = "";
            while(station_name.charAt(k)!=' ')
            {
                code = station_name.charAt(k)+code;
                k--;
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("qty_trolley", quant_trolley);
            editor.putInt("qty_container", quant_cont);
            editor.putInt("qty_bag", quant_bag);
            editor.putString("pnr", PNR);
            editor.putString("order_date", date_of_booking);
            editor.putString("order_time", time_of_booking);
            editor.apply();

            Intent i = new Intent(BookCoolieActivity.this, AvailableCoolies.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("code", code);
            startActivity(i);
        }
        else
        {

        }
    }


    public void decrementTrolleyWeight(View view)
    {
        int q1 = Integer.parseInt(TrolleyQty.getText().toString());
        q1 = q1-1;
        if(q1 < 0)
        {
            TrolleyQty.setText("0");
        }
        else
        {
            TrolleyQty.setText(String.valueOf(q1));
        }
    }

    public void incrementTrolleyWeight(View view)
    {
        int q1 = Integer.parseInt(TrolleyQty.getText().toString());
        q1 = q1+1;
        if(q1>50)
        {
            Toast.makeText(this,
                    "A coolie can max lift 50 kgs.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            TrolleyQty.setText(String.valueOf(q1));
        }
    }

    public void decrementContWeight(View view)
    {
        int q1 = Integer.parseInt(ContQty.getText().toString());
        q1 = q1-1;
        if(q1 < 0)
        {
            ContQty.setText("0");
        }
        else
        {
            ContQty.setText(String.valueOf(q1));
        }
    }

    public void incrementContWeight(View view)
    {
        int q1 = Integer.parseInt(ContQty.getText().toString());
        q1 = q1+1;
        if(q1>50)
        {
            Toast.makeText(this,
                    "A coolie can max lift 50 kgs.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ContQty.setText(String.valueOf(q1));
        }
    }

    public void decrementBagWeight(View view)
    {
        int q1 = Integer.parseInt(BagQty.getText().toString());
        q1 = q1-1;
        if(q1 < 0)
        {
            BagQty.setText("0");
        }
        else
        {
            BagQty.setText(String.valueOf(q1));
        }
    }

    public void incrementBagWeight(View view)
    {
        int q1 = Integer.parseInt(BagQty.getText().toString());
        q1 = q1+1;
        if(q1>50)
        {
            Toast.makeText(this,
                    "A coolie can max lift 50 kgs.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            BagQty.setText(String.valueOf(q1));
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BookCoolieActivity.this,
                User_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }
}