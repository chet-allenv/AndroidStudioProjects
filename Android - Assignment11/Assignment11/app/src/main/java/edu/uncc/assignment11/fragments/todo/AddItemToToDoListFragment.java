package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentAddItemToToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddItemToToDoListFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST = "ARG_PARAM_TODO_LIST";
    private ToDoList mTodoList;

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "ToDoListsFragment";
    private String AUTH_TOKEN;

    private static final String BASE_URL = "https://www.theappsdr.com";

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

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("authToken", "not found");
        Log.d(TAG, "onViewCreated: " + AUTH_TOKEN);

        if(AUTH_TOKEN.equals("not found")){
            mListener.onCancelAddItemToList(mTodoList);
        }

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
                    createNewToDoListItem(itemName, priority);
                }
            }
        });


    }

    void createNewToDoListItem(String itemName, String priority) {
        Log.d(TAG, "createNewToDoListItem: start");

        RequestBody body = new FormBody.Builder()
                .add("todolist_id", String.valueOf(mTodoList.getToDoListId()))
                .add("name", itemName)
                .add("priority", priority)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolist-items/create")
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
                            Toast.makeText(getActivity(), "Failed to create todo list item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {
                        Log.d(TAG, json.getString("message"));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Todo list item created successfully", Toast.LENGTH_SHORT).show();
                                mListener.onSuccessAddItemToList();
                            }
                        });
                    } else {
                        Log.d(TAG, json.getString("message"));
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "To Do List item creation failed. . .");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do List item creation failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "createNewToDoListItem: end");
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