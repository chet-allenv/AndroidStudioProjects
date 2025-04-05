package com.example.assignment10.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assignment10.models.City;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {
    public CityAdapter(@NonNull Context context, int resource, @NonNull List<City> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
        }

        City city = getItem(position);

        if (city != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(String.format("%s, %s", city.getName(), city.getState()));
        }

        return convertView;
    }
}
