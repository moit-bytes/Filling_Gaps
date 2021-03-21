package com.moitbytes.coolieapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.UI.SplashScreen;
import com.moitbytes.coolieapp.User.User_Dashboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class User_Profile extends AppCompatActivity
{
    TextView textView, total_balance, total_orders;
    Dialog dialog;
    Retrofit retrofit;
    LinearLayout coolie_station_view, body_layout;
    RadioGroup coolie_yes_no;
    EditText fname, lname;
    RadioButton selectedMode;
    boolean coolie;
    String station, first_name, last_name, c_yes;
    String usr_email, usr_phone;
    SharedPreferences preferences;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText rateTrolley, rateBag, rateContainer;
    float trolleyRate, bagRate, contRate, wallet_bal;
    int tot_order;
    String type;
    RadioButton coolie_yes, coolie_no;

    TextView banner_name, banner_email, banner_phone;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);

        textView = findViewById(R.id.text_view);
        coolie_station_view = findViewById(R.id.selectTrainSwitch);
        coolie_yes_no = findViewById(R.id.CoolieRadio);
        fname = findViewById(R.id.userFirstName);
        lname = findViewById(R.id.userLastName);
        body_layout = findViewById(R.id.coolie_user_layout);
        rateBag = findViewById(R.id.rateBag);
        rateContainer = findViewById(R.id.rateContainer);
        rateTrolley = findViewById(R.id.rateTrolley);
        total_orders = findViewById(R.id.total_orders);
        total_balance = findViewById(R.id.total_balance);
        coolie_yes = findViewById(R.id.coolieYes);
        coolie_no = findViewById(R.id.coolieNo);
        banner_name = findViewById(R.id.full_name);
        banner_phone = findViewById(R.id.phone_number);
        banner_email = findViewById(R.id.email);

        usr_email = preferences.getString("email", "");
        usr_phone = preferences.getString("phone", "");
        first_name = preferences.getString("first_name", "");
        last_name = preferences.getString("last_name", "");
        coolie = preferences.getBoolean("coolie", false);
        wallet_bal = preferences.getFloat("wallet_balance", 0.0f);
        tot_order = preferences.getInt("tot_orders", 0);

        banner_email.setText(usr_email);
        banner_name.setText(first_name+" "+last_name);
        banner_phone.setText("+91 "+usr_phone);

        fname.setText(first_name);
        lname.setText(last_name);
        total_orders.setText(""+tot_order);
        total_balance.setText(""+wallet_bal);

        reference = FirebaseDatabase.getInstance().getReference("users");


        if(coolie)
        {
            station = preferences.getString("station", "");
            trolleyRate = preferences.getFloat("trolleyRate", 0.0f);
            bagRate = preferences.getFloat("bagRate", 0.0f);
            contRate = preferences.getFloat("contRate", 0.0f);
            coolie_station_view.setVisibility(View.VISIBLE);
            textView.setText(station);
            rateBag.setText(""+bagRate);
            rateContainer.setText(""+contRate);
            rateTrolley.setText(""+trolleyRate);
            coolie_yes.performClick();

        }
        else
        {
            coolie_no.performClick();
        }

        body_layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                closeKeyboard();
                return false;
            }
        });

        coolie_yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                fname.clearFocus();
                lname.clearFocus();
                closeKeyboard();
            }
        });


        List<String> station_names = new ArrayList<>();
        String base_url = "https://9433xu.deta.dev";
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        RetrofitApiService service = retrofit.create(RetrofitApiService.class);
        Call<String> response = service.getStationNames();
        response.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.body());

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject each_node = jsonArray.getJSONObject(i);
                        String name = each_node.getString("name");
                        String code = each_node.getString("code");
                        String res = name + " ("+code+")";
                        station_names.add(res);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(User_Profile.this, "Fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(User_Profile.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(850, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText text = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(User_Profile.this,
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

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        textView.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });

            }
        });

    }


    public void UpdateDetailsOnFirebase(View view)
    {
        if(checkInputFields())
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("first_name", first_name);
            editor.putString("last_name", last_name);
            reference.child(usr_phone).child("first_name").setValue(first_name);
            reference.child(usr_phone).child("last_name").setValue(last_name);
            if(coolie)
            {
                if(station.charAt(station.length()-1) == ')')
                {
                    int k = station.length()-2;
                    String code = "";
                    while(station.charAt(k)!='(')
                    {
                        code = station.charAt(k)+code;
                        k--;
                    }
                    reference.child(usr_phone).child("station_name").setValue(code);
                }

                reference.child(usr_phone).child("coolie").setValue(true);
                reference.child(usr_phone).child("trolleyRate").setValue(trolleyRate);
                reference.child(usr_phone).child("bagRate").setValue(bagRate);
                reference.child(usr_phone).child("containerRate").setValue(contRate);
                editor.putString("station", station);
                editor.putFloat("trolleyRate", trolleyRate);
                editor.putFloat("bagRate", bagRate);
                editor.putFloat("contRate", contRate);
                editor.putBoolean("coolie", true);
            }
            else
            {
                reference.child(usr_phone).child("coolie").setValue(false);
                reference.child(usr_phone).child("station_name").setValue("");
                editor.putString("station", "");
                editor.putBoolean("coolie", false);
            }

            editor.apply();
            reload();
        }
        else
        {
            Toast.makeText(this, "Fill in details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    private boolean checkInputFields()
    {

        if(fname.getText().toString().isEmpty())
        {
            fname.setError("First Name is Required");
            return false;
        }
        else
        {
            first_name = fname.getText().toString();
        }

        if(lname.getText().toString().isEmpty())
        {
            lname.setError("Last Name is Required");
            return false;
        }
        else
        {
            last_name = lname.getText().toString();
        }
        if(coolie_yes_no.getCheckedRadioButtonId() != -1)
        {
            int selected_id = coolie_yes_no.getCheckedRadioButtonId();
            selectedMode = findViewById(selected_id);
            c_yes = selectedMode.getText().toString();
            if(c_yes.equals("Yes"))
            {
                coolie = true;
                if(textView.getText().toString().isEmpty())
                {
                    Toast.makeText(this,
                            "Please Select a Station", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else
                {
                    station = textView.getText().toString();
                }
                coolie_station_view.setVisibility(View.VISIBLE);

                if(rateTrolley.getText().toString().isEmpty())
                {
                    rateTrolley.setError("Please enter the rate");
                    return false;
                }
                else
                {
                    trolleyRate = Float.parseFloat(rateTrolley.getText().toString());
                    if(trolleyRate<=0)
                    {
                        rateTrolley.setError("Enter rate > 0");
                        return false;
                    }
                }
                if(rateBag.getText().toString().isEmpty())
                {
                    rateBag.setError("Please enter the rate");
                    return false;
                }
                else
                {
                    bagRate = Float.parseFloat(rateBag.getText().toString());
                    if(bagRate<=0)
                    {
                        rateBag.setError("Enter rate > 0");
                        return false;
                    }
                }
                if(rateContainer.getText().toString().isEmpty())
                {
                    rateContainer.setError("Please enter the rate");
                    return false;
                }
                else
                {
                    contRate = Float.parseFloat(rateContainer.getText().toString());
                    if(contRate<=0)
                    {
                        rateContainer.setError("Enter rate > 0");
                        return false;
                    }
                }

            }
            else if(c_yes.equals("No"))
            {
                coolie = false;
                coolie_station_view.setVisibility(View.GONE);
            }
        }
        else
        {
            Toast.makeText(this,
                    "Please Select whether you are a Coolie or not",
                    Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void OnYesSelect(View view)
    {
        coolie_station_view.setVisibility(View.VISIBLE);
    }

    public void OnNoSelect(View view)
    {
        coolie_station_view.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed()
    {
        if(coolie)
        {
            Intent i = new Intent(User_Profile.this,
                    Coolie_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else
        {
            Intent i = new Intent(User_Profile.this,
                    User_Dashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }

    public void reload()
    {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void LogOutUser(View view)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Logging Out User!");
        pd.setCancelable(false);
        pd.show();
        MyTask task = new MyTask();
        task.execute();
        FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        pd.dismiss();
        Intent i = new Intent(User_Profile.this, SplashScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}

class MyTask extends AsyncTask<Void, Void, String>
{

    @Override
    protected String doInBackground(Void... voids)
    {
        try
        {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}