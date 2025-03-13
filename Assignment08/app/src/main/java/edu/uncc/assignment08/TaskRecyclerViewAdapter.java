package edu.uncc.assignment08;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.uncc.assignment08.models.Task;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> {

    ArrayList<Task> mTasks = new ArrayList<Task>();

    public TaskRecyclerViewAdapter(ArrayList<Task> tasks) {
        this.mTasks = tasks;
    }

    @NonNull
    @Override
    public TaskRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_row, parent, false);

        return new TaskRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.position = holder.getAdapterPosition();
        holder.mTasks = mTasks;
        holder.adapter = this;
        holder.mListener = (TaskRecyclerViewListener) holder.itemView.getContext();

        String priorityString;

        if(task.getPriority() == 1) {
            priorityString = "Low";
        }
        else if(task.getPriority() == 2) {
            priorityString = "Medium";
        }
        else {
            priorityString = "High";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);

        holder.taskTitle.setText(task.getName());
        holder.taskDate.setText(sdf.format(task.getDate()));
        holder.taskPriority.setText(priorityString);

    }

    @Override
    public int getItemCount() {
        return this.mTasks.size();
    }

    public static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView taskTitle;
        TextView taskDate;
        TextView taskPriority;
        ImageView deleteButton;

        int position;
        ArrayList<Task> mTasks = new ArrayList<Task>();
        TaskRecyclerViewAdapter adapter;

        TaskRecyclerViewListener mListener;

        public TaskRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDate = itemView.findViewById(R.id.taskDate);
            taskPriority = itemView.findViewById(R.id.taskPriority);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.gotoTaskDetails(mTasks.get(position));
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mTasks.remove(position);
                    adapter.notifyItemRemoved(position);
                    mListener.removeTask(position);
                }
            });
        }
    }

    public interface TaskRecyclerViewListener {
            void removeTask(int position);
            void gotoTaskDetails(Task task);
    }
}
