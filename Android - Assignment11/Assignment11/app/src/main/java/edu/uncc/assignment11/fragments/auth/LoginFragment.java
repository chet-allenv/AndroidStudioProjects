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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.assignment11.databinding.FragmentLoginBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "LoginFragment";

    private static final String BASE_URL = "https://www.theappsdr.com";

    public LoginFragment() {
        // Required empty public constructor
    }

    FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Login");
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Name!!", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Password!!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        binding.buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSignUpUser();
            }
        });

    }

    void loginUser(String email, String password) {

        Log.d(TAG, "loginUser: start");

        RequestBody body = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/login")
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
                            Toast.makeText(getActivity(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {

                        String authToken = json.getString("token");
                        Log.d(TAG, "Token: " + authToken);

                        Log.d(TAG, "Login successful. . .");

                        mListener.onLoginSuccessful(authToken);
                    } else {
                        Log.d(TAG, "Login failed. . .");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        Log.d(TAG, "loginUser: end");

    }

    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
        }
    }

    public interface LoginListener {
        void gotoSignUpUser();
        void onLoginSuccessful(String authToken);
    }
}