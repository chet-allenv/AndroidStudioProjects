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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment {

    EditText nameEntry;
    EditText emailEntry;

    RadioGroup roles;

    Role role;

    User user;

    public CreateUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_user, container, false);


    }
    CreateUserListener cuListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        role = Role.Student;

        roles = view.findViewById(R.id.RadioGroupRole);

        nameEntry = view.findViewById(R.id.editTextName);
        emailEntry = view.findViewById(R.id.editTextTextEmailAddress);

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

        view.findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Object> dataList = gatherData();

                if (dataList == null)
                {
                    return;
                }

                user = generateUser(dataList);

                cuListener.nextButtonPressed(user);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        cuListener = (CreateUserListener) context;
    }

    public interface CreateUserListener {
        void nextButtonPressed(User user);
    }

    private ArrayList<Object> gatherData()
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

}