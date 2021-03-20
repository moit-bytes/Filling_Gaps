package com.moitbytes.coolieapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moitbytes.coolieapp.R;
import com.moitbytes.coolieapp.ReportIssuePojo;

public class Report_Issue_Activity extends AppCompatActivity
{
    EditText subject;
    EditText detailed_issue;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String sub, body, phone;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__issue_);

        subject = findViewById(R.id.subjectIssue);
        detailed_issue = findViewById(R.id.descriptionIssue);
        preferences = getSharedPreferences("coolie_shared", MODE_PRIVATE);
        phone = preferences.getString("phone", "");
    }

    public void GoBackToDashboard(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        closeKeyboard();
        Intent i = new Intent(Report_Issue_Activity.this,
                User_Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    public void SendTicketToFirebase(View view)
    {
        closeKeyboard();
        if(checkInputFields())
        {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("issues");
            ReportIssuePojo pojo = new ReportIssuePojo(phone, sub, body);
            reference.child(phone).setValue(pojo);
            Toast.makeText(this,
                    "Your Issue was submitted successfully", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkInputFields()
    {
        if(subject.getText().toString().isEmpty())
        {
            subject.setError("Please enter a subject");
            return false;
        }
        else
        {
            sub = subject.getText().toString();
            subject.clearFocus();
        }

        if(detailed_issue.getText().toString().isEmpty())
        {
            detailed_issue.setError("Please describe your issue");
            return false;
        }
        else
        {
            body = detailed_issue.getText().toString();
            detailed_issue.clearFocus();
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
}