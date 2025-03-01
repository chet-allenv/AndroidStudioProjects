package com.example.assessment02;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment02.databinding.FragmentActivityLevelBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityLevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityLevelFragment extends Fragment {

    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentActivityLevelBinding binding;

    public ActivityLevelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityLevelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityLevelFragment newInstance(CalorieCalculator calorieCalculator) {
        ActivityLevelFragment fragment = new ActivityLevelFragment();
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
        binding = FragmentActivityLevelBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ALListener = (ActivityLevelListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ALListener.onCancel();
            }
        });

        binding.buttonSedentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calorieCalculator.setActivityLevel(0);
                ALListener.onSubmitSelect(calorieCalculator);
            }

        });
        binding.buttonCancelLightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieCalculator.setActivityLevel(1);
                ALListener.onSubmitSelect(calorieCalculator);
            }
        });

        binding.buttonCancelModeratelyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieCalculator.setActivityLevel(2);
                ALListener.onSubmitSelect(calorieCalculator);
            }
        });

        binding.buttonCancelVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieCalculator.setActivityLevel(3);
                ALListener.onSubmitSelect(calorieCalculator);
            }
        });

        binding.buttonCancelSuperActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieCalculator.setActivityLevel(4);
                ALListener.onSubmitSelect(calorieCalculator);
            }
        });
    }

    ActivityLevelListener ALListener;
    public interface ActivityLevelListener
    {
        void onCancel();
        void onSubmitSelect(CalorieCalculator calorieCalculator);
    }

}