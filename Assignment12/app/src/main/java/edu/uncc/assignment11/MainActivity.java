package edu.uncc.assignment11;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();

        if(mCurrentUser != null) {
            Log.d(TAG, "onLoginSuccessful: " + mCurrentUser.getEmail());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ToDoListsFragment())
                    .commit();
        } else {
            Log.d(TAG, "onLoginSuccessful: Not logged in");

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new LoginFragment())
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
    public void onLoginSuccessful() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();

        Log.d(TAG, "onLoginSuccessful: Log in successful!");
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }

    @Override
    public void onSignUpSuccessful() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();

        Log.d(TAG, "onSignUpSuccessful: Sign up successful!");
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
        mAuth.signOut();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
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
}