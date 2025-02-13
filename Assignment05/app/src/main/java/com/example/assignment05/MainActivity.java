package com.example.assignment05;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements WelcomeFragment.WelcomeListener, CreateUserFragment.CreateUserListener, ProfileFragment.ProfileListener, EditUserFragment.EditUserListener {

    // IMPLEMENT THE INTERFACE

    User user;

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

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, new WelcomeFragment(), "Welcome-Fragment")
                .commit();
    }


    @Override
    public void startButtonPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, new CreateUserFragment(), "Create-User-Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void nextButtonPressed(User user) {

        //this.user = user;

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainerView, ProfileFragment.newInstance(user), "Profile-Fragment")
                .addToBackStack(null)
            .commit();
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void updateButtonPressed() {

        ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("Profile-Fragment");
        EditUserFragment euf;

        if (pf != null)
        {
            euf = new EditUserFragment();
            euf.setUser(pf.getUser());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, euf, "Edit-User-Fragment")
                    .commit();
        }
    }

    @Override
    public void onCancelButtonPressed() {
        ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("Profile-Fragment");

        if (pf != null)
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, pf)
                    .commit();
        }
    }

    @Override
    public void onSubmitButtonPressed(User user) {

        ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("Profile-Fragment");

        if (pf != null)
        {
            pf.setUser(user);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, pf)
                    .commit();
        }

    }
}