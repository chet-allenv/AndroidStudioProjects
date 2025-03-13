package edu.uncc.assignment08;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Date;

import edu.uncc.assignment08.models.SortSelection;
import edu.uncc.assignment08.models.Task;

public class MainActivity extends AppCompatActivity implements TasksFragment.TasksListener,
        CreateTaskFragment.CreateTaskListener, SelectTaskDateFragment.SelectTaskDateListener, SortFragment.SortListener, TaskRecyclerViewAdapter.TaskRecyclerViewListener,
        TaskDetailsFragment.DetailsLisener {

    ArrayList<Task> mTasks = new ArrayList<>();

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new TasksFragment(), "tasks-fragment")
                .commit();
    }

    @Override
    public void gotoCreateTask() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CreateTaskFragment(), "create-task-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectSort() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SortFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void clearAllTasks() {
        mTasks.clear();
    }

    @Override
    public void removeTask(int position) {
        mTasks.remove(position);
    }

    @Override
    public void gotoTaskDetails(Task task) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, TaskDetailsFragment.newInstance(task))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return mTasks;
    }

    @Override
    public void createTask(Task task) {
        mTasks.add(task);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelAndPopBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void sendSortSelection(SortSelection sortSelection) {
        TasksFragment fragment = (TasksFragment) getSupportFragmentManager().findFragmentByTag("tasks-fragment");
        if (fragment != null) {
            fragment.setSortSelection(sortSelection);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void sendSelectedDate(Date date) {
        CreateTaskFragment fragment = (CreateTaskFragment) getSupportFragmentManager().findFragmentByTag("create-task-fragment");
        if (fragment != null) {
            fragment.setSelectedDate(date);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void selectDate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectTaskDateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoBack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void deleteTask(Task task) {
        mTasks.remove(task);
        getSupportFragmentManager().popBackStack();
    }
}