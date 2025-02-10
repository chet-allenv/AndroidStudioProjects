package com.example.assignment03;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "Assignment 03";

    double discount = 0.1;

    // TextView Declarations
    TextView textViewDiscount;
    TextView textViewSeekbar;
    TextView textViewFinalPrice;

    // Seekbar Declarations
    SeekBar seekBarCustomDiscount;

    // RadioGroup Declarations
    RadioGroup radioGroupPercents;

    // EditTextDecimal Declaration
    EditText editTextDecimalPrice;

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

        findViewById(R.id.buttonCalculate).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);

        textViewDiscount = findViewById(R.id.textViewDiscount);
        textViewSeekbar = findViewById(R.id.textViewSeekbarValue);
        textViewFinalPrice = findViewById(R.id.textViewFinalPrice);

        seekBarCustomDiscount = findViewById(R.id.seekBarCustomDiscount);

        editTextDecimalPrice = findViewById(R.id.editTextNumberDecimalPrice);

        radioGroupPercents = findViewById(R.id.RadioGroupPercents);

        textViewSeekbar.setText(String.format("%d%%", seekBarCustomDiscount.getProgress()));

        radioGroupPercents.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.radioButtonTen)
                {
                    Log.d(TAG, "10% pressed");
                    discount = .1;
                }
                else if (checkedId == R.id.radioButtonFifteen)
                {
                    Log.d(TAG, "15% pressed");
                    discount = .15;
                }
                else if (checkedId == R.id.radioButtonEighteen)
                {
                    Log.d(TAG, "18% pressed");
                    discount = .18;
                }
                else if (checkedId == R.id.radioButtonCustom)
                {
                    Log.d(TAG, "Custom pressed");
                    discount = (double) seekBarCustomDiscount.getProgress() / 100;
                }
            }
        });

        seekBarCustomDiscount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "Seekbar value: " + progress);
                discount = (double) progress / 100;
                textViewSeekbar.setText(String.format("%d%%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }



    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonReset)
        {
            Log.d(TAG, "Reset button pressed");

            textViewFinalPrice.setText(R.string.label_for_final_price);
            textViewDiscount.setText(R.string.label_for_discount);
            seekBarCustomDiscount.setProgress(25);
            textViewSeekbar.setText(String.format("%d%%", seekBarCustomDiscount.getProgress()));
            editTextDecimalPrice.setText("");
        }
        else if (v.getId() == R.id.buttonCalculate)
        {
            Log.d(TAG, "Calculate button pressed");

            calculateAndUpdateDiscount();
        }
    }

    private void calculateAndUpdateDiscount()
    {
        double total_initial;

        try {

            var entered = editTextDecimalPrice.getText().toString();
            total_initial = Double.parseDouble(entered);

            Log.d(TAG, Double.toString(total_initial));

        } catch (NumberFormatException exception){

            Log.d(TAG, "Entry box is empty");
            Toast.makeText(this, "Entry box is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        double discounted = total_initial * discount;

        Log.d(TAG, Double.toString(discounted));

        double finalPrice = total_initial - discounted;

        Log.d(TAG, Double.toString(finalPrice));

        textViewDiscount.setText(String.format("%s %.2f", getString(R.string.label_for_discount), discounted));
        textViewFinalPrice.setText(String.format("%s %.2f", getString(R.string.label_for_final_price), finalPrice));

    }

}