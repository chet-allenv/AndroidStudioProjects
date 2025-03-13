package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assignment08.databinding.FragmentTasksBinding;
import edu.uncc.assignment08.models.SortSelection;
import edu.uncc.assignment08.models.Task;

public class TasksFragment extends Fragment {
    public TasksFragment() {
        // Required empty public constructor
    }

    FragmentTasksBinding binding;
    SortSelection sortSelection;

    LinearLayoutManager linearLayoutManager;
    TaskRecyclerViewAdapter adapter;

    public void setSortSelection(SortSelection sortSelection) {
        this.sortSelection = sortSelection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ArrayList<Task> mTasks = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");
        mTasks = mListener.getTasks();

        binding.recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TaskRecyclerViewAdapter(mTasks);

        binding.recyclerView.setAdapter(adapter);

        if (sortSelection != null) {
            if (sortSelection.getSortAttribute().equals("date") && sortSelection.getSortOrder().equals("ASC")) {
                mTasks.sort(Comparator.comparing(Task::getDate));
            }
            else if (sortSelection.getSortAttribute().equals("date") && sortSelection.getSortOrder().equals("DESC")) {
                mTasks.sort(Comparator.comparing(Task::getDate).reversed());
            }
            else if (sortSelection.getSortAttribute().equals("name") && sortSelection.getSortOrder().equals("ASC")) {
                mTasks.sort(Comparator.comparing(Task::getName));
            }
            else if (sortSelection.getSortAttribute().equals("name") && sortSelection.getSortOrder().equals("DESC")) {
                mTasks.sort(Comparator.comparing(Task::getName).reversed());
            }
            else if (sortSelection.getSortAttribute().equals("priority") && sortSelection.getSortOrder().equals("ASC")) {
                mTasks.sort(Comparator.comparing(Task::getPriority));
            }

            else if (sortSelection.getSortAttribute().equals("priority") && sortSelection.getSortOrder().equals("DESC")) {
                mTasks.sort(Comparator.comparing(Task::getPriority).reversed());
            }
        }

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clearAllTasks();
                mTasks.clear();
                adapter.notifyDataSetChanged();
            }
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreateTask();
            }
        });

        binding.buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

    }

    TasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TasksListener) {
            mListener = (TasksListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TasksListener");
        }
    }

    interface TasksListener{
        void gotoCreateTask();
        void gotoSelectSort();
        void clearAllTasks();
        void gotoTaskDetails(Task task);
        ArrayList<Task> getTasks();
    }
}