package com.example.assignment4;

import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameEntry;
    EditText emailEntry;

    RadioGroup roles;

    Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameEntry = findViewById(R.id.editTextName);
        emailEntry = findViewById(R.id.editTextTextEmailAddress);

        findViewById(R.id.buttonNext).setOnClickListener(this);

        roles = findViewById(R.id.RadioGroupRole);

        role = Role.Student;

        roles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonStudent)
                {
                    role = Role.Student;
                }
                else if (checkedId == R.id.radioButtonEmployee)
                {
                    role = Role.Employee;
                }
                else if (checkedId == R.id.radioButtonOther)
                {
                    role = Role.Other;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonNext)
        {
            ArrayList<Object> list = gatherData();

            if (list == null)
            {
                return;
            }

            else
            {
                User newUser = generateUser(list);

                Intent profileActivity = new Intent(this, ProfileActivity.class);
                profileActivity.putExtra("newUser", newUser);
                startActivity(profileActivity);
                finish();
            }
        }
    }

    private ArrayList<Object> gatherData()
    {
        var list = new ArrayList<Object>();

        if (nameEntry.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter A Name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (emailEntry.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailEntry.getText().toString()).matches())
        {
            Toast.makeText(this, "Please Enter An Email", Toast.LENGTH_SHORT).show();
            return null;
        }

        list.add(nameEntry.getText().toString());
        list.add(emailEntry.getText().toString());
        list.add(role);

        return list;
    }

    private User generateUser(ArrayList<Object> list)
    {
        return new User((String) list.get(0), (String) list.get(1), (Role) list.get(2));
    }

}