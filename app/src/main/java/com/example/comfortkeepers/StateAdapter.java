package com.example.comfortkeepers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {

    private List<String> stateList;
    private List<String> fullList;

    public StateAdapter(List<String> stateList) {
        this.stateList = new ArrayList<>(stateList);
        this.fullList = new ArrayList<>(stateList);
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateViewHolder holder, int position) {
        holder.stateName.setText(stateList.get(position));
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public void filter(String query) {
        stateList.clear();
        if (query.isEmpty()) {
            stateList.addAll(fullList);
        } else {
            for (String state : fullList) {
                if (state.toLowerCase().contains(query.toLowerCase())) {
                    stateList.add(state);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class StateViewHolder extends RecyclerView.ViewHolder {
        TextView stateName;

        public StateViewHolder(View itemView) {
            super(itemView);
            stateName = itemView.findViewById(R.id.stateNameText);
        }
    }
}