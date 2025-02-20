package edu.uncc.assignment06;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TasksFragment.TasksListener, CreateTaskFragment.CreateTaskListener, SelectTaskDateFragment.SelectTaskDateListener {
    ArrayList<Task> tasks = new ArrayList<>();

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainerView, new TasksFragment(), "Task")
                .addToBackStack(null)
                .commit();
    }

    // Tasks Fragment
    @Override
    public void onCreateTaskClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, new CreateTaskFragment(), "CreateTask")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteTaskClicked() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, new TasksFragment(), "Task")
                .addToBackStack(null)
                .commit();
    }

    // Create Tasks Fragment
    @Override
    public void onCancelPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSubmitPressed(Task task) {

        tasks.add(task);

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSetDatePressed(Task task) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, SelectTaskDateFragment.newInstance(task), "DateFragment")
                .addToBackStack(null)
                .commit();
    }

    // Set Task Date Fragment

    @Override
    public void onDateCancelPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDateSubmitPressed(Task task) {
        CreateTaskFragment ctf = (CreateTaskFragment) getSupportFragmentManager().findFragmentByTag("CreateTask");

        Bundle args = new Bundle();
        args.putSerializable(CreateTaskFragment.ARG_PARAM_TASK, task);

        if (ctf != null)
        {
            getSupportFragmentManager().popBackStack();
        }
    }
}