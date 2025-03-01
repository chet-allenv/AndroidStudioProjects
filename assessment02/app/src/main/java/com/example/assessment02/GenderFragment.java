package com.example.assessment02;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assessment02.databinding.FragmentCalorieCalculator2Binding;
import com.example.assessment02.databinding.FragmentGenderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_CALORIE_CALCULATOR = "calorieCalculator";

    private CalorieCalculator calorieCalculator;

    private FragmentGenderBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenderFragment newInstance(CalorieCalculator calorieCalculator) {
        GenderFragment fragment = new GenderFragment();
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
        binding = FragmentGenderBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        GListener = (GenderListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateInfo();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.RadioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                    calorieCalculator.setGender("Male");
                } else {
                    calorieCalculator.setGender("Female");
                }
                GListener.onSubmitSelect(calorieCalculator);
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GListener.onCancel();
            }
        });
    }

    GenderListener GListener;

    public interface GenderListener {
        void onCancel();
        void onSubmitSelect(CalorieCalculator calorieCalculator);
    }

    private void updateInfo()
    {
        binding.textViewTitle.setText(R.string.select_gender_title);

        String gender = calorieCalculator.getGender();

        if (gender == null || gender.equals("Male")) { binding.RadioGroupGender.check(R.id.radioButtonMale); }
        else { binding.RadioGroupGender.check(R.id.radioButtonFemale); }
    }


}