package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "ToDoListsFragment";
    private String AUTH_TOKEN;

    private static final String BASE_URL = "https://www.theappsdr.com";

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

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("authToken", "not found");
        Log.d(TAG, "onViewCreated: " + AUTH_TOKEN);

        if(AUTH_TOKEN.equals("not found")){
            mListener.goBackToToDoLists();
        }

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
        Log.d(TAG, "loadToDoListItems: start");

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolists/" + mToDoList.getToDoListId())
                .addHeader("Authorization", "BEARER " + AUTH_TOKEN)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "Response failed. . .");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "Response received. . .");

                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response not successful. . .");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Failed to retrieve todo list items", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    JSONObject todoList = json.getJSONObject("todolist");

                    JSONArray jsonItems = todoList.getJSONArray("items");

                    for(int i = 0; i < jsonItems.length(); i++) {
                        JSONObject jsonItem = jsonItems.getJSONObject(i);
                        ToDoListItem toDoListItem = new ToDoListItem();

                        toDoListItem.setName(jsonItem.getString("name"));
                        toDoListItem.setPriority(jsonItem.getString("priority"));
                        toDoListItem.setToDoListId(jsonItem.getInt("todolist_item_id"));

                        mToDoListItems.add(toDoListItem);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    Log.d(TAG, "To Do Item List retrieval failed. . .");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do Item List retrieval failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "loadToDoListItems: end");
    }

    void deleteToDoListItem(ToDoListItem toDoListItem){
        //TODO: Delete the item using the api
        //TODO: Reload the items for the to do list using the api

        Log.d(TAG, "deleteToDoListItem: start");

        RequestBody body = new FormBody.Builder()
                .add("todolist_id", String.valueOf(mToDoList.getToDoListId()))
                .add("todolist_item_id", String.valueOf(toDoListItem.getToDoListId()))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolist-items/delete")
                .addHeader("Authorization", "BEARER " + AUTH_TOKEN)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "Response failed. . .");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "Response received. . .");

                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response not successful. . .");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Failed to delete todo list item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {
                        mToDoListItems.remove(toDoListItem);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Log.d(TAG, json.getString("message"));
                    } else {
                        Log.d(TAG, json.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "To Do List item deletion failed. . .");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do List item deletion failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "deleteToDoListItem: end");
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