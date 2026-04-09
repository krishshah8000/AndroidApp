package com.example.comfortkeepers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.ViewHolder> {

    private List<Specialist> specialistList;
    private Context context;

    public SpecialistAdapter(Context context, List<Specialist> specialistList) {
        this.context = context;
        this.specialistList = specialistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specialist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Specialist specialist = specialistList.get(position);
        holder.firstName.setText(specialist.getFirstName());
        holder.lastName.setText(specialist.getLastName());
        holder.regularCharge.setText("Regular: ₹" + specialist.getRegular_charge());
        holder.visitCharge.setText("Visit: ₹" + specialist.getVisit_charge());

        // Pass specialist's first name to BookingActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("specialistName", specialist.getFirstName());
            intent.putExtra("visitCharge", specialist.getVisit_charge());
            intent.putExtra("regularCharge", specialist.getRegular_charge());
            intent.putExtra("subcategory", specialist.getSubcategory()); // Add this line
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return specialistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, regularCharge, visitCharge;

        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            regularCharge = itemView.findViewById(R.id.regularCharge);
            visitCharge = itemView.findViewById(R.id.visitCharge);
        }
    }
}
