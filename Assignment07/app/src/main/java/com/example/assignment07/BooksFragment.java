package com.example.assignment07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.example.assignment07.databinding.FragmentBooksBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BooksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GENRE = "ARG_GENRE";

    // TODO: Rename and change types of parameters
    private String genre;

    private static ArrayList<Book> books;

    private FragmentBooksBinding binding;

    private BookAdapter adapter;

    public BooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BooksFragment newInstance(String genre) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENRE, genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genre = getArguments().getString(ARG_GENRE);
            books = Data.getBooksByGenre(genre);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void updateUI()
    {
        binding.textViewTitle.setText(R.string.title_books);

        binding.textViewSubtitle.setText(genre);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BookAdapter(getContext(), R.layout.book_row_item, books);
        binding.listView.setAdapter(adapter);

        updateUI();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackClicked();
            }
        });

        binding.listView.setOnItemClickListener((parent, view1, position, id) -> {
            Book clickedBook = books.get(position);
            listener.onBookClicked(clickedBook);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (BookListener) context;

    }

    BookListener listener;

    public interface BookListener {
        void onBookClicked(Book book);
        void onBackClicked();
    }
}