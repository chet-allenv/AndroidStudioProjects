package edu.uncc.assessment03.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    ArrayList<User> mUsers;

    public UserAdapter(ArrayList<User> mUsers) {
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_item, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.position = holder.getAdapterPosition();
        holder.mUsers = mUsers;
        holder.adapter = this;
        holder.mListener = (UserListener) holder.itemView.getContext();


        int score = user.getCreditScore();
        int imageResource = getImageResource(score);

        holder.scoreDial.setImageResource(imageResource);
        holder.userScore.setText(String.valueOf(score));
        holder.userName.setText(user.getName());
        holder.userAge.setText(String.format("%d years old", user.getAge()));
        holder.userState.setText(String.format("%s (%s)", user.getState().getName(), user.getState().getAbbreviation()));

    }

    private static int getImageResource(int score) {
        int imageResource;

        if(score >= 300 && score <= 579) {
            imageResource = R.drawable.poor;
        }
        else if(score >= 580 && score <= 669) {
            imageResource = R.drawable.fair;
        }
        else if(score >= 670 && score <= 739) {
            imageResource = R.drawable.good;
        }
        else if (score >= 740 && score <= 799) {
            imageResource = R.drawable.very_good;
        }
        else {
            imageResource = R.drawable.excellent;
        }
        return imageResource;
    }

    @Override
    public int getItemCount() {
        return this.mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        int position;
        TextView userName;
        TextView userAge;
        TextView userState;
        TextView userScore;
        ImageView scoreDial;
        ImageView deleteButton;

        ArrayList<User> mUsers;

        UserAdapter adapter;
        UserAdapter.UserListener mListener;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            userState = itemView.findViewById(R.id.userState);
            userScore = itemView.findViewById(R.id.userScore);
            scoreDial = itemView.findViewById(R.id.scoreDial);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.notifyItemRemoved(position);
                    mListener.onUserDeleted(mUsers.get(position));
                }
            });
        }
    }

    public interface UserListener {
        void onUserDeleted(User user);
    }
}
