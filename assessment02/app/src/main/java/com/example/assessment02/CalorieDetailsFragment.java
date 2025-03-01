package com.example.assessment02;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment02.databinding.FragmentCalorieDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalorieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalorieDetailsFragment extends Fragment {

    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentCalorieDetailsBinding binding;

    public CalorieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CalorieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalorieDetailsFragment newInstance(CalorieCalculator calorieCalculator) {
        CalorieDetailsFragment fragment = new CalorieDetailsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalorieDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateValues();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CDListener.onCancel();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        CDListener = (CalorieDetailListener) context;
    }

    private void updateValues()
    {
        binding.textViewTitle.setText(R.string.calorie_detail_title);

        if (calorieCalculator == null) {
            return;
        }

        binding.textViewLabelGender.setText(String.format("%s %s", getString(R.string.label_gender), calorieCalculator.getGender() != null ? calorieCalculator.getGender() : "N/A"));
        binding.textViewLabelWeight.setText(String.format("%s %s", getString(R.string.label_weight), calorieCalculator.getWeight() != null ? calorieCalculator.getWeight() + " lbs" : "N/A"));
        binding.textViewLabelHeight.setText(String.format("%s %s", getString(R.string.label_height), calorieCalculator.getHeightString() != null  && calorieCalculator.getHeightInches() != null ? calorieCalculator.getHeightString() : "N/A"));
        binding.textViewLabelAge.setText(String.format("%s %s", getString(R.string.label_age), calorieCalculator.getAge() != null ? calorieCalculator.getAge() : "N/A"));
        binding.textViewLabelActivityLevel.setText(String.format("%s %s", getString(R.string.label_activity_level), calorieCalculator.getActivityLevelString() != null ? calorieCalculator.getActivityLevelString() : "N/A"));
        binding.textViewBMR.setText(String.format("%s %.2f", getString(R.string.BMR_label), BMR_Calc()));
        binding.textViewTDEE.setText(String.format("%s %.2f", getString(R.string.TDEE_label), TDEE_Calc()));
    }
    CalorieDetailListener CDListener;
    public interface CalorieDetailListener {
        void onCancel();
    }

    private float BMR_Calc()
    {
        if (calorieCalculator.getGender().equals("Male"))
        {
            return (float) ((10 * calorieCalculator.getWeight() / 2.205) + (15.875 * ((12 * calorieCalculator.getHeightFeet()) + calorieCalculator.getHeightInches())) - (5 * calorieCalculator.getAge()) + 5);
        } else {
            return (float) ((10 * calorieCalculator.getWeight() / 2.205) + (15.875 * ((12 * calorieCalculator.getHeightFeet()) + calorieCalculator.getHeightInches())) - (5 * calorieCalculator.getAge()) - 161);
        }
    }

    private float TDEE_Calc()
    {
        int activityLevel = calorieCalculator.getActivityLevel();
        float BMR = BMR_Calc();

        switch (activityLevel){
            case 0:
                return BMR * 1.2f;
            case 1:
                return BMR * 1.375f;
            case 2:
                return BMR * 1.55f;
            case 3:
                return BMR * 1.725f;
            case 4:
                return BMR * 1.9f;
            default:
                return BMR;
        }
    }

}