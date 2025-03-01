package com.example.assessment02;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements CalorieCalculatorFragment.CalorieCalculatorListener, GenderFragment.GenderListener, WeightFragment.WeightListener, HeightFragment.HeightListener, AgeFragment.AgeListener, ActivityLevelFragment.ActivityLevelListener, CalorieDetailsFragment.CalorieDetailListener {

    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, CalorieCalculatorFragment.newInstance(new CalorieCalculator()), "calorieCalculatorFragment")
                .addToBackStack(null)
                .commit();


    }


    @Override
    public void onGenderSelect(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, GenderFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWeightSelect(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, WeightFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onHeightSelect(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, HeightFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAgeSelect(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, AgeFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityLevelSelect(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, ActivityLevelFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSubmit(CalorieCalculator calorieCalculator) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView2, CalorieDetailsFragment.newInstance(calorieCalculator))
                .addToBackStack(null)
                .commit();

    }


    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSubmitSelect(CalorieCalculator calorieCalculator) {

        CalorieCalculatorFragment CCF = (CalorieCalculatorFragment) getSupportFragmentManager().findFragmentByTag("calorieCalculatorFragment");
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CALORIE_CALCULATOR ,calorieCalculator);


        if (CCF != null) {

            CCF.setArguments(bundle);

            getSupportFragmentManager().popBackStack();
        }
    }
}