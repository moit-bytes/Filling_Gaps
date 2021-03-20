package com.moitbytes.coolieapp.Login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moitbytes.coolieapp.Coolie.Coolie_Dashboard;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.User.User_Dashboard;

import static android.content.Context.MODE_PRIVATE;

public class Login_Fragment extends Fragment
{
    TextInputEditText phone, password;
    ConstraintLayout constraintLayout;
    Button login;
    float a = 0;
    ProgressDialog pd;
    String usr_phone, usr_password;
    CheckBox remember_me;
    boolean store_credential = false;
    SharedPreferences preferences;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container,
                false);

        preferences = getActivity().getSharedPreferences("coolie_shared", MODE_PRIVATE);
        phone = root.findViewById(R.id.login_phone);
        password = root.findViewById(R.id.login_password);
        login = root.findViewById(R.id.login);
        constraintLayout = root.findViewById(R.id.loginFragment);
        remember_me = root.findViewById(R.id.remember_me);


        phone.setTranslationX(800);
        password.setTranslationX(800);
        login.setTranslationX(800);

        phone.setAlpha(a);
        password.setAlpha(a);
        login.setAlpha(a);

        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                closeKeyboard();
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkInputFields())
                {
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Logging In");
                    pd.setCancelable(false);
                    pd.show();
                    isUser();
                }
                else
                {
                    Toast.makeText(getActivity(),
                            "Please enter valid details",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        return root;
    }

    private void isUser()
    {
        usr_phone = usr_phone.trim();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("users");

        Query checkUser = databaseReference.orderByChild("phone").equalTo(usr_phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                pd.dismiss();
                if(snapshot.exists())
                {
                    phone.setError(null);
                    String passwordFromDB = snapshot.child(usr_phone).child("password").
                            getValue(String.class);
                    //Toast.makeText(getActivity(), "e"+passwordFromDB, Toast.LENGTH_SHORT).show();

                    if(passwordFromDB.equals(usr_password))
                    {
                        SharedPreferences.Editor editor = preferences.edit();

                        password.setError(null);

                        String fNameFromDB = snapshot.child(usr_phone).child("first_name").
                                getValue(String.class);
                        Toast.makeText(getActivity(), "Welcome Back! "+fNameFromDB, Toast.LENGTH_SHORT).show();
                        String lNameFromDB = snapshot.child(usr_phone).child("last_name").
                                getValue(String.class);
                        String phoneFromDB = snapshot.child(usr_phone).child("phone").
                                getValue(String.class);
                        Boolean coolieFromDB = snapshot.child(usr_phone).child("coolie").
                                getValue(Boolean.class);
                        String email = snapshot.child(usr_phone).child("email").
                                getValue(String.class);
                        String station_code = snapshot.child(usr_phone).child("station_name").
                                getValue(String.class);
                        Boolean status = snapshot.child(usr_phone).child("status").
                                getValue(Boolean.class);

                        Float trolleyRate = snapshot.child(usr_phone).child("trolleyRate").
                                getValue(Float.class);
                        Float bagRate = snapshot.child(usr_phone).child("bagRate").
                                getValue(Float.class);
                        Float contRate = snapshot.child(usr_phone).child("containerRate").
                                getValue(Float.class);
                        Integer tot_order = snapshot.child(usr_phone).child("tot_orders").
                                getValue(Integer.class);

                        Float walletBalance = snapshot.child(usr_phone).child("wallet_balance").
                                getValue(Float.class);

                        editor.putBoolean("stored_credentials", true);
                        editor.putString("first_name", fNameFromDB);
                        editor.putString("last_name", lNameFromDB);
                        editor.putString("phone", phoneFromDB);
                        editor.putString("email", email);
                        editor.putBoolean("coolie", coolieFromDB);
                        editor.putBoolean("status", status);
                        editor.putString("station", station_code);
                        editor.putFloat("trolleyRate", trolleyRate);
                        editor.putFloat("bagRate", bagRate);
                        editor.putFloat("contRate", contRate);
                        editor.putInt("tot_orders", tot_order);
                        editor.putFloat("wallet_balance", walletBalance);
                        editor.apply();
                        if(coolieFromDB)
                        {
                            Intent i = new Intent(getActivity(), Coolie_Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            getActivity().finish();
                        }
                        else
                        {
                            Intent i = new Intent(getActivity(), User_Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            getActivity().finish();
                        }

                    }
                    else
                    {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else
                {
                    phone.setError("No such user exist");
                    phone.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                pd.dismiss();
                Toast.makeText(getActivity(),
                        "Server Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInputFields()
    {

        if(phone.getText().toString().isEmpty() || (phone.getText().toString()).length()!=10)
        {
            phone.setError("Enter valid 10 digit phone number");
            return false;
        }
        else
        {
            usr_phone = phone.getText().toString();
        }


        if(password.getText().toString().isEmpty())
        {
            password.setError("Your Password is required");
            return false;
        }
        else
        {
            usr_password = password.getText().toString().trim();
        }
        if(remember_me.isChecked())
        {
            store_credential = true;
        }

        return true;
    }

    private void closeKeyboard()
    {
        View view = getActivity().getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager inm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
