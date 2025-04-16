package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentToDoListsBinding;
import edu.uncc.assignment11.databinding.ListItemTodoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoListsFragment extends Fragment {
    public ToDoListsFragment() {
        // Required empty public constructor
    }

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "ToDoListsFragment";
    private String AUTH_TOKEN;

    private static final String BASE_URL = "https://www.theappsdr.com";

    FragmentToDoListsBinding binding;
    ArrayList<ToDoList> mToDoLists = new ArrayList<>();
    ToDoListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListsBinding.inflate(inflater, container, false);
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
            mListener.logout();
        }

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
        Log.d(TAG, "retrieveToDoLists: start");

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolists")
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
                            Toast.makeText(getActivity(), "Failed to retrieve todo lists", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {

                        mToDoLists.clear();

                        JSONArray todoLists = json.getJSONArray("todolists");

                        for (int i = 0; i < todoLists.length(); i++) {
                            JSONObject jsonToDo = todoLists.getJSONObject(i);

                            ToDoList toDoList = new ToDoList();
                            toDoList.setName(jsonToDo.getString("name"));
                            toDoList.setToDoListId(jsonToDo.getInt("todolist_id"));

                            mToDoLists.add(toDoList);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "To Do Lists retrieval failed. . .");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do Lists retrieval failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "retrieveToDoLists: end");
    }

    private void deleteToDoList(ToDoList toDoList) {
        //TODO: delete the todo list using the api
        //TODO: reload the todo lists for the currently logged in user
        Log.d(TAG, "deleteToDoList: start");

        RequestBody body = new FormBody.Builder()
                .add("todolist_id", String.valueOf(toDoList.getToDoListId()))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolists/delete")
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
                            Toast.makeText(getActivity(), "Failed to delete todo list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {
                        mToDoLists.remove(toDoList);

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
                    Log.d(TAG, "To Do Lists deletion failed. . .");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do Lists deletion failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "deleteToDoList: end");
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
                        Log.d(TAG, "onClick: " + toDoList.getToDoListId());
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