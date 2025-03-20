package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.models.CreditCategory;
import edu.uncc.assessment03.models.State;

public class FilterAdapter extends ArrayAdapter<CreditCategory> {

    public FilterAdapter(@NonNull Context context, int resource, @NonNull CreditCategory[] objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filter_row_item, parent, false);
        }

        CreditCategory creditCategory = getItem(position);

        ImageView scoreImage = convertView.findViewById(R.id.scoreImage);
        TextView scoreType = convertView.findViewById(R.id.scoreType);


        scoreType.setText(creditCategory.getName());
        scoreImage.setImageResource(creditCategory.getImageResourceId());

        return convertView;
    }
}
