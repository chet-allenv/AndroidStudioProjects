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

import com.example.assessment02.databinding.FragmentGenderBinding;
import com.example.assessment02.databinding.FragmentHeightBinding;
import com.example.assessment02.databinding.FragmentWeightBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeightFragment extends Fragment {

    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentHeightBinding binding;

    public HeightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeightFragment newInstance(CalorieCalculator calorieCalculator) {
        HeightFragment fragment = new HeightFragment();
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
        binding = FragmentHeightBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateInfo();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.editTextNumberFeet.getText().toString().isEmpty() || binding.editTextNumberInches.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    Integer feet = Integer.parseInt(binding.editTextNumberFeet.getText().toString());
                    Integer inches = Integer.parseInt(binding.editTextNumberInches.getText().toString());

                    if (inches > 11) {
                        Toast.makeText(getContext(), "Please enter a valid inch height", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    calorieCalculator.setHeightFeet(feet);
                    calorieCalculator.setHeightInches(inches);
                    HListener.onSubmitSelect(calorieCalculator);
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HListener.onCancel();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        HListener = (HeightListener) context;
    }

    HeightListener HListener;
    public interface HeightListener {
        void onCancel();
        void onSubmitSelect(CalorieCalculator calorieCalculator);
    }

    private void updateInfo()
    {
        binding.textViewTitle.setText(R.string.select_height_title);

        Integer heightFeet = calorieCalculator.getHeightFeet();
        Integer heightInches = calorieCalculator.getHeightInches();

        if (heightFeet != null && heightInches != null) {
            binding.editTextNumberFeet.setText(heightFeet.toString());
            binding.editTextNumberInches.setText(heightInches.toString());
        }
    }

}