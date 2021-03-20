package com.moitbytes.coolieapp.Register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.moitbytes.coolieapp.R;

public class Signup_Fragment extends Fragment
{
    TextInputEditText email, password, cnf_password, mobile;
    ConstraintLayout constraintLayout;
    Button signup;
    float a = 0;

    String usr_email, usr_phone, usr_password;
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_fragment, container,
                false);

        email = root.findViewById(R.id.signup_email);
        password = root.findViewById(R.id.signup_password);
        cnf_password = root.findViewById(R.id.signup_confirm_password);
        mobile = root.findViewById(R.id.signup_mobile);
        signup = root.findViewById(R.id.signup);
        constraintLayout = root.findViewById(R.id.constraintSignUp);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                closeKeyboard();
                return false;
            }
        });

        email.setTranslationX(800);
        password.setTranslationX(800);
        cnf_password.setTranslationX(800);
        mobile.setTranslationX(800);
        signup.setTranslationX(800);

        email.setAlpha(a);
        password.setAlpha(a);
        cnf_password.setAlpha(a);
        mobile.setAlpha(a);
        signup.setAlpha(a);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        cnf_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                closeKeyboard();
                if(checkInputFields())
                {
                    Intent i = new Intent(getActivity(), Coolie_User_Details.class);
                    i.putExtra("email", usr_email);
                    i.putExtra("phone", usr_phone);
                    i.putExtra("pass", usr_password);
                    startActivity(i);
                }

            }
        });
        return root;
    }

    private boolean checkInputFields()
    {

        if(email.getText().toString().isEmpty())
        {
            email.setError("Your email is required");
            return false;
        }
        else
        {
            usr_email = email.getText().toString();
        }

        if(mobile.getText().toString().isEmpty() || (mobile.getText().toString()).length()!=10)
        {
            mobile.setError("Enter a valid 10 digit number");
            return false;
        }
        else
        {
            usr_phone = mobile.getText().toString();
        }

        if(password.getText().toString().isEmpty())
        {
            password.setError("Your Password is required");
            return false;
        }
        if(cnf_password.getText().toString().isEmpty())
        {
            cnf_password.setError("Your Password is required");
            return false;
        }
        else
        {
            String pass1 = password.getText().toString();
            String pass2 = cnf_password.getText().toString();
            if(pass1.equals(pass2))
            {
                usr_password = password.getText().toString();
            }
            else
            {
                Toast.makeText(getActivity(),
                        "Your Password doesn't match", Toast.LENGTH_SHORT).show();
                cnf_password.setError("Please enter the correct password");
            }

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
