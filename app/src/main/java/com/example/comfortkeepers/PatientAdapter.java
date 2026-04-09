package com.example.comfortkeepers;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patientList;

    // Constructor
    public PatientAdapter(List<Patient> patientList) {
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        holder.firstName.setText(patient.firstName +" "+ patient.lastName);
        holder.gender.setText("Gender: " + patient.gender);
        holder.contactNumber.setText("Contact: " + patient.contactNumber);
        holder.city.setText("City: " + patient.city);
        holder.state.setText("State: " + patient.state);
        holder.email.setText("Email: " + patient.email);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {


        TextView firstName, gender, contactNumber, city, state , email;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            email = itemView.findViewById(R.id.email);
            gender = itemView.findViewById(R.id.gender);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
        }
    }
}
