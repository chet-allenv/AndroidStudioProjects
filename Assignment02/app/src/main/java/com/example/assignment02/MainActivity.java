package com.example.assignment02;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Variable Declarations
    final String TAG = "Activity 02";

    // Color integer value
    int red = 64;
    int green = 128;
    int blue = 0;

    // UI object declarations

    // View declaration
    View viewColor;

    // TextView declarations
    TextView textViewHex;
    TextView textViewRGB;
    TextView textViewValueRed;
    TextView textViewValueGreen;
    TextView textViewValueBlue;

    // SeekBar Declarations
    SeekBar seekBarRed;
    SeekBar seekBarGreen;
    SeekBar seekBarBlue;

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

        // View instances
        viewColor = findViewById(R.id.viewColor);

        // TextView instances
        textViewHex = findViewById(R.id.textViewHex);
        textViewRGB = findViewById(R.id.textViewRGB);
        textViewValueRed = findViewById(R.id.textViewValueRed);
        textViewValueGreen = findViewById(R.id.textViewValueGreen);
        textViewValueBlue = findViewById(R.id.textViewValueBlue);

        // SeekBar instances
        seekBarRed = findViewById(R.id.seekBarRed);
        seekBarGreen = findViewById(R.id.seekBarGreen);
        seekBarBlue = findViewById(R.id.seekBarBlue);

        // Button instances
        findViewById(R.id.buttonWhite).setOnClickListener(this);
        findViewById(R.id.buttonBlack).setOnClickListener(this);
        findViewById(R.id.buttonBlue).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);

        // Initializing the color of the viewColor instance
        viewColor.setBackgroundColor(Color.rgb(red, green, blue));

        // Initializing the text contained in the TextView instances
        updateText();
        updateSeekBarValues();

        // Event handling of seekBarChange
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                red = progress;
                updateColor();
                updateText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                green = progress;
                updateColor();
                updateText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");
            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                blue = progress;
                updateColor();
                updateText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonWhite)
        {
            Log.d(TAG, "White Button Pressed");
            red = 255; green = 255; blue = 255;
            updateColor();
            updateText();
            updateSeekBarValues();
        }
        else if (v.getId() == R.id.buttonBlack)
        {
            Log.d(TAG, "Black Button Pressed");
            red = 0; green = 0; blue = 0;
            updateColor();
            updateText();
            updateSeekBarValues();
        }
        else if (v.getId() == R.id.buttonBlue)
        {
            Log.d(TAG, "Blue Button Pressed");
            red = 0; green = 0; blue = 255;
            updateColor();
            updateText();
            updateSeekBarValues();
        }
        else if (v.getId() == R.id.buttonReset)
        {
            Log.d(TAG, "Reset Button Pressed");
            red = 64; green = 128; blue = 0;
            updateColor();
            updateText();
            updateSeekBarValues();
        }
    }

    private void updateText()
    {
        textViewRGB.setText(String.format("%s (%d, %d, %d)", getString(R.string.rgb), red, green, blue));
        textViewHex.setText(String.format("%s %s", getString(R.string.hex), toHex()));
        textViewValueRed.setText(Integer.toString(red));
        textViewValueGreen.setText(Integer.toString(green));
        textViewValueBlue.setText(Integer.toString(blue));

        Log.d(TAG, "Text Changed!");
    }

    private String toHex()
    {
        Log.d(TAG, "Converting red, green, and blue vars to hex string");
        return String.format("#%02X%02X%02X", (0xFF & red), (0xFF & green), (0xFF & blue));
    }

    private void updateColor()
    {
        viewColor.setBackgroundColor(Color.rgb(red, green, blue));
        Log.d(TAG, "Color changed");
    }

    private void updateSeekBarValues()
    {
        seekBarRed.setProgress(red);
        seekBarGreen.setProgress(green);
        seekBarBlue.setProgress(blue);
    }
}