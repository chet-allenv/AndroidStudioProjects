package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.databinding.FragmentUsersBinding;
import edu.uncc.assessment03.models.CreditCategory;
import edu.uncc.assessment03.models.User;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    String selectedSort;
    String sortDirection = "ASC";
    CreditCategory selectedFilterCategory;

    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");

        ArrayList<User> users = mListener.getAllUsers();
        UserAdapter adapter = new UserAdapter(users);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());

        binding.recyclerView.setLayoutManager(layoutManager);


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_user_action){
                    mListener.gotoAddUser();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.imageViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

        binding.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectFilter();
            }
        });

        binding.imageViewSortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (sortDirection.equals("DESC")) {
                  Collections.reverse(adapter.mUsers);
               }
               sortDirection = "ASC";
                binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
               adapter.notifyDataSetChanged();

            }
        });

        binding.imageViewSortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortDirection.equals("ASC")) {
                    Collections.reverse(adapter.mUsers);
                }
                sortDirection = "DESC";
                binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
                adapter.notifyDataSetChanged();
            }
        });




        if(selectedFilterCategory == null){
            binding.textViewFilter.setText("N/A");
        } else {
            binding.textViewFilter.setText(selectedFilterCategory.getName() + " or Higher" );

            int selectedCategoryMinScore = 300;

            switch (selectedFilterCategory.getName()){
                case "Excellent":
                    selectedCategoryMinScore = 800;
                    break;
                case "Very Good":
                    selectedCategoryMinScore = 740;
                    break;
                case "Good":
                    selectedCategoryMinScore = 670;
                    break;
                case "Fair":
                    selectedCategoryMinScore = 580;
                    break;
                case "Poor":
                    break;
            }
            int finalSelectedCategoryMinScore = selectedCategoryMinScore;

            ArrayList<User> tempUsers = new ArrayList<>();

            users.stream().filter(user -> user.getCreditScore() >= finalSelectedCategoryMinScore).forEach(tempUsers::add);

            adapter.mUsers = tempUsers;
            adapter.notifyDataSetChanged();
        }

        if(selectedSort == null){
            selectedSort = "Name";
            sortDirection = "ASC";
        } else {

            if (selectedSort.equals("Name")) {
                adapter.mUsers.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        if (sortDirection.equals("ASC")) {
                            return user.getName().compareTo(t1.getName());
                        } else {
                            return t1.getName().compareTo(user.getName());
                        }
                    }
                });
            } else if (selectedSort.equals("Age")) {
                adapter.mUsers.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        if (sortDirection.equals("ASC")) {
                            return user.getAge() - t1.getAge();
                        } else {
                            return t1.getAge() - user.getAge();
                        }
                    }
                });
            } else if (selectedSort.equals("Credit Score")) {
                adapter.mUsers.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        if (sortDirection.equals("ASC")) {
                            return user.getCreditScore() - t1.getCreditScore();
                        } else {
                            return t1.getCreditScore() - user.getCreditScore();
                        }
                    }
                });
            } else if (selectedSort.equals("State")) {
                adapter.mUsers.sort(new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        if (sortDirection.equals("ASC")) {
                            return user.getState().getName().compareTo(t1.getState().getName());
                        } else {
                            return t1.getState().getName().compareTo(user.getState().getName());
                        }
                    }
                });
            }

        }
        binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
    }

    UsersListener mListener;


    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
    }

    public void setSelectedFilterCategory(CreditCategory selectedFilterCategory) {
        this.selectedFilterCategory = selectedFilterCategory;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UsersListener) {
            mListener = (UsersListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UsersListener");
        }
    }

    public interface UsersListener {
        void gotoAddUser();
        void gotoSelectFilter();
        void gotoSelectSort();
        ArrayList<User> getAllUsers();
    }
}