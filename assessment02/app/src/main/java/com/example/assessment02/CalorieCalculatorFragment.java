package com.example.assessment02;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.assessment02.databinding.FragmentCalorieCalculator2Binding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalorieCalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalorieCalculatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private FragmentCalorieCalculator2Binding binding;

    // TODO: Rename and change types of parameters

    private CalorieCalculator calorieCalculator;

    public CalorieCalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalorieCalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalorieCalculatorFragment newInstance(CalorieCalculator calorieCalculator) {
        CalorieCalculatorFragment fragment = new CalorieCalculatorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CALORIE_CALCULATOR, calorieCalculator);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            calorieCalculator = (CalorieCalculator) getArguments().getSerializable(ARG_CALORIE_CALCULATOR);
        }

        else {
            calorieCalculator = new CalorieCalculator();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCalorieCalculator2Binding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateValues();

        binding.buttonSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCListener.onGenderSelect(calorieCalculator);
            }
        });

        binding.buttonSelectWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCListener.onWeightSelect(calorieCalculator);
            }
        });

        binding.buttonSelectHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCListener.onHeightSelect(calorieCalculator);
            }
        });

        binding.buttonSelectAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCListener.onAgeSelect(calorieCalculator);
            }
        });

        binding.buttonSelectActivityLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CCListener.onActivityLevelSelect(calorieCalculator);
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (calorieCalculator != null && calorieCalculator.getGender() != null && calorieCalculator.getWeight() != null && calorieCalculator.getHeightFeet() != null && calorieCalculator.getHeightInches() != null && calorieCalculator.getAge() != null && calorieCalculator.getActivityLevel() != null)
                {
                    CCListener.onSubmit(calorieCalculator);
                } else {
                    Toast.makeText(getContext(), "Please select all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateValues()
    {
        if (calorieCalculator == null) {
            return;
        }

        binding.textViewLabelGender.setText(String.format("%s %s", getString(R.string.label_gender), calorieCalculator.getGender() != null ? calorieCalculator.getGender() : "N/A"));
        binding.textViewLabelWeight.setText(String.format("%s %s", getString(R.string.label_weight), calorieCalculator.getWeight() != null ? calorieCalculator.getWeight() + " lbs" : "N/A"));
        binding.textViewLabelHeight.setText(String.format("%s %s", getString(R.string.label_height), calorieCalculator.getHeightString() != null  && calorieCalculator.getHeightInches() != null ? calorieCalculator.getHeightString() : "N/A"));
        binding.textViewLabelAge.setText(String.format("%s %s", getString(R.string.label_age), calorieCalculator.getAge() != null ? calorieCalculator.getAge() : "N/A"));
        binding.textViewLabelActivityLevel.setText(String.format("%s %s", getString(R.string.label_activity_level), calorieCalculator.getActivityLevelString() != null ? calorieCalculator.getActivityLevelString() : "N/A"));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        CCListener = (CalorieCalculatorListener) context;
    }
    CalorieCalculatorListener CCListener;

    public interface CalorieCalculatorListener {
        void onGenderSelect(CalorieCalculator calorieCalculator);
        void onWeightSelect(CalorieCalculator calorieCalculator);
        void onHeightSelect(CalorieCalculator calorieCalculator);
        void onAgeSelect(CalorieCalculator calorieCalculator);
        void onActivityLevelSelect(CalorieCalculator calorieCalculator);

        void onSubmit(CalorieCalculator calorieCalculator);

    }

}