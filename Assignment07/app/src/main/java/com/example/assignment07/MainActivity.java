package com.example.assignment07;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements GenreFragment.GenreListener, BooksFragment.BookListener, BookDetailsFragment.BookDetailsListener {

    private static final String GENRE_TAG = "GENRE_TAG";

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new GenreFragment(), GENRE_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onGenreClicked(String genre) {
        // DO SOMETHING;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, BooksFragment.newInstance(genre), GENRE_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBookClicked(Book book) {
        // DO SOMETHING
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, BookDetailsFragment.newInstance(book), GENRE_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackClicked() {
        getSupportFragmentManager().popBackStack();
    }
}