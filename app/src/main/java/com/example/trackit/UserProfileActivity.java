package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfileActivity extends AppCompatActivity {
    TextInputLayout fullNameLayout,admissionNoLayout,busNoLayout,contactNoLayout;
    TextView headerNameTextView;
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
        setTextFields(loginStudent);
    }

    private void setTextFields(Student student) {
        fullNameLayout.getEditText().setText(student.getStudentName());
        admissionNoLayout.getEditText().setText(student.getAdmissionNumber());
        busNoLayout.getEditText().setText(student.getBusNumber());
        contactNoLayout.getEditText().setText(student.getMobileNumber());
        headerNameTextView.setText(student.getStudentName());
    }
}