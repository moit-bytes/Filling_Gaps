package com.moitbytes.coolieapp.Register;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.RetrofitApiService;
import com.moitbytes.coolieapp.User.User_Dashboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Coolie_User_Details extends AppCompatActivity
{
    TextView textView;
    Dialog dialog;
    Retrofit retrofit;
    LinearLayout coolie_station_view, body_layout;
    RadioGroup coolie_yes_no;
    ImageButton backPress;
    EditText fname, lname, user_otp;
    RadioButton selectedMode;
    boolean coolie;
    String station, first_name, last_name, c_yes;
    String usr_email, usr_phone, usr_password;
    ProgressBar progressBar;
    String verificationId;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText rateTrolley, rateBag, rateContainer;
    float trolleyRate, bagRate, contRate;
    boolean OTPtrue = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coolie__user__details);

        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
        Intent i = getIntent();
        usr_email = i.getExtras().getString("email");
        usr_phone = i.getExtras().getString("phone");
        usr_password = i.getExtras().getString("pass");

//        usr_email = "coolie655@gmail.com";
//        usr_phone ="5555555555";
//        usr_password = "12345";

        textView = findViewById(R.id.text_view);
        coolie_station_view = findViewById(R.id.selectTrainSwitch);
        coolie_yes_no = findViewById(R.id.CoolieRadio);
        fname = findViewById(R.id.userFirstName);
        lname = findViewById(R.id.userLastName);
        backPress = findViewById(R.id.register_go_back);
        body_layout = findViewById(R.id.coolie_user_layout);
        user_otp = findViewById(R.id.userOTP);
        progressBar = findViewById(R.id.progress_bar);
        rateBag = findViewById(R.id.rateBag);
        rateContainer = findViewById(R.id.rateContainer);
        rateTrolley = findViewById(R.id.rateTrolley);

        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(usr_phone);

        body_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                closeKeyboard();
                return false;
            }
        });

        coolie_station_view.setVisibility(View.GONE);

        coolie_yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fname.clearFocus();
                lname.clearFocus();
                closeKeyboard();
            }
        });

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                closeKeyboard();
                onBackPressed();
            }
        });

//        user_otp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if(s.length()==6)
//                {
//                    Toast.makeText(Coolie_User_Details.this,
//                            "OTP Verified", Toast.LENGTH_SHORT).show();
//                    user_otp.setFocusable(false);
//                    user_otp.setClickable(false);
//                    user_otp.setEnabled(false);
//                    progressBar.setVisibility(View.GONE);
//                    OTPtrue = true;
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });


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
                    Toast.makeText(Coolie_User_Details.this, "Fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Coolie_User_Details.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(850, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText text = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Coolie_User_Details.this,
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


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Coolie_User_Details.this,
                                    "OTP Verified", Toast.LENGTH_SHORT).show();
                            user_otp.setFocusable(false);
                            user_otp.setClickable(false);
                            user_otp.setEnabled(false);
                            progressBar.setVisibility(View.GONE);
                            OTPtrue = true;
                        }
                        else
                            {

                            //Toast.makeText(Coolie_User_Details.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,
                60,
                TimeUnit.SECONDS,
                Coolie_User_Details.this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null)
            {
                user_otp.setText(code);
                progressBar.setVisibility(View.GONE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {

            Toast.makeText(Coolie_User_Details.this,
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    private void verifyCode(String code)
    {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


    private boolean checkInputFields()
    {

        if(user_otp.getText().toString().isEmpty())
        {
            user_otp.setError("Enter OTP");
            return false;
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            //verifyCode(user_otp.getText().toString());
            if(!OTPtrue)
            {
                //Toast.makeText(this,
                //"In-Correct OTP", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void SendDetailsToFirebase(View view)
    {
        if(checkInputFields())
        {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("stored_credentials", true);
            editor.putString("first_name", first_name);
            editor.putString("last_name", last_name);
            editor.putString("phone", usr_phone);
            editor.putString("email", usr_email);
            editor.putBoolean("coolie", coolie);
            editor.putBoolean("status", false);
            editor.putInt("tot_orders", 0);
            editor.putFloat("wallet_balance", 0.0f);
            if(coolie)
            {
                int k = station.length()-2;
                String code = "";
                while(station.charAt(k)!='(')
                {
                    code = station.charAt(k)+code;
                    k--;
                }
                Toast.makeText(this, "Welcome! "+first_name, Toast.LENGTH_SHORT).show();
                DatabasePojo databasePojo = new DatabasePojo(first_name,
                        last_name, usr_email,
                        usr_phone, usr_password,
                        code,
                        coolie,
                        false,trolleyRate,bagRate,contRate,
                        0, 0.0f);
                reference.child(usr_phone).setValue(databasePojo);
                editor.putString("station", station);
                editor.putFloat("trolleyRate", trolleyRate);
                editor.putFloat("bagRate", bagRate);
                editor.putFloat("contRate", contRate);
                editor.apply();
                Intent i = new Intent(Coolie_User_Details.this, Coolie_Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(this, "Welcome! "+first_name, Toast.LENGTH_SHORT).show();
                DatabasePojo databasePojo = new DatabasePojo
                        (first_name,
                        last_name, usr_email,
                        usr_phone, usr_password,
                        "",
                        coolie,
                        false,0.0f,0.0f,
                                0.0f,0,0.0f);
                reference.child(usr_phone).setValue(databasePojo);
                editor.putString("station", "");
                editor.putFloat("trolleyRate", 0.0f);
                editor.putFloat("bagRate", 0.0f);
                editor.putFloat("contRate", 0.0f);
                editor.apply();

                Intent i = new Intent(Coolie_User_Details.this,
                        User_Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

        }
        else
        {
            Toast.makeText(this, "Fill in details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void OnYesSelect(View view)
    {
        coolie_station_view.setVisibility(View.VISIBLE);
    }

    public void OnNoSelect(View view)
    {
        coolie_station_view.setVisibility(View.GONE);
    }

}