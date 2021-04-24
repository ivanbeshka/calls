package com.example.my_application;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_application.data.Call;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.ViewHolder> {

    private ArrayList<Call> calls;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCallNum;
        private final TextView tvCallTime;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

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
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvCallNum.setText(calls.get(position).getNum());
        viewHolder.tvCallTime.setText(calls.get(position).getDate());

        if (calls.get(position).getDuration() == 0){
            int color = viewHolder.tvCallNum.getResources().getColor(R.color.missed_call);
            viewHolder.tvCallNum.setTextColor(color);
        } else {
            viewHolder.tvCallNum.setTextColor(viewHolder.tvCallTime.getCurrentTextColor());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return calls.size();
    }
}
