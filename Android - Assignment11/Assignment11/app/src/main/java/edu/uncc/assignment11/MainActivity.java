package edu.uncc.assignment11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.uncc.assignment11.fragments.todo.AddItemToToDoListFragment;
import edu.uncc.assignment11.fragments.auth.LoginFragment;
import edu.uncc.assignment11.fragments.todo.CreateNewToDoListFragment;
import edu.uncc.assignment11.fragments.auth.SignUpFragment;
import edu.uncc.assignment11.fragments.todo.ToDoListDetailsFragment;
import edu.uncc.assignment11.fragments.todo.ToDoListsFragment;
import edu.uncc.assignment11.models.ToDoList;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener,
        ToDoListsFragment.ToDoListsListener, ToDoListDetailsFragment.ToDoListDetailsListener, CreateNewToDoListFragment.CreateNewToDoListListener,
        AddItemToToDoListFragment.AddItemToListListener{

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

        String authToken = readTokenFromSharedPreferences();

        if (authToken.equals("not found")) {
            Log.d(TAG, "onCreate: authToken not found");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new LoginFragment())
                    .commit();
        } else {
            Log.d(TAG, "onCreate: authToken found");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ToDoListsFragment())
                    .commit();
        }
    }

    @Override
    public void gotoSignUpUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SignUpFragment())
                .commit();
    }

    @Override
    public void onLoginSuccessful(String authToken) {

        writeTokenToSharedPreferences(authToken);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();

        Log.d(TAG, "onLoginSuccessful: " + authToken);
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }

    @Override
    public void onSignUpSuccessful(String authToken) {

        writeTokenToSharedPreferences(authToken);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    @Override
    public void gotoCreateNewToDoList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new CreateNewToDoListFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoToDoListDetails(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, ToDoListDetailsFragment.newInstance(toDoList))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        deleteTokenFromSharedPreferences();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();

        Log.d(TAG, "Logging out current user");

    }

    @Override
    public void onSuccessCreateNewToDoList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelCreateNewToDoList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoAddListItem(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, AddItemToToDoListFragment.newInstance(toDoList))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToToDoLists() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSuccessAddItemToList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelAddItemToList(ToDoList todoList) {
        getSupportFragmentManager().popBackStack();
    }

    private void writeTokenToSharedPreferences(String token) {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(getString(R.string.preference_file_key), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authToken", token);
        editor.apply();

        Log.d(TAG, "Writing token to SharedPreferences: " + sharedPreferences.getString("authToken", "not found"));
    }

    private String readTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(getString(R.string.preference_file_key), this.MODE_PRIVATE);

        String token = sharedPreferences.getString("authToken", "not found");

        Log.d(TAG, "Reading token from SharedPreferences: " + token);
        return token;

    }

    private void deleteTokenFromSharedPreferences() {
        SharedPreferences sP = this.getSharedPreferences(getString(R.string.preference_file_key), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.remove("authToken");
        editor.apply();

        Log.d(TAG, "Deleting token from SharedPreferences");
    }
}