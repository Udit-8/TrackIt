package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPasswordText,newPasswordText,confirmPasswordText;
    Button changePasswordButton;
    TextView backTextView;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("loginStudent");
        currentPasswordText = findViewById(R.id.currentPasswordText);
        newPasswordText = findViewById(R.id.newPasswordText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        changePasswordButton = findViewById(R.id.changePassword);
        backTextView = findViewById(R.id.backText);
        ref = FirebaseDatabase.getInstance().getReference().child("studentInfo");
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword,newPassword,confirmPassword;
                currentPassword = currentPasswordText.getText().toString().trim();
                newPassword = newPasswordText.getText().toString().trim();
                confirmPassword = confirmPasswordText.getText().toString().trim();
                if(TextUtils.isEmpty(currentPassword)||TextUtils.isEmpty(newPassword)||TextUtils.isEmpty(confirmPassword))
                    Toast.makeText(ChangePasswordActivity.this,"Do not leave any fields empty",Toast.LENGTH_LONG).show();
                else{
                    Query checkUser = ref.orderByChild("admissionNumber").equalTo(student.getAdmissionNumber());

                    final String[] key = new String[1];
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                boolean passwordFound = false;
                                for(DataSnapshot childSnapShot : snapshot.getChildren())
                                {
                                    if(childSnapShot != null)
                                    {
                                         key[0] = childSnapShot.getKey();
                                        Student foundStudent = childSnapShot.getValue(Student.class);
                                        if(foundStudent.getPassword().equals(currentPassword))
                                        {
                                            passwordFound = true;
                                        }
                                    }
                                }
                                if(!passwordFound)
                                {
                                    Toast.makeText(ChangePasswordActivity.this,"Enter correct current password",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if(!newPassword.equals(confirmPassword))
                                    {
                                        Toast.makeText(ChangePasswordActivity.this,"The passwords do not match.Please enter the same password",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        ref.child(key[0]).child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(ChangePasswordActivity.this,"The password was updated",Toast.LENGTH_LONG).show();
                                                    Intent newIntent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                                                    newIntent.putExtra("loginStudent",student);
                                                    startActivity(newIntent);
                                                }
                                                else{
                                                    Toast.makeText(ChangePasswordActivity.this,"Some Error occured.Please try Later",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                newIntent.putExtra("loginStudent",student);
                startActivity(newIntent);
            }
        });
    }
}