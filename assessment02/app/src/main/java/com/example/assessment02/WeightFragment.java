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
import com.example.assessment02.databinding.FragmentWeightBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentWeightBinding binding;


    public WeightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeightFragment newInstance(CalorieCalculator calorieCalculator) {
        WeightFragment fragment = new WeightFragment();
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
        binding = FragmentWeightBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }



    private void updateInfo() {
        binding.textViewTitle.setText(R.string.select_weight_title);

        Integer weight = calorieCalculator.getWeight();

        if (weight != null) {
            binding.editTextNumber.setText(Integer.toString(weight));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateInfo();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.editTextNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your weight", Toast.LENGTH_SHORT).show();
                } else {
                    calorieCalculator.setWeight(Integer.parseInt(binding.editTextNumber.getText().toString()));
                    WListener.onSubmitSelect(calorieCalculator);
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WListener.onCancel();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        WListener = (WeightListener) context;
    }

    WeightListener WListener;

    public interface WeightListener {
        void onCancel();
        void onSubmitSelect(CalorieCalculator calorieCalculator);
    }
}

