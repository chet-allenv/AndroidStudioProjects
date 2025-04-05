package com.example.assignment10.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment10.R;
import com.example.assignment10.adapters.ForecastViewAdapter;
import com.example.assignment10.databinding.FragmentWeatherForecastBinding;
import com.example.assignment10.models.City;
import com.example.assignment10.models.Forecast;

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
 * Use the {@link WeatherForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecastFragment extends Fragment {

    private OkHttpClient client = new OkHttpClient();
    private static final String TAG = "WeatherForecastFragment";

    private ArrayList<Forecast> forecasts;
    FragmentWeatherForecastBinding binding;
    ForecastViewAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CITY = "city";

    // TODO: Rename and change types of parameters
    private City city;

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherForecastFragment newInstance(City city) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forecasts = new ArrayList<>();

        binding.recyclerForecasts.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerForecasts.setLayoutManager(linearLayoutManager);

        mAdapter = new ForecastViewAdapter(forecasts);
        binding.recyclerForecasts.setAdapter(mAdapter);

        binding.textViewCityname.setText(String.format("%s, %s", city.getName(), city.getState()));


        if (forecasts == null) {
            forecasts = new ArrayList<>();
        }

        gatherForecastData();

    }

    void gatherForecastData() {

        Log.d(TAG, "gatherForecastData: start");

        Request request = new Request.Builder()
                .url(String.format("https://api.weather.gov/points/%f,%f", city.getLat(), city.getLon()))
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

                if (response.isSuccessful()) {

                    Log.d(TAG, "Response Successful");

                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        JSONObject properties = json.getJSONObject("properties");

                        String forecastUrl = properties.getString("forecast");

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                populateForecast(forecastUrl);
                            }
                        });


                        Log.d(TAG, "Cities added");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Log.d(TAG, "gatherForecastData: end");
    }

    void populateForecast(String forecastUrl) {
        Log.d(TAG, "populateForecast: start");

        Request request = new Request.Builder()
                .url(forecastUrl)
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

                if (response.isSuccessful()) {

                    Log.d(TAG, "Response Successful");

                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        JSONObject properties = json.getJSONObject("properties");

                        JSONArray forecastArray = properties.getJSONArray("periods");

                        for (int i = 0; i < forecastArray.length(); i++) {
                            JSONObject forecastObject = forecastArray.getJSONObject(i);

                            Forecast forecast = new Forecast();

                            forecast.setStartTime(forecastObject.getString("startTime"));
                            forecast.setTemperature(forecastObject.getInt("temperature"));

                            forecast.setWindSpeed(forecastObject.getString("windSpeed"));
                            forecast.setShortForecast(forecastObject.getString("shortForecast"));
                            forecast.setWeatherIconURL(forecastObject.getString("icon"));

                            JSONObject probabilityOfPrecipitation = forecastObject.getJSONObject("probabilityOfPrecipitation");
                            String percent = probabilityOfPrecipitation.getString("value");

                            if (percent.isEmpty() || percent.equals("null")) {
                                percent = "0";
                            }

                            forecast.setPrecipitationPercent(Integer.parseInt(percent));

                            forecasts.add(forecast);
                        }

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                mAdapter.notifyDataSetChanged();
                            }

                        });

                        Log.d(TAG, "Forecasts added");

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Log.d(TAG, "gatherForecastData: end");
    }

    public interface WeatherForecastListener {

    }
}