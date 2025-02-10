package com.example.assignment1b;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String TAG = "Assignment 1B";

    EditText editTextTemperature;

    TextView textViewConversion;
    RadioGroup selectionGroup;

    int selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectionGroup = findViewById(R.id.selectionGroup);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        textViewConversion = findViewById(R.id.textViewConversion);

        findViewById(R.id.buttonCalculate).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);
        findViewById(R.id.radioButtonFahrenheit).setOnClickListener(this);


        selectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCelsius)
                {
                    selection = 1;
                }
                else if (checkedId == R.id.radioButtonFahrenheit)
                {
                    selection = 2;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonCalculate)
        {
            double temperature = 0;

            if (selection == 0)
            {
                Toast.makeText(this, "Please make a selection", Toast.LENGTH_SHORT).show();
            }

            else {

                try {

                    var entered = editTextTemperature.getText().toString();
                    temperature = Double.parseDouble(entered);

                } catch (NumberFormatException exception){

                    Log.d(TAG, "Entry box is empty");
                    Toast.makeText(this, "Entry box is empty", Toast.LENGTH_SHORT).show();
                }

                switch (selection){
                    case 1:
                        temperature = (temperature * ((double) 9/5)) + 32;
                        break;
                    case 2:
                        temperature = (temperature - 32) * ((double) 5 /9);
                        break;
                }

                textViewConversion.setText(String.format("Conversion: %.2f", temperature));
            }
        }
        else if (v.getId() == R.id.buttonReset)
        {
            Log.d(TAG, "Reset Button Pressed");

            textViewConversion.setText("Conversion: ");
            editTextTemperature.setText("");

            selectionGroup.clearCheck();
            selection = 0;
        }
    }
}