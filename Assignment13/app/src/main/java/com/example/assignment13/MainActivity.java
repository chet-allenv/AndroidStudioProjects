package com.example.assignment13;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private OkHttpClient client = new OkHttpClient();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        getPoints(googleMap);
    }

    private void getPoints(GoogleMap googleMap) {

        Log.d(TAG, "getPoints: Start");

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/map/route")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    JSONArray longAndLat = jsonObject.getJSONArray("path");

                    ArrayList<LatLng> points = new ArrayList<>();

                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < longAndLat.length(); i++) {
                                LatLng latLng = null;
                                try {

                                    latLng = new LatLng(longAndLat.getJSONObject(i).getDouble("latitude"), longAndLat.getJSONObject(i).getDouble("longitude"));
                                    points.add(latLng);
                                    boundsBuilder.include(latLng);
                                    
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d(TAG, "onResponse: " + latLng.toString());
                            }

                            LatLngBounds bounds = boundsBuilder.build();

                            Polyline line = googleMap.addPolyline(new PolylineOptions()
                                    .add(points.toArray(new LatLng[]{}))
                                    .width(5)
                                    .color(Color.RED));

                            googleMap.addMarker(new MarkerOptions().position(points.get(0)));
                            googleMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        }
                    });


                    Log.d(TAG, "onResponse: " + jsonObject.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}