package com.example.assignment05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM_USER = "ARG_PARAM_USER";
    User user;

    TextView nameLabel;
    TextView emailLabel;
    TextView roleLabel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(User user)
    {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        args.putSerializable(ARG_PARAM_USER, user);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            user = (User) getArguments().getSerializable(ARG_PARAM_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.updateButtonPressed();
            }
        });

        nameLabel = view.findViewById(R.id.textViewNameLabel2);
        emailLabel = view.findViewById(R.id.textViewEmailLabel2);
        roleLabel = view.findViewById(R.id.textViewRoleLabel2);

        updateText();
    }

    ProfileListener pListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        pListener = (ProfileListener) context;

    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() { return this.user; }

    public interface ProfileListener{
        public void updateButtonPressed();
    }


    private void updateText()
    {
        nameLabel.setText(String.format("%s %s", getString(R.string.label_name), user.getName()));
        emailLabel.setText(String.format("%s %s", getString(R.string.label_email), user.getEmail()));
        roleLabel.setText(String.format("%s %s", getString(R.string.label_role), user.getRole().name()));
    }
}