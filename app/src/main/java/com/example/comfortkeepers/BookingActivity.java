package com.example.comfortkeepers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends AppCompatActivity {

    private DatabaseReference database;
    private TextView selectedDateText, noSlotsText;
    private Button selectDateButton, bookSlotButton;
    private List<String> timeSlots;
    private List<Button> slotButtons;
    private String selectedDate, selectedSlot = null;
    private String specialistName;
    private GridLayout slotsLayout;
    private String visitCharge, regularCharge;
    private Button lastSelectedButton = null;
    private String subcategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        selectedDateText = findViewById(R.id.selectedDateText);
        selectDateButton = findViewById(R.id.selectDateButton);
        bookSlotButton = findViewById(R.id.bookSlotButton);
        slotsLayout = findViewById(R.id.slotsLayout);
        noSlotsText = findViewById(R.id.noSlotsText);
        database = FirebaseDatabase.getInstance().getReference("bookings");

        specialistName = getIntent().getStringExtra("specialistName");
        visitCharge = getIntent().getStringExtra("visitCharge");
        regularCharge = getIntent().getStringExtra("regularCharge");
        subcategory = getIntent().getStringExtra("subcategory"); // Add this line


        if (specialistName == null || specialistName.isEmpty()) {
            Toast.makeText(this, "Error: No specialist selected!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        timeSlots = Arrays.asList("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM",
                "6:00 PM", "7:00 PM", "8:00 PM");
        slotButtons = new ArrayList<>();

        selectDateButton.setOnClickListener(view -> showDatePicker());
        bookSlotButton.setOnClickListener(view -> bookSelectedSlot());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, month1, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(selectedCalendar.getTime());
            selectedDateText.setText("Selected Date: " + selectedDate);
            fetchSlotsFromFirebase();
        }, year, month, day);

        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, 1);
        datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePicker.show();
    }

    private void fetchSlotsFromFirebase() {
        if (selectedDate == null || specialistName == null) return;

        database.child(specialistName).child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> bookedSlots = new ArrayList<>();

                if (!snapshot.exists()) {
                    generateSlotButtons(new ArrayList<>());
                    return;
                }

                for (DataSnapshot slotSnapshot : snapshot.getChildren()) {
                    String bookedTime = slotSnapshot.getKey();
                    if (bookedTime != null && slotSnapshot.hasChildren()) {
                        bookedSlots.add(bookedTime);
                    }
                }

                generateSlotButtons(bookedSlots);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingActivity.this, "Failed to load slots", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateSlotButtons(List<String> bookedSlots) {
        slotsLayout.removeAllViews();
        slotButtons.clear();
        lastSelectedButton = null;
        selectedSlot = null;

        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(now.getTime());
        int currentHour = now.get(Calendar.HOUR_OF_DAY); // Get the current hour (24-hour format)

        for (final String time : timeSlots) {
            String timeOnly = time.split(" ")[0]; // Extract only the time part
            String amPm = time.split(" ")[1]; // AM or PM

            int slotHour = Integer.parseInt(timeOnly.split(":")[0]); // Get hour part
            if (amPm.equals("PM") && slotHour != 12) {
                slotHour += 12; // Convert PM time to 24-hour format
            } else if (amPm.equals("AM") && slotHour == 12) {
                slotHour = 0; // Convert 12 AM to 0
            }

            // Skip past slots if the selected date is today
            if (selectedDate.equals(todayDate) && slotHour <= currentHour) {
                continue;
            }

            final Button button = new Button(this);
            button.setText(time);
            button.setTextSize(14);
            button.setPadding(10, 10, 10, 10);
            button.setAllCaps(false);
            button.setBackgroundResource(R.drawable.slot_border);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(5, 5, 5, 5);
            button.setLayoutParams(params);

            if (bookedSlots.contains(time)) {
                button.setEnabled(false);
                button.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                button.setTag("booked");
            } else {
                button.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                button.setOnClickListener(v -> selectSlot(button, time));
            }

            slotButtons.add(button);
            slotsLayout.addView(button);
        }
    }

    private void selectSlot(Button button, String time) {
        if ("booked".equals(button.getTag())) {
            Toast.makeText(this, "Slot already booked. Please select another.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (button.equals(lastSelectedButton)) {
            button.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            selectedSlot = null;
            lastSelectedButton = null;
            return;
        }

        if (lastSelectedButton != null) {
            lastSelectedButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        button.setTextColor(getResources().getColor(android.R.color.black));
        selectedSlot = time;
        lastSelectedButton = button;
    }

    private void bookSelectedSlot() {
        if (selectedDate == null || selectedSlot == null || specialistName == null) {
            Toast.makeText(this, "Please select a date and slot", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference slotRef = database.child(specialistName).child(selectedDate).child(selectedSlot);
        slotRef.child("status").setValue("pending").addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Slot temporarily reserved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BookingActivity.this, PatientDetailsActivity.class);
            intent.putExtra("specialistName", specialistName);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("selectedSlot", selectedSlot);
            intent.putExtra("visitCharge", visitCharge);
            intent.putExtra("regularCharge", regularCharge);
            intent.putExtra("subcategory", subcategory); // Add this line
            startActivity(intent);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to reserve slot, try again", Toast.LENGTH_SHORT).show();
        });
    }
}