package com.example.my_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.TextView;

import com.example.my_application.data.Call;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (!checkPermissions()) {
            requestPermissions();
        } else {
            initView();
            getCallDetails();
        }

    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_calls);
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
        Cursor managedCursor = this.getContentResolver().query(contacts, null, null, null, null);
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

    /**
     * this method request to permission asked.
     */
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CALL_LOG);

        if (shouldProvideRationale) {
        } else {
            Log.i("Error", "Requesting permission");
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * this method check permission and return current state of permission need.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("Error", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Kick off the process of building and connecting
                // GoogleApiClient.
                getCallDetails();
            } else {
            }
        }

    }
}