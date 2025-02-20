package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.uncc.assignment06.databinding.FragmentCreateTaskBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTaskFragment extends Fragment {

    public static final String ARG_PARAM_TASK = "ARG_PARAM_TASK";

    Task currentTask;

    ArrayList<Task> tasks;
    Priority priority = Priority.High;

    String date;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance(Task task) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();


        args.putSerializable(ARG_PARAM_TASK, task);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){

            currentTask = (Task) getArguments().getSerializable(ARG_PARAM_TASK);
        }
    }

    FragmentCreateTaskBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Task");

        MainActivity ma = (MainActivity) getActivity();

        assert ma != null;
        tasks = ma.getTasks();

        if (currentTask != null)
        {
            updateText();
        }

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CTListener.onCancelPressed();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task newTask = generateTask();

                if (newTask == null)
                {
                    return;
                }

                CTListener.onSubmitPressed(newTask);
            }
        });

        binding.buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentTask = new Task(binding.editTextTaskName.getEditableText().toString()
                        , binding.textViewDate.getText().toString()
                        , priority);

                CTListener.onSetDatePressed(currentTask);
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.radioButtonHigh.getId())
                {
                    priority = Priority.High;
                }
                else if (checkedId == binding.radioButtonMedium.getId())
                {
                    priority = Priority.Medium;
                }
                else if (checkedId == binding.radioButtonLow.getId())
                {
                    priority = Priority.Low;
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        CTListener = (CreateTaskListener) context;
    }

    private Task generateTask()
    {
        if (binding.editTextTaskName.getEditableText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Please enter a name for the task", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (binding.textViewDate.getText().toString().isEmpty() || binding.textViewDate.getText().toString().equalsIgnoreCase("n/a"))
        {
            Toast.makeText(getContext(), "Please enter a date", Toast.LENGTH_SHORT).show();
            return null;
        }

        var list = new ArrayList<Object>();

        list.add(binding.editTextTaskName.getEditableText().toString());
        list.add(binding.textViewDate.getText().toString());
        list.add(priority);

        return new Task((String) list.get(0), (String) list.get(1), (Priority) list.get(2));
    }

    private void updateText() {
        Log.d("CreateTaskFragment", "updateText: " + currentTask.getDate());

        binding.editTextTaskName.setText(currentTask.getName());

        //binding.textViewDate.setText(!Objects.equals(currentTask.getDate(), "N/A") ? currentTask.getDate() : "N/A");

        binding.textViewDate.setText(currentTask.getDate());

        Log.d("CreateTaskFragment", "updateText: " + currentTask.getDate());

        int checkedID = binding.radioButtonHigh.getId();

        if (currentTask.priority == null || currentTask.priority == Priority.High) {
            checkedID = binding.radioButtonHigh.getId();
        }
        else if (currentTask.priority == Priority.Medium)
        {
            checkedID = binding.radioButtonMedium.getId();
        }
        else if (currentTask.priority == Priority.Low)
        {
            checkedID = binding.radioButtonLow.getId();
        }


        binding.radioGroup.check(checkedID);
    }


    CreateTaskListener CTListener;

    public interface CreateTaskListener{
        void onCancelPressed();
        void onSubmitPressed(Task task);
        void onSetDatePressed(Task task);
    }
}