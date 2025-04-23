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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentAddItemToToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class AddItemToToDoListFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST = "ARG_PARAM_TODO_LIST";
    private ToDoList mTodoList;

    private static final String TAG = "AddItemToToDoListFragment";
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    public AddItemToToDoListFragment() {
        // Required empty public constructor
    }

    public static AddItemToToDoListFragment newInstance(ToDoList toDoList) {
        AddItemToToDoListFragment fragment = new AddItemToToDoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTodoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    FragmentAddItemToToDoListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddItemToToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Item to List");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelAddItemToList(mTodoList);
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = binding.editTextName.getText().toString().trim();

                if (itemName.isEmpty()) {
                    Toast.makeText(getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String priority = "Low";
                    int checkedId = binding.radioGroup.getCheckedRadioButtonId();
                    if(checkedId == R.id.radioButtonMedium){
                        priority = "Medium";
                    } else if(checkedId == R.id.radioButtonHigh){
                        priority = "High";
                    }
                    //TODO: Add new todo list item to the list using the api

                    String finalPriority = priority;

                    db.collection("users").document(mCurrentUser.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            HashMap<String, ArrayList<ToDoListItem>> toDoLists = (HashMap<String, ArrayList<ToDoListItem>>) documentSnapshot.get("tasks");
                                            mTodoList = new ToDoList(mTodoList.getName());

                                            ToDoListItem toDoListItem = new ToDoListItem(itemName, finalPriority);
                                            toDoLists.get(mTodoList.getName()).add(toDoListItem);

                                            db.collection("users").document(mCurrentUser.getUid())
                                                    .update("tasks", toDoLists).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            mListener.onSuccessAddItemToList();
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

    AddItemToListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemToListListener) {
            mListener = (AddItemToListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddItemToListListener");
        }
    }

    public interface AddItemToListListener{
        void onSuccessAddItemToList();
        void onCancelAddItemToList(ToDoList todoList);
    }
}