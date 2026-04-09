package com.example.comfortkeepers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> appointmentList;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.txtSpecialistName.setText("Specialist: " + appointment.getSpecialistName());
        holder.txtDate.setText("Date: " + appointment.getDate());
        holder.txtTime.setText("Time: " + appointment.getTime());
        holder.txtVisitCharge.setText("Visit Charge: ₹" + appointment.getVisitCharge());
        holder.txtRegularCharge.setText("Regular Charge: ₹" + appointment.getRegularCharge());

        // Check if subcategory is null or empty and handle accordingly
        String subcategory = appointment.getSubcategory();
        if (subcategory != null && !subcategory.isEmpty() && !subcategory.equals("Not specified")) {
            holder.txtSubcategory.setVisibility(View.VISIBLE);
            holder.txtSubcategory.setText("Service: " + subcategory);
        } else {
            holder.txtSubcategory.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSpecialistName, txtDate, txtTime, txtVisitCharge, txtRegularCharge, txtSubcategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSpecialistName = itemView.findViewById(R.id.txtSpecialistName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtVisitCharge = itemView.findViewById(R.id.txtVisitCharge);
            txtRegularCharge = itemView.findViewById(R.id.txtRegularCharge);
            txtSubcategory = itemView.findViewById(R.id.txtSubcategory); // Make sure this ID exists in your item_appointment.xml
        }
    }
}