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
import edu.uncc.assignment11.databinding.FragmentCreateNewToDoListBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateNewToDoListFragment extends Fragment {
    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "ToDoListsFragment";
    private String AUTH_TOKEN;

    private static final String BASE_URL = "https://www.theappsdr.com";


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
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelCreateNewToDoList();
            }
        });

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("authToken", "not found");
        Log.d(TAG, "onViewCreated: " + AUTH_TOKEN);

        if(AUTH_TOKEN.equals("not found")){
            mListener.onCancelCreateNewToDoList();
        }

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = binding.editTextName.getText().toString().trim();
                if (listName.isEmpty()) {
                    Toast.makeText(getContext(), "List name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: Create new todo list using the api
                    createNewToDoList(listName);
                }
            }
        });
    }

    void createNewToDoList(String listName) {
        Log.d(TAG, "createNewToDoList: start");

        RequestBody body = new FormBody.Builder()
                .add("name", listName)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/todolists/create")
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
                            Toast.makeText(getActivity(), "Failed to create todo list", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "Todo list created successfully", Toast.LENGTH_SHORT).show();
                                mListener.onSuccessCreateNewToDoList();
                            }
                        });
                    } else {
                        Log.d(TAG, json.getString("message"));
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "To Do List creation failed. . .");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "To Do List creation failed. . .", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "createNewToDoList: end");
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