package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;

import edu.uncc.assignment08.databinding.FragmentTaskDetailsBinding;
import edu.uncc.assignment08.models.Task;

public class TaskDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TASK = "ARG_PARAM_TASK";
    private Task mTask;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(Task task) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTask = (Task)getArguments().getSerializable(ARG_PARAM_TASK);
        }
    }

    FragmentTaskDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Task Details");

        String priorityString;

        if(mTask.getPriority() == 1) {
            priorityString = "Low";
        }
        else if(mTask.getPriority() == 2) {
            priorityString = "Medium";
        }
        else {
            priorityString = "High";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);

        binding.nameData.setText(mTask.getName());
        binding.dateData.setText(sdf.format(mTask.getDate()));
        binding.priorityData.setText(priorityString);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoBack();
            }
        });

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.deleteTask(mTask);
            }
        });

    }

    DetailsLisener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DetailsLisener) {
            mListener = (DetailsLisener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DetailsLisener");
        }
    }

    public interface DetailsLisener {
        void gotoBack();
        void deleteTask(Task task);
    }
}