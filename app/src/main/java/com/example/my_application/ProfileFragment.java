package com.example.my_application;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public static final String phoneNum = "phone_number";
    private TextView profileNum;

    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.profile);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        profileNum = view.findViewById(R.id.tv_profile_num);
        profileNum.setText(getArguments().getString(phoneNum));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
