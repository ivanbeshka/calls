package com.example.my_application;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_application.data.Call;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.ViewHolder> {

    private ArrayList<Call> calls;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCallNum;
        private final TextView tvCallTime;
        private final AppCompatImageButton ibPopUpMenu;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            ibPopUpMenu = view.findViewById(R.id.iv_call_more);
            tvCallNum = view.findViewById(R.id.call_num);
            tvCallTime = view.findViewById(R.id.call_date);
        }
    }

    public CallsAdapter(ArrayList<Call> dataSet) {
        calls = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.call_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Call currentCall = calls.get(position);

        //call number
        String callsNum = currentCall.getCallsNum() > 1 ? " (" + currentCall.getCallsNum() + ")" : "";
        if (currentCall.getCachedName() != null && !currentCall.getCachedName().isEmpty()){
            viewHolder.tvCallNum.setText(currentCall.getCachedName() + callsNum);
        }else {
            viewHolder.tvCallNum.setText(currentCall.getNum() + callsNum);
        }

        //date
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
        String callDayTime = format.format(currentCall.getDate());
        viewHolder.tvCallTime.setText(callDayTime);

        //number color
        if (currentCall.getDuration() == 0){
            int color = viewHolder.tvCallNum.getResources().getColor(R.color.missed_call);
            viewHolder.tvCallNum.setTextColor(color);
        } else {
            viewHolder.tvCallNum.setTextColor(viewHolder.tvCallTime.getCurrentTextColor());
        }

        setCallMenuClickListener(viewHolder, currentCall);

        viewHolder.tvCallNum.setOnClickListener(textView -> {
            Bundle bundle = new Bundle();
            bundle.putString(ProfileFragment.phoneNum, currentCall.getNum() + " (" + currentCall.getCachedName() + ")");
            NavHostFragment navFragment = (NavHostFragment) ((AppCompatActivity) textView.getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navFragment.getNavController().navigate(R.id.action_mainFragment_to_profileFragment, bundle);
        });
    }

    private void setCallMenuClickListener(@NotNull ViewHolder viewHolder, Call currentCall) {
        viewHolder.ibPopUpMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.setOnMenuItemClickListener(menu -> {
                switch (menu.getItemId()){
                    case R.id.copy:
                        ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Number", currentCall.getNum());
                        clipboard.setPrimaryClip(clip);
                        return true;
                    case R.id.spam:
                        return true;
                    default:
                        return false;
                }
            });

            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.call_menu, popup.getMenu());
            popup.show();
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return calls.size();
    }
}
