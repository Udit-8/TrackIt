package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText userNameText, passwordText;
    DatabaseReference ref;
    Button driverButton,studentButton;
    boolean isStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.loginFragment, StudentLoginFragment.class, null).commit();
        }
        driverButton = findViewById(R.id.DriverButton);
        studentButton = findViewById(R.id.StudentButton);
        isStudent = true;
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStudent)
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.loginFragment, StudentLoginFragment.class, null).commit();
                isStudent = true;
            }
        });
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudent)
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.loginFragment, DriverLoginFragment.class, null).commit();
                isStudent = false;
            }
        });
    }
}