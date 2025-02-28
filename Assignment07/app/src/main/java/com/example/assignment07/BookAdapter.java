package com.example.assignment07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book>
{
    public BookAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_row_item, parent, false);
        }

        Book book = getItem(position);

        TextView textViewBookTitle = convertView.findViewById(R.id.textViewBookTitle);
        TextView textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
        TextView textViewGenre = convertView.findViewById(R.id.textViewGenre);
        TextView textViewYear = convertView.findViewById(R.id.textViewYear);

        textViewBookTitle.setText(book.getTitle());
        textViewAuthor.setText(book.getAuthor());
        textViewGenre.setText(book.getGenre());
        textViewYear.setText(Integer.toString(book.getYear()));

        return convertView;
    }
}
