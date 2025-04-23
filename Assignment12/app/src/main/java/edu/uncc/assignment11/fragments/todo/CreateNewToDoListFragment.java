package edu.uncc.assignment11.fragments.todo;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assignment11.databinding.FragmentCreateNewToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class CreateNewToDoListFragment extends Fragment {

    private static final String TAG = "CreateNewToDoListFragment";
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    FragmentCreateNewToDoListBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateNewToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New List");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelCreateNewToDoList();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = binding.editTextName.getText().toString().trim();
                if (listName.isEmpty()) {
                    Toast.makeText(getContext(), "List name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Create new todo list using the api
                    db.collection("users").document(mCurrentUser.getUid())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            HashMap<String, ArrayList<HashMap<String, String>>> toDoLists = (HashMap<String, ArrayList<HashMap<String, String>>>) documentSnapshot.get("tasks");
                                            ToDoList toDoList = new ToDoList(listName);
                                            toDoLists.put(toDoList.getName(), new ArrayList<>());

                                            db.collection("users").document(mCurrentUser.getUid())
                                                    .update("tasks", toDoLists).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            mListener.onSuccessCreateNewToDoList();
                                                        }
                                                    });
                                        } else {
                                            Log.d(TAG, "onComplete: document does not exist");
                                        }
                                    } else {
                                        Log.d(TAG, "onComplete: failed to get todo lists");
                                    }
                                }
                            });



                }
            }
        });
    }

    CreateNewToDoListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewToDoListListener) {
            mListener = (CreateNewToDoListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateNewToDoListListener");
        }
    }

    public interface CreateNewToDoListListener {
        void onSuccessCreateNewToDoList();
        void onCancelCreateNewToDoList();
    }
}