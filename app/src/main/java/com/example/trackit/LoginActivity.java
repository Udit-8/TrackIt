package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText userNameText,passwordText;
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                if( TextUtils.isEmpty(username))
                {
                    Toast.makeText(LoginActivity.this,"Please enter a username",Toast.LENGTH_LONG).show();
                }
                else if ( TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this,"Please enter a password",Toast.LENGTH_LONG).show();
                }
                else{
                    DatabaseReference reference = ref.child("Authentication");
                    reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(!task.isSuccessful() || !task.getResult().exists())
                            {
                                Toast.makeText(LoginActivity.this,"Username does not exists",Toast.LENGTH_LONG).show();
                            }
                            else{
                                String passwordInDatabase = task.getResult().child("Password").getValue().toString();
                                if(passwordInDatabase.equals(password))
                                {
                                    Toast.makeText(LoginActivity.this,"Signed in Successfully",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}