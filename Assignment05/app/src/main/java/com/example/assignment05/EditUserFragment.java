package com.example.assignment05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserFragment extends Fragment {

    EditText nameEntry;
    EditText emailEntry;

    RadioGroup roles;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {return this.user;}


    Role role;

    public EditUserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        role = user.getRole();

        nameEntry = view.findViewById(R.id.editTextName2);
        emailEntry = view.findViewById(R.id.editTextTextEmailAddress2);

        roles = view.findViewById(R.id.RadioGroupRole);

        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Object> dataList = gatherData();
                user = generateUser(dataList);

                euListener.onSubmitButtonPressed(user);

            }
        });

        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                euListener.onCancelButtonPressed();
            }
        });

        checkProperBox(role, roles);

        nameEntry.setText(user.getName());
        emailEntry.setText(user.getEmail());

        roles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonStudent)
                {
                    role = Role.Student;
                }
                else if (checkedId == R.id.radioButtonEmployee)
                {
                    role = Role.Employee;
                }
                else if (checkedId == R.id.radioButtonOther)
                {
                    role = Role.Other;
                }
            }
        });
    }

    EditUserListener euListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        euListener = (EditUserListener) context;

    }

    public ArrayList<Object> gatherData()
    {
        var list = new ArrayList<Object>();

        if (nameEntry.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Please Enter A Name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (emailEntry.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailEntry.getText().toString()).matches())
        {
            Toast.makeText(getContext(), "Please Enter An Email", Toast.LENGTH_SHORT).show();
            return null;
        }

        list.add(nameEntry.getText().toString());
        list.add(emailEntry.getText().toString());
        list.add(role);

        return list;
    }

    private User generateUser(ArrayList<Object> list)
    {
        return new User((String) list.get(0), (String) list.get(1), (Role) list.get(2));
    }


    private void checkProperBox(Role r, RadioGroup roles)
    {
        switch (r)
        {
            case Student:
                roles.check(R.id.radioButtonStudent);
                break;
            case Employee:
                roles.check(R.id.radioButtonEmployee);
                break;
            case Other:
                roles.check(R.id.radioButtonOther);
                break;
        }
    }

    public interface EditUserListener{
        void onCancelButtonPressed();
        void onSubmitButtonPressed(User user);
    }
}