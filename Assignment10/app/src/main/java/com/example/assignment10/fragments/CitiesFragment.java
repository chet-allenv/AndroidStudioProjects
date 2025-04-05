package com.example.assignment10.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.assignment10.R;
import com.example.assignment10.adapters.CityAdapter;
import com.example.assignment10.databinding.FragmentCitiesBinding;
import com.example.assignment10.models.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesFragment extends Fragment {
    private final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "CitiesFragment";

    private CityAdapter mAdapter;
    private ArrayList<City> cities;
    private CitiesListener mListener;

    FragmentCitiesBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    // private String mParam2;

    public CitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CitiesFragment newInstance(OkHttpClient client) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cities = new ArrayList<>();

        mAdapter = new CityAdapter(getContext(), android.R.layout.simple_list_item_1, cities);
        binding.listViewCities.setAdapter(mAdapter);
        mListener = (CitiesListener) getActivity();

        try {
            populateCities();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        binding.listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cities.get(position);
                mListener.onCitySelected(city);
            }
        });

    }

    void populateCities() throws Exception {

        Log.d(TAG, "populateCities: start");

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/cities")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "Response Failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d(TAG, "Response received");

                ArrayList<City> list = new ArrayList<>();

                if (response.isSuccessful()) {

                    Log.d(TAG, "Response Successful");

                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        JSONArray cityArray = json.getJSONArray("cities");

                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject cityObject = cityArray.getJSONObject(i);

                            City city = new City();

                            city.setName(cityObject.getString("name"));
                            city.setState(cityObject.getString("state"));
                            city.setLat(cityObject.getDouble("lat"));
                            city.setLon(cityObject.getDouble("lng"));

                            list.add(city);
                        }

                        Log.d(TAG, "Adding cities to adapter . . .");

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                cities = list;
                                mAdapter.clear();
                                mAdapter.addAll(cities);
                                mAdapter.notifyDataSetChanged();
                            }
                        });


                        Log.d(TAG, "Cities added");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Log.d(TAG, "populateCities: end");
    }
    public interface CitiesListener {
        void onCitySelected(City city);
    }
}