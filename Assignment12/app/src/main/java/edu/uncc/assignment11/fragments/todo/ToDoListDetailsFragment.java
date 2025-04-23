package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

    private static final String TAG = "ToDoListDetailsFragment";
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    public static ToDoListDetailsFragment newInstance(ToDoList toDoList) {
        ToDoListDetailsFragment fragment = new ToDoListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    public ToDoListDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToDoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    ArrayList<ToDoListItem> mToDoListItems = new ArrayList<>();
    ToDoListItemAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
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
                menuInflater.inflate(R.menu.todo_list_details_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_list_item_action){
                    mListener.gotoAddListItem(mToDoList);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToToDoLists();
            }
        });

        adapter = new ToDoListItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadToDoListItems();
    }

    void loadToDoListItems(){
        //TODO: Load the items for the to do list using the api
        db.collection("users").document(mCurrentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                HashMap<String, ArrayList<HashMap<String, String>>> toDoLists = (HashMap<String, ArrayList<HashMap<String, String>>>) documentSnapshot.get("tasks");

                                ArrayList<HashMap<String, String>> items = toDoLists.get(mToDoList.getName());

                                mToDoListItems.clear();

                                for (HashMap<String, String> item : items) {
                                    ToDoListItem toDoListItem = new ToDoListItem(item.get("name"), item.get("priority"));
                                    mToDoListItems.add(toDoListItem);
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

    void deleteToDoListItem(ToDoListItem toDoListItem){
        //TODO: Delete the item using the api
        //TODO: Reload the items for the to do list using the api
        db.collection("users").document(mCurrentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                HashMap<String, ArrayList<HashMap<String, String>>> toDoLists = (HashMap<String, ArrayList<HashMap<String, String>>>) documentSnapshot.get("tasks");

                                ArrayList<HashMap<String, String>> items = toDoLists.get(mToDoList.getName());
                                HashMap<String, String> itemToDelete = items.get(mToDoListItems.indexOf(toDoListItem));

                                items.remove(itemToDelete);

                                mToDoListItems.clear();

                                for (HashMap<String, String> item : items) {
                                    ToDoListItem itemTemp = new ToDoListItem(item.get("name"), item.get("priority"));
                                    mToDoListItems.add(itemTemp);
                                }

                                Log.d(TAG, "onComplete: " + mToDoListItems.indexOf(toDoListItem));


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

    class ToDoListItemAdapter extends RecyclerView.Adapter<ToDoListItemAdapter.ToDoListItemViewHolder>{

        @NonNull
        @Override
        public ToDoListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemListItemBinding itemBinding = ListItemListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListItemViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListItemViewHolder holder, int position) {
            ToDoListItem toDoListItem = mToDoListItems.get(position);
            holder.bind(toDoListItem);
        }

        @Override
        public int getItemCount() {
            return mToDoListItems.size();
        }

        class ToDoListItemViewHolder extends RecyclerView.ViewHolder{
            ListItemListItemBinding itemBinding;
            ToDoListItem mToDoListItem;
            public ToDoListItemViewHolder(ListItemListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoListItem toDoListItem) {
                this.mToDoListItem = toDoListItem;

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoListItem(mToDoListItem);
                    }
                });

                itemBinding.textViewName.setText(toDoListItem.getName());
                itemBinding.textViewPriority.setText(toDoListItem.getPriority());
            }
        }
    }

    ToDoListDetailsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListDetailsListener) {
            mListener = (ToDoListDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListDetailsListener");
        }
    }

    public interface ToDoListDetailsListener {
        void gotoAddListItem(ToDoList toDoList);
        void goBackToToDoLists();
    }
}