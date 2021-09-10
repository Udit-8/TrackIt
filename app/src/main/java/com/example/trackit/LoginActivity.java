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
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameText = findViewById(R.id.inputUsername);
        passwordText = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.btnLogin);
        ref = FirebaseDatabase.getInstance().getReference();
        loginButton.setOnClickListener(v -> {
            String username = userNameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
            } else {
                Query checkUser = ref.child("studentInfo").orderByChild("admissionNumber").equalTo(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(LoginActivity.this, "Wrong admission number", Toast.LENGTH_LONG).show();
                        } else {
                            boolean passwordFound = false;
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (childSnapshot != null) {
                                    Student student = childSnapshot.getValue(Student.class);
                                    if (student.getPassword().equals(password)) {
                                        passwordFound = true;
                                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                        intent.putExtra("loginStudent", student);
                                        startActivity(intent);
                                    }
                                }
                            }
                            if (!passwordFound)
                                Toast.makeText(LoginActivity.this, "Wrong Password Entered", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}