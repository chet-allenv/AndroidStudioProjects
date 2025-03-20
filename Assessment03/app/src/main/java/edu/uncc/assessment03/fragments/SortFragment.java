package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.databinding.FragmentSortBinding;

public class SortFragment extends Fragment {
    public SortFragment() {
        // Required empty public constructor
    }

    String[] mSorts = {"Name", "Age", "Credit Score", "State"};

    FragmentSortBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSortBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Sort");

        binding.listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mSorts));

        binding.listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            mListener.onSortSelected(mSorts[i]);
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAddOrSelection();
            }
        });
    }

    SortListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SortListener) {
            mListener = (SortListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SortListener");
        }
    }

    public interface SortListener {
        void onSortSelected(String sort);
        void cancelAddOrSelection();
    }
}