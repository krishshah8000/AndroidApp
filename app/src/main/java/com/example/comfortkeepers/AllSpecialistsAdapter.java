package com.example.comfortkeepers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllSpecialistsAdapter extends RecyclerView.Adapter<AllSpecialistsAdapter.SpecialistViewHolder> {

    Context context;
    List<Specialist> specialistList;

    public AllSpecialistsAdapter(Context context, List<Specialist> specialistList) {
        this.context = context;
        this.specialistList = specialistList;
    }

    @NonNull
    @Override
    public SpecialistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_specialist, parent, false);
        return new SpecialistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialistViewHolder holder, int position) {
        Specialist s = specialistList.get(position);

        String fullName = s.getFirstName() + " " + s.getLastName();
        holder.nameTextView.setText(fullName);
        holder.emailTextView.setText("Email: " + s.getEmail());
        holder.categoryTextView.setText("Category: " + s.getCategory());
        holder.subcategoryTextView.setText("Subcategory: " + s.getSubcategory());
    }

    @Override
    public int getItemCount() {
        return specialistList.size();
    }

    public static class SpecialistViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, categoryTextView, subcategoryTextView;

        public SpecialistViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            subcategoryTextView = itemView.findViewById(R.id.textViewSubcategory);
        }
    }
}