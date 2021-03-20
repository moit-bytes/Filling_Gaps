package com.moitbytes.coolieapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class BookWheelChairActivity extends AppCompatActivity
{
    EditText user_pnr, req_wheelchair;
    CardView cardView;
    TextView available_wheelchair, select_station;
    String PNR;
    Dialog dialog;
    Retrofit retrofit;
    ProgressDialog pd;
    String station_name;
    TextView dateTextView, timeTextView;
    String date_of_booking, time_of_booking;
    String prev_pnr = "";
    List<String> station_names;
    String station_code;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_wheel_chair);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        cardView = findViewById(R.id.wheelChair_Card);
        cardView.setVisibility(View.GONE);

        user_pnr = findViewById(R.id.userPNR);
        req_wheelchair = findViewById(R.id.userWheelchair);
        select_station = findViewById(R.id.text_view);
        available_wheelchair = findViewById(R.id.available_wheelchair);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);


        station_names = new ArrayList<>();
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
        return true;
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BookWheelChairActivity.this,
                User_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    public void ProceedToBooking(View view)
    {
        if(req_wheelchair.getText().toString().isEmpty())
        {
            req_wheelchair.setError("Enter required wheelchair");
        }
        else
        {
            int num = Integer.parseInt(req_wheelchair.getText().toString());
            if(num>0)
            {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pnr", PNR);
                editor.putString("order_date", date_of_booking);
                editor.putString("order_time", time_of_booking);
                editor.putFloat("amount", num*25.0f);
                editor.apply();
                Intent i = new Intent(BookWheelChairActivity.this,
                        CoolieBookingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("type", "wheelchair");
                i.putExtra("station_name", station_code);
                i.putExtra("n_wheelchair", num);
                startActivity(i);
                finish();
            }
            else
            {
                req_wheelchair.setError("Value should be >0");
            }
        }


    }

    public void SearchForWheelChairs(View view)
    {
        closeKeyboard();
        if(checkInputFields())
        {
            cardView.setVisibility(View.VISIBLE);
            int max = 50;
            int min = 31;
            int range = max - min + 1;
            int rand = (int)(Math.random() * range) + min;
            available_wheelchair.setText(""+rand);
            int k = station_name.length()-1;
            String code = "";
            while(station_name.charAt(k)!=' ')
            {
                code = station_name.charAt(k)+code;
                k--;
            }
            station_code = code;
        }
        else
        {
            cardView.setVisibility(View.GONE);
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
                                Toast.makeText(BookWheelChairActivity.this,
                                        "Train Routes Fetched. You can now select", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(BookWheelChairActivity.this,
                                        "Invalid PNR Please Try again", Toast.LENGTH_SHORT).show();
                                station_names.clear();
                            }
                            pd.dismiss();

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(BookWheelChairActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
            dialog = new Dialog(BookWheelChairActivity.this);
            dialog.setContentView(R.layout.dialog_searchable_spinner);
            dialog.getWindow().setLayout(850, 1200);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText text = dialog.findViewById(R.id.edit_text);
            ListView listView = dialog.findViewById(R.id.list_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(BookWheelChairActivity.this,
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

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}