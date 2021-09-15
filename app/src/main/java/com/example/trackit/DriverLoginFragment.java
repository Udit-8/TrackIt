package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText userNameText, passwordText;
    DatabaseReference ref;
    Button loginButton;

    public DriverLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverLoginFragment newInstance(String param1, String param2) {
        DriverLoginFragment fragment = new DriverLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userNameText = view.findViewById(R.id.inputUsername);
        passwordText = view.findViewById(R.id.inputPassword);
        loginButton = view.findViewById(R.id.btnLogin);
        ref = FirebaseDatabase.getInstance().getReference();
        loginButton.setOnClickListener(v -> {
            String username = userNameText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_LONG).show();
            } else {
                final boolean[] isDriverLoggedIn = {false};
                ref.child("loginDrivers").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                            isDriverLoggedIn[0] = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(isDriverLoggedIn[0])
                {
                    Toast.makeText(getContext(),"Driver is currently logged in from other device.",Toast.LENGTH_LONG).show();
                }
                else{
                    Query checkUser = ref.child("driverInfo").orderByChild("driverBusNumber").equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(getContext(), "Wrong bus number", Toast.LENGTH_LONG).show();
                            } else {
                                boolean passwordFound = false;
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (childSnapshot != null) {
                                        Driver driver = childSnapshot.getValue(Driver.class);
                                        if (driver.getDriverPassword().equals(password)) {
                                            passwordFound = true;
                                            ref.child("loginDrivers").child(username).setValue(true);
                                            Intent intent = new Intent(getContext(), DriverProfileActivity.class);
                                            intent.putExtra("loginDriver", driver);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                if (!passwordFound)
                                    Toast.makeText(getContext(), "Wrong Password Entered", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }
}