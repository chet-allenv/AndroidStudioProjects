package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentToDoListsBinding;
import edu.uncc.assignment11.databinding.ListItemTodoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class ToDoListsFragment extends Fragment {
    public ToDoListsFragment() {
        // Required empty public constructor
    }

    FragmentToDoListsBinding binding;
    ArrayList<ToDoList> mToDoLists = new ArrayList<>();
    ToDoListAdapter adapter;

    private static final String TAG = "ToDoListsFragment";
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_lists_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_todo_list_action){
                    mListener.gotoCreateNewToDoList();
                    return true;
                } else if(menuItem.getItemId() == R.id.logout_action){
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        adapter = new ToDoListAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        getAllToDoListsForUser();
    }


    private void getAllToDoListsForUser() {
        //TODO: reload the todo lists for the currently logged in user
        db.collection("users").document(mCurrentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                HashMap<String, ArrayList<HashMap<String, String>>> toDoLists = (HashMap<String, ArrayList<HashMap<String, String>>>) documentSnapshot.get("tasks");
                                mToDoLists.clear();

                                for (String key : toDoLists.keySet()) {
                                    ToDoList toDoListTemp = new ToDoList(key);
                                    mToDoLists.add(toDoListTemp);
                                }

                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Log.d(TAG, "onComplete: document does not exist");
                            }
                        } else {
                            Log.d(TAG, "onComplete: failed to get todo lists");
                        }
                    }
                });


    }

    private void deleteToDoList(ToDoList toDoList) {
        //TODO: delete the todo list using the api
        //TODO: reload the todo lists for the currently logged in user
        db.collection("users").document(mCurrentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                HashMap<String, ArrayList<HashMap<String, String>>> toDoLists = (HashMap<String, ArrayList<HashMap<String, String>>>) documentSnapshot.get("tasks");
                                toDoLists.remove(toDoList.getName());
                                mToDoLists.clear();

                                for (String key : toDoLists.keySet()) {
                                    ToDoList toDoListTemp = new ToDoList(key);
                                    mToDoLists.add(toDoListTemp);
                                }

                                db.collection("users").document(mCurrentUser.getUid())
                                        .update("tasks", toDoLists);

                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Log.d(TAG, "onComplete: document does not exist");
                            }
                        } else {
                            Log.d(TAG, "onComplete: failed to get todo lists");
                        }
                    }
                });

    }


    class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>{

        @NonNull
        @Override
        public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemTodoListBinding itemBinding = ListItemTodoListBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
            ToDoList toDoList = mToDoLists.get(position);
            holder.bind(toDoList);
        }

        @Override
        public int getItemCount() {
            return mToDoLists.size();
        }

        class ToDoListViewHolder extends RecyclerView.ViewHolder{
            ListItemTodoListBinding itemBinding;
            ToDoList mToDoList;
            public ToDoListViewHolder(ListItemTodoListBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoList toDoList) {
                mToDoList = toDoList;
                itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.gotoToDoListDetails(toDoList);
                    }
                });

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoList(mToDoList);
                    }
                });

                itemBinding.textViewName.setText(toDoList.getName());
            }
        }
    }

    ToDoListsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListsListener) {
            mListener = (ToDoListsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListsListener");
        }
    }

    public interface ToDoListsListener {
        void gotoCreateNewToDoList();
        void gotoToDoListDetails(ToDoList toDoList);
        void logout();
    }
}