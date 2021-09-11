package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

public class UserProfileActivity extends AppCompatActivity {
    TextInputLayout fullNameLayout,admissionNoLayout,busNoLayout,contactNoLayout;
    TextView headerNameTextView;
    MaterialCardView changePasswordCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        Student loginStudent = (Student) intent.getSerializableExtra("loginStudent");
        fullNameLayout = findViewById(R.id.fullNameLayout);
        admissionNoLayout = findViewById(R.id.admissionNumberLayout);
        busNoLayout = findViewById(R.id.busNoLayout);
        contactNoLayout = findViewById(R.id.contactNoLayout);
        headerNameTextView = findViewById(R.id.headerNameText);
        changePasswordCardView = (MaterialCardView) findViewById(R.id.changePasswordCard);
        setTextFields(loginStudent);
        changePasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
                newIntent.putExtra("loginStudent",loginStudent);
                startActivity(newIntent);
            }
        });
    }

    private void setTextFields(Student student) {
        fullNameLayout.getEditText().setText(student.getStudentName());
        admissionNoLayout.getEditText().setText(student.getAdmissionNumber());
        busNoLayout.getEditText().setText(student.getBusNumber());
        contactNoLayout.getEditText().setText(student.getMobileNumber());
        headerNameTextView.setText(student.getStudentName());
    }
}