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

import edu.uncc.assignment11.databinding.FragmentSignupBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment {

    private OkHttpClient client = new OkHttpClient();

    private static final String TAG = "SignUpFragment";

    private static final String BASE_URL = "https://www.theappsdr.com";

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

                if(fname.isEmpty()){
                    Toast.makeText(getContext(), "First Name is required", Toast.LENGTH_SHORT).show();
                } else if(lname.isEmpty()){
                    Toast.makeText(getContext(), "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(fname, lname, email, password);
                }
            }
        });
    }

    void createUser(String fname, String lname, String email, String password) {
        //TODO: create the user using the api
        Log.d(TAG, "createUser: start");

        RequestBody body = new FormBody.Builder()
                .add("fname", fname)
                .add("lname", lname)
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/signup")
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
                            Toast.makeText(getActivity(), "Sign Up failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                try {
                    JSONObject json = new JSONObject(response.body().string());

                    if (json.getString("status").equals("ok")) {

                        String authToken = json.getString("token");
                        Log.d(TAG, "Token: " + authToken);

                        Log.d(TAG, "Sign Up successful. . .");

                        mListener.onSignUpSuccessful(authToken);
                    } else if (json.getString("status").equals("error")) {
                        Log.d(TAG, "Sign Up failed. . .");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "Sign Up failed. . .");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Sign Up failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Sign Up failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "createUser: end");
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
        void onSignUpSuccessful(String authToken);
    }
}