package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.models.State;

public class StateAdapter extends ArrayAdapter<State> {

    public StateAdapter(@NonNull Context context, int resource, @NonNull State[] objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.state_row_item, parent, false);
        }

        State state = getItem(position);

        TextView textViewStateName = convertView.findViewById(R.id.stateName);
        TextView textViewStateAbbr = convertView.findViewById(R.id.stateAbbr);


        textViewStateName.setText(state.getName());
        textViewStateAbbr.setText(state.getAbbreviation());

        return convertView;
    }
}