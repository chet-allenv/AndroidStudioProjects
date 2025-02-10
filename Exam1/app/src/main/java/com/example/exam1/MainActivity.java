package com.example.exam1;

import android.graphics.Color;
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



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Object declarations
    TextView MaltCount;
    TextView BeerCount;
    TextView WineCount;
    TextView LiquorCount;
    TextView BAC_message;
    TextView message;
    EditText WeightEntry;
    RadioGroup gender;
    boolean isFemale = true;

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
        findViewById(R.id.buttonMinusLiquor).setOnClickListener(this);
        findViewById(R.id.buttonMinusBeer).setOnClickListener(this);
        findViewById(R.id.buttonMinusMalt).setOnClickListener(this);
        findViewById(R.id.buttonMinusWine).setOnClickListener(this);
        findViewById(R.id.buttonPlusBeer).setOnClickListener(this);
        findViewById(R.id.buttonPlusWine).setOnClickListener(this);
        findViewById(R.id.buttonPlusLiquor).setOnClickListener(this);
        findViewById(R.id.buttonPlusMalt).setOnClickListener(this);

        MaltCount = findViewById(R.id.textViewMaltCount);
        BeerCount = findViewById(R.id.textViewBeerCount);
        WineCount = findViewById(R.id.textViewWineCount);
        LiquorCount = findViewById(R.id.textViewLiquorCount);

        WeightEntry = findViewById(R.id.editTextNumberWeight);

        BAC_message = findViewById(R.id.textViewBAC);
        message = findViewById(R.id.textViewMessage);

        //: 34, g: 139, b: 34
        message.setBackgroundColor(Color.rgb(34, 139, 34));
        message.setText(R.string.safe);

        gender = findViewById(R.id.RadioGroupGender);


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonFemale)
                {
                    isFemale = true;
                }
                else if (checkedId == R.id.radioButtonMale)
                {
                    isFemale = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonPlusBeer)
        {
            onIncrementButtonPress(true, BeerCount);
        }
        else if (v.getId() == R.id.buttonPlusWine)
        {
            onIncrementButtonPress(true, WineCount);
        }
        else if (v.getId() == R.id.buttonPlusLiquor)
        {
            onIncrementButtonPress(true, LiquorCount);
        }
        else if (v.getId() == R.id.buttonPlusMalt)
        {
            onIncrementButtonPress(true, MaltCount);
        }
        else if (v.getId() == R.id.buttonMinusBeer)
        {
            onIncrementButtonPress(false, BeerCount);
        }
        else if (v.getId() == R.id.buttonMinusLiquor)
        {
            onIncrementButtonPress(false,LiquorCount);
        }
        else if (v.getId() == R.id.buttonMinusMalt)
        {
            onIncrementButtonPress(false, MaltCount);
        }
        else if (v.getId() == R.id.buttonMinusWine)
        {
            onIncrementButtonPress(false, WineCount);
        }
        else if (v.getId() == R.id.buttonCalculate)
        {
            if (WeightEntry.getText().toString().equals("") || WeightEntry.getText().toString().equals("0"))
            {
                Toast.makeText(this, "Please enter a weight", Toast.LENGTH_SHORT).show();
            }
            else
            {
                double BAC = calculateBAC(Integer.parseInt(WeightEntry.getText().toString()), isFemale, Integer.parseInt(BeerCount.getText().toString()), Integer.parseInt(WineCount.getText().toString()), Integer.parseInt(LiquorCount.getText().toString()), Integer.parseInt(MaltCount.getText().toString()));

                BAC_message.setText(String.format("%s %.3f%%", getString(R.string.BAC_level), BAC));

                if (BAC >= 0 && BAC <= 0.08 )
                {
                    message.setBackgroundColor(Color.rgb(34, 139, 34));
                    message.setText(R.string.safe);
                }
                else if (BAC <= 0.2)
                {
                    message.setBackgroundColor(Color.rgb(255, 166, 0));
                    message.setText(R.string.careful);
                }
                else
                {
                    message.setBackgroundColor(Color.RED);
                    message.setText(R.string.over);
                }
            }
        }
        else if (v.getId() == R.id.buttonReset)
        {
            BeerCount.setText("0");
            WineCount.setText("0");
            MaltCount.setText("0");
            MaltCount.setText("0");

            WeightEntry.setText("");

            BAC_message.setText(R.string.BAC_level);

            gender.check(R.id.radioButtonFemale);

            message.setBackgroundColor(Color.rgb(34, 139, 34));
            message.setText(R.string.safe);

        }
    }


    private void onIncrementButtonPress(boolean isPlus, TextView count)
    {
        if (isPlus)
        {
            if (Integer.parseInt(count.getText().toString()) == 10)
            {
                return;
            }
            count.setText(String.format("%d", Integer.parseInt(count.getText().toString()) + 1));
            return;
        }
        else
        {
            if (Integer.parseInt(count.getText().toString()) == 0)
            {
                return;
            }
            count.setText(String.format("%d", Integer.parseInt(count.getText().toString()) - 1));
            return;
        }
    }

    private double calculateBAC(int weight, boolean isFemale, int beerNumber, int wineNumber, int liquorNumber, int maltNumber)
    {
        double r;

        if (isFemale) { r = 0.66; }
        else { r = 0.73; }

        Log.d("CALCULATION",String.format("R = %.3f", r));

        double Abeer = (double) ((12 * beerNumber) * 5) / 100;
        double Awine = (double) ((5 * wineNumber) * 12) / 100;
        double Aliq = (double) ((1.5 * liquorNumber) * 40) / 100;
        double Amalt = (double) ((9 * maltNumber) * 7) / 100;

        Log.d("CALCULATION",String.format("Abeer: %.3f, Awine: %.3f, Aliq: %.3f, Amalt: %.3f", Abeer, Awine, Aliq, Amalt));

        double A = Abeer + Awine + Aliq + Amalt;

        Log.d("CALCULATION",String.format("A = %.3f", A));

        return (A * 5.14) / (weight * r);
    }
}