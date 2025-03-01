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

import com.example.assessment02.databinding.FragmentAgeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgeFragment extends Fragment {

   private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentAgeBinding binding;

    public AgeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgeFragment newInstance(CalorieCalculator calorieCalculator) {
        AgeFragment fragment = new AgeFragment();
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
        binding = FragmentAgeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateInfo();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.editTextNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                } else {
                    calorieCalculator.setAge(Integer.parseInt(binding.editTextNumber.getText().toString()));
                    AListener.onSubmitSelect(calorieCalculator);
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AListener.onCancel();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AListener = (AgeListener) context;
    }

    AgeListener AListener;

    public interface AgeListener {
        void onCancel();
        void onSubmitSelect(CalorieCalculator calorieCalculator);
    }

    private void updateInfo() {
        binding.textViewTitle.setText(R.string.select_age_title);

        Integer age = calorieCalculator.getAge();

        if (age != null) {
            binding.editTextNumber.setText(Integer.toString(age));
        }
    }
}