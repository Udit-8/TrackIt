package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {
    TextInputLayout fullNameLayout,admissionNoLayout,busNoLayout,contactNoLayout;
    TextView headerNameTextView;
    MaterialCardView changePasswordCardView,busLocationCardView;
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
        busLocationCardView = (MaterialCardView) findViewById(R.id.busLocationCard);
        setTextFields(loginStudent);
        busLocationCardView.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Active Buses");
            if(ref != null)
            {
                ref.child(loginStudent.getBusNumber()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().getValue() != null){
                                Intent newIntent = new Intent(UserProfileActivity.this,StudentMapsActivity.class);
                                newIntent.putExtra("busNumber",loginStudent.getBusNumber());
                                startActivity(newIntent);
                            }
                            else{
                                Toast.makeText(UserProfileActivity.this,"Your Driver is not currently using the app",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                            Toast.makeText(UserProfileActivity.this,"Your Driver is not currently using the app",Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                Toast.makeText(UserProfileActivity.this,"Your Driver is not currently using the app",Toast.LENGTH_LONG).show();
            }
        });
        changePasswordCardView.setOnClickListener(v -> {
            Intent newIntent = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            newIntent.putExtra("loginStudent",loginStudent);
            newIntent.putExtra("isStudent",true);
            startActivity(newIntent);
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