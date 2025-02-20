package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;

import edu.uncc.assignment06.databinding.FragmentTasksBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    ArrayList<Task> tasks;

    int currentIndex = 0;


    public TasksFragment() {
        // Required empty public constructor
    }
    public static TasksFragment newInstance() {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity ma = (MainActivity) getActivity();

        assert ma != null;
        tasks = ma.getTasks();
    }

    FragmentTasksBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");


        if (tasks.isEmpty())
        {
            binding.cardViewTask.setVisibility(View.INVISIBLE);
        }
        else {
            tasks.sort(Comparator.comparing(Task::getDate));
            updateCardView();
        }


        binding.imageViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0)
                {
                    currentIndex--;
                    updateCardView();
                }
            }
        });

        binding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < tasks.size() - 1)
                {
                    currentIndex++;
                    updateCardView();
                }
            }
        });

        binding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tasks.remove(currentIndex);

                TListener.onDeleteTaskClicked();
            }
        });

        binding.buttonCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TListener.onCreateTaskClicked();
            }
        });


    }

    private void updateCardView()
    {
        if (tasks.isEmpty()) {
            binding.textViewTasksCount.setText("You have 0 tasks");
            binding.textViewTaskName.setText("");
            binding.textViewTaskDate.setText("");
            binding.textViewTaskPriority.setText("");
            binding.textViewTaskOutOf.setText("");
            binding.cardViewTask.setVisibility(View.INVISIBLE);
            return;
        }

        binding.textViewTasksCount.setText("You have " + tasks.size() + " tasks");
        binding.textViewTaskName.setText(tasks.get(currentIndex).getName());
        binding.textViewTaskDate.setText(tasks.get(currentIndex).getDate());
        binding.textViewTaskPriority.setText(tasks.get(currentIndex).getPriority().toString());
        binding.textViewTaskOutOf.setText("Task " + (currentIndex + 1) + " of " + tasks.size());
    }


    TasksListener TListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        TListener = (TasksListener) context;
    }

    public interface TasksListener{
        void onCreateTaskClicked();
        void onDeleteTaskClicked();
    }
}