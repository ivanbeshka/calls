package com.example.my_application;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_application.data.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    public MainFragment(){
        super(R.layout.fragment_main);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(false);

        recyclerView = view.findViewById(R.id.rv_calls);
        getCallDetails();

        return view;
    }

    private void getCallDetails() {
        ArrayList<Call> calls = getCalls();

        CallsAdapter adapter = new CallsAdapter(calls);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private ArrayList<Call> getCalls() {
        ArrayList<Call> calls = new ArrayList<>();

        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = getActivity().getContentResolver().query(contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callDate = managedCursor.getString(date);
            int currentDuration = managedCursor.getInt(duration);

            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy HH:mm");
            String callDayTime = format.format(new Date(Long.valueOf(callDate)));

            calls.add(new Call(phNumber, callDayTime, currentDuration));
        }
        managedCursor.close();

        return calls;
    }
}
