package edu.uncc.assignment11.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assignment11.databinding.FragmentSignupBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private static final String TAG = "SignUpFragment";

    public SignUpFragment() {
        // Required empty public constructor
    }

    FragmentSignupBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Sign Up");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLogin();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = binding.editTextFirstName.getText().toString().trim();
                String lname = binding.editTextLastName.getText().toString().trim();
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();

                if (fname.isEmpty()) {
                    Toast.makeText(getContext(), "First Name is required", Toast.LENGTH_SHORT).show();
                } else if (lname.isEmpty()) {
                    Toast.makeText(getContext(), "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("firstName", fname);
                                        user.put("lastName", lname);
                                        user.put("email", email);
                                        user.put("tasks", new HashMap<ToDoList, ArrayList<ToDoListItem>>());

                                        db.collection("users")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "onSuccess: user profile is created for " + mAuth.getCurrentUser().getUid());
                                                    }
                                                });

                                        mListener.onSignUpSuccessful();
                                    } else {
                                        Toast.makeText(getContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    SignUpListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SignUpListener) {
            mListener = (SignUpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SignUpListener");
        }
    }

    public interface SignUpListener {
        void gotoLogin();
        void onSignUpSuccessful();
    }
}