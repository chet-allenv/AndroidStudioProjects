package com.example.assignment4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    User user;

    TextView nameLabel;
    TextView emailLabel;
    TextView roleLabel;

    ActivityResultLauncher<Intent> startUpdateUserForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getSerializableExtra("newUser") != null)
            {
                user = (User) result.getData().getSerializableExtra("newUser");
                updateText();
            }
            else
            {
                user = null;
                updateText();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                user = null;
            }
            else
            {
                user = (User) extras.get("newUser");
            }
        }
        else
        {
            user = (User) savedInstanceState.getSerializable("newUser");
        }

        findViewById(R.id.buttonUpdate).setOnClickListener(this);

        nameLabel = findViewById(R.id.textViewNameLabel2);
        emailLabel = findViewById(R.id.textViewEmailLabel2);
        roleLabel = findViewById(R.id.textViewRoleLabel2);

        updateText();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonUpdate)
        {
            Intent updateActivity = new Intent(this, UpdateUserActivity.class);
            updateActivity.putExtra("newUser", user);
            startUpdateUserForResult.launch(updateActivity);
        }
    }

    private void updateText()
    {
        nameLabel.setText(String.format("%s %s", getString(R.string.label_name), user.getName()));
        emailLabel.setText(String.format("%s %s", getString(R.string.label_email), user.getEmail()));
        roleLabel.setText(String.format("%s %s", getString(R.string.label_role), user.getRole().name()));
    }
}