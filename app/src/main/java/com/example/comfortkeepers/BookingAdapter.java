package com.example.comfortkeepers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookingList;
    private String specialistName;


    public BookingAdapter(Context context, List<Booking> bookingList, String specialistName) {
        this.context = context;
        this.bookingList = bookingList;
        this.specialistName = specialistName; // Store specialist name
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_cardview, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set data
        holder.name.setText(booking.getPatientName());
        holder.date.setText("Date: " + booking.getDate());
        holder.time.setText("Time: " + booking.getTime());
        holder.status.setText("Status: " + booking.getStatus());

        // Handle CardView click to open SpePatientDetailsActivity
        holder.cardView.setOnClickListener(v -> {
            if (specialistName == null || booking.getDate() == null || booking.getTime() == null) {
                Toast.makeText(context, "Error: Missing appointment details!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(context, SpePatientDetailsActivity.class);
            intent.putExtra("patientName", booking.getPatientName());
            intent.putExtra("specialistName", specialistName); // Pass specialist name
            intent.putExtra("date", booking.getDate());
            intent.putExtra("time", booking.getTime());
            intent.putExtra("status", booking.getStatus());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, time, status;
        CardView cardView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvPatientName);
            date = itemView.findViewById(R.id.tvBookingDate);
            time = itemView.findViewById(R.id.tvBookingTime);
            status = itemView.findViewById(R.id.tvStatus);
            cardView = itemView.findViewById(R.id.cardBooking);
        }
    }
}
