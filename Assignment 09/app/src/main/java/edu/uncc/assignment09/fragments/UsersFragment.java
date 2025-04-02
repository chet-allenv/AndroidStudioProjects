package edu.uncc.assignment09.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assignment09.R;
import edu.uncc.assignment09.databinding.FragmentUsersBinding;
import edu.uncc.assignment09.databinding.RowItemUserBinding;
import edu.uncc.assignment09.models.AppDatabase;
import edu.uncc.assignment09.models.CreditCategory;
import edu.uncc.assignment09.models.User;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    String selectedSort;
    String sortDirection = "ASC";
    CreditCategory selectedFilterCategory;

    ArrayList<User> mUsers;
    UsersAdapter adapter = new UsersAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");

        mUsers = mListener.getAllUsers();

        if (mUsers.isEmpty()) {
            mListener.populateEmptyArray();
            mUsers = mListener.getAllUsers();
            adapter.notifyDataSetChanged();
        }

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
                    Collections.reverse(mUsers);
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
                    Collections.reverse(mUsers);
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

            mListener.getAllUsers().stream().filter(user -> user.getCreditScore() >= finalSelectedCategoryMinScore).forEach(tempUsers::add);

            mUsers = tempUsers;
            adapter.notifyDataSetChanged();
        }

        if(selectedSort == null){
            selectedSort = "Name";
            sortDirection = "ASC";
        }
        else {

            switch (selectedSort) {
                case "Name":
                    mUsers.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            if (sortDirection.equals("ASC")) {
                                return user.getName().compareTo(t1.getName());
                            } else {
                                return t1.getName().compareTo(user.getName());
                            }
                        }
                    });
                    break;
                case "Age":
                    mUsers.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            if (sortDirection.equals("ASC")) {
                                return user.getAge() - t1.getAge();
                            } else {
                                return t1.getAge() - user.getAge();
                            }
                        }
                    });
                    break;
                case "Credit Score":
                    mUsers.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            if (sortDirection.equals("ASC")) {
                                return user.getCreditScore() - t1.getCreditScore();
                            } else {
                                return t1.getCreditScore() - user.getCreditScore();
                            }
                        }
                    });
                    break;
                case "State":
                    mUsers.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            if (sortDirection.equals("ASC")) {
                                return user.getState().compareTo(t1.getState());
                            } else {
                                return t1.getState().compareTo(user.getState());
                            }
                        }
                    });
                    break;
            }
        }

        binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter();
        binding.recyclerView.setAdapter(adapter);
    }

    class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowItemUserBinding itemBinding = RowItemUserBinding.inflate(getLayoutInflater(), parent, false);
            return new UserViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = mUsers.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            RowItemUserBinding itemBinding;
            User mUser;
            public UserViewHolder(RowItemUserBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(User user) {
                this.mUser = user;
                itemBinding.textViewName.setText(user.getName());
                itemBinding.textViewAge.setText(user.getAge() + " years old");
                itemBinding.textViewState.setText(user.getState());

                //Poor (300-579), Fair (580-669), Good (670-739), Very Good (740-799), Excellent (800-850).
                itemBinding.textViewCreditScore.setText(String.valueOf(user.getCreditScore()));
                if(user.getCreditScore() <= 579){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.poor);
                } else if(user.getCreditScore() <= 669){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.fair);
                } else if(user.getCreditScore() <= 739){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.good);
                } else if(user.getCreditScore() <= 799){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.very_good);
                } else {
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.excellent);
                }

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete the user from DB
                        //reload the list using the adapter
                        mListener.deleteUser(mUser);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
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
        void deleteUser(User user);

        void populateEmptyArray();
    }
}