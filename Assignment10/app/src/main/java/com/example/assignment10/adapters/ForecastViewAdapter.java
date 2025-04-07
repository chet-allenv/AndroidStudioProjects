package com.example.assignment10.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment10.R;
import com.example.assignment10.models.Forecast;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ForecastViewAdapter extends RecyclerView.Adapter<ForecastViewAdapter.ForecastViewHolder> {

    ArrayList<Forecast> mForecasts = new ArrayList<>();

    public ForecastViewAdapter(ArrayList<Forecast> forecasts) {
        this.mForecasts = forecasts;
    }

    @NonNull
    @Override
    public ForecastViewAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row_item, parent, false);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewAdapter.ForecastViewHolder holder, int position) {
        Forecast forecast = mForecasts.get(position);

        holder.textViewStartTime.setText(forecast.getStartTime());
        holder.textViewTemperature.setText(String.format("%d°F", forecast.getTemperature()));
        holder.textViewPrecipitation.setText(String.format("Precipitation: %d%%", forecast.getPrecipitationPercent()));
        holder.textViewWindSpeed.setText(String.format("Wind Speed: %s", forecast.getWindSpeed()));
        holder.textViewShortForecast.setText(forecast.getShortForecast());

        Picasso.get().load(forecast.getWeatherIconURL()).into(holder.imageViewWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return this.mForecasts.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStartTime;
        TextView textViewTemperature;
        TextView textViewPrecipitation;
        TextView textViewWindSpeed;
        TextView textViewShortForecast;
        ImageView imageViewWeatherIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);
            textViewPrecipitation = itemView.findViewById(R.id.textViewPrecipitationPercent);
            textViewWindSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            textViewShortForecast = itemView.findViewById(R.id.textViewShortForecast);
            imageViewWeatherIcon = itemView.findViewById(R.id.imageViewWeatherIcon);

        }
    }
}
