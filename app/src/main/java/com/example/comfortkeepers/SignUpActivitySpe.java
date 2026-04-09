package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivitySpe extends AppCompatActivity {

    private Button submitButton;
    private EditText password1,confirmpassword1;
    ImageView passwordToggle,confirmPasswordToggle;
    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible1 = false;
    public EditText email1;
    private EditText firstName;
    private EditText middleName;
    private EditText lastName;
    private EditText contactNumber;
    private EditText address;
    private EditText pincode1;
    private EditText dob1;
    private EditText visit_charge;
    private EditText regular_charge;
    private RadioGroup rgGender;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, databaseReference1;
    private AutoCompleteTextView stateDropdown, cityDropdown;
    private String selectedState, selectedCity;
    private AutoCompleteTextView categoryDropdown, subcategoryDropdown;
    private String selectedCategory, selectedSubcategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_spe);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            submitButton=findViewById(R.id.submitButton);
            passwordToggle = findViewById(R.id.passwordToggle);
            password1 = findViewById(R.id.password);
            confirmpassword1= findViewById(R.id.confirmPassword);
            confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle);
            email1=findViewById(R.id.email);
            firstName=findViewById(R.id.firstName);
            middleName=findViewById(R.id.middleName);
            lastName=findViewById(R.id.lastName);
            contactNumber=findViewById(R.id.contactNumber);
            address=findViewById(R.id.address);
            pincode1=findViewById(R.id.pincode);
            dob1=findViewById(R.id.dob);
            visit_charge=findViewById(R.id.visit_charge);
            regular_charge=findViewById(R.id.regular_charge);
            rgGender = findViewById(R.id.genderGroup);
            categoryDropdown = findViewById(R.id.categoryDropdown);
            subcategoryDropdown = findViewById(R.id.subcategoryDropdown);
            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("Specialists");
            stateDropdown = findViewById(R.id.stateDropdown);
            cityDropdown = findViewById(R.id.cityDropdown);

            databaseReference = FirebaseDatabase.getInstance().getReference("States");
            databaseReference1 = FirebaseDatabase.getInstance().getReference("categories");


            // Load states from Firebase
            loadStates();

            // Handle state selection and load corresponding cities
            stateDropdown.setOnItemClickListener((parent, view, position, id) -> {
                selectedState = (String) parent.getItemAtPosition(position);
                loadCities(selectedState);
            });

            // Handle city selection
            cityDropdown.setOnItemClickListener((parent, view, position, id) -> {
                selectedCity = (String) parent.getItemAtPosition(position);
            });

            loadCategories();

            // Handle category selection and load corresponding subcategories
            categoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
                selectedCategory = (String) parent.getItemAtPosition(position);
                loadSubcategories(selectedCategory);
            });

            // Handle subcategory selection
            subcategoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
                selectedSubcategory = (String) parent.getItemAtPosition(position);
            });




            passwordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible) {
                    // Hide password
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                password1.setSelection(password1.length()); // Move cursor to end
            });

            confirmPasswordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible1) {
                    // Hide password
                    confirmpassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    confirmpassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible1 = !isPasswordVisible1;
                confirmpassword1.setSelection(confirmpassword1.length()); // Move cursor to end
            });

            submitButton.setOnClickListener(view -> {
                String email = email1.getText().toString().trim();
                String password = password1.getText().toString().trim();
                String confirmPassword = confirmpassword1.getText().toString().trim();
                String contact = contactNumber.getText().toString().trim();
                String pincode = pincode1.getText().toString().trim();
                String dob = dob1.getText().toString().trim();
                String addressStr = address.getText().toString().trim();
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                String gender = "";
                if (selectedGenderId == R.id.male) gender = "Male";
                if (selectedGenderId == R.id.female) gender = "Female";
                if (selectedGenderId == R.id.other) gender = "Other";

                if (TextUtils.isEmpty(email)) {
                    email1.setError("Email is required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email1.setError("Invalid email format");
                    return;

                }



                if (password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
                    password1.setError("Password must be at least 8 characters, with at least one special character and number");
                    return;

                }


                if (!confirmPassword.equals(password)) {
                    confirmpassword1.setError("Passwords do not match");
                    return;

                }
                // Validate First, Middle, and Last Name
                if (!validateName(firstName, "First Name"))
                {
                    return;
                }


                if (!validateName(middleName, "Middle Name"))
                {
                    return;
                }

                if (!validateName(lastName, "Last Name"))
                {
                    return;
                }


                if (!contact.matches("\\d{10}")) {
                    contactNumber.setError("Enter a valid 10-digit contact number");
                    return;

                }

                if(selectedState == null) {
                    stateDropdown.setError("Please select a state");
                    return;

                }

                if(selectedCity == null) {
                    cityDropdown.setError("Please select a city");
                    return;

                }


                if (selectedCategory == null) {
                    categoryDropdown.setError("Please select a category");
                    return;

                }

                if (selectedSubcategory == null) {
                    subcategoryDropdown.setError("Please select a subcategory");
                    return;

                }

                if (addressStr.isEmpty()) {
                    address.setError("Address is required");
                    return;


                }

                if (!pincode.matches("\\d{6}")) {
                    pincode1.setError("Enter a valid 6-digit pincode");
                    return;


                }
                // Validate Gender Selection
                if (rgGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SignUpActivitySpe.this, "Please select a Gender", Toast.LENGTH_SHORT).show();
                    return;

                }

                // Validate Date of Birth

                if (!isValidDOB(dob))
                {
                    return;
                }


                // Validate Visit Charges & Regular Charges (Numeric)
                if (!validateNumeric(visit_charge, "Visit Charges"))
                {
                    return;
                }

                if (!validateNumeric(regular_charge, "Regular Charges"))
                {
                    return;
                }


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (firebaseUser != null) {
                                        saveSpecialistData();
                                    }
                                } else {
                                    Toast.makeText(SignUpActivitySpe.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
            });

            return insets;
        });
    }

    private void loadStates() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> stateList = new ArrayList<>();
                for (DataSnapshot stateSnapshot : snapshot.getChildren()) {
                    stateList.add(stateSnapshot.getKey()); // Get state name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivitySpe.this, android.R.layout.simple_dropdown_item_1line, stateList);
                stateDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivitySpe.this, "Failed to load states", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCities(String state) {
        databaseReference.child(state).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> cityList = new ArrayList<>();
                for (DataSnapshot citySnapshot : snapshot.getChildren()) {
                    cityList.add(citySnapshot.getValue(String.class)); // Get city name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivitySpe.this, android.R.layout.simple_dropdown_item_1line, cityList);
                cityDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivitySpe.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategories() {
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> categoryList = new ArrayList<>();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    categoryList.add(categorySnapshot.getKey()); // Get category name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivitySpe.this, android.R.layout.simple_dropdown_item_1line, categoryList);
                categoryDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivitySpe.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSubcategories(String category) {
        databaseReference1.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> subcategoryList = new ArrayList<>();
                for (DataSnapshot subcategorySnapshot : snapshot.getChildren()) {
                    subcategoryList.add(subcategorySnapshot.getValue(String.class)); // Get subcategory name
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivitySpe.this, android.R.layout.simple_dropdown_item_1line, subcategoryList);
                subcategoryDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpActivitySpe.this, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Method to extract file name
    private boolean validateName(EditText editText, String fieldName) {
        String name = editText.getText().toString().trim();
        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z]+")) {
            editText.setError(fieldName + " must contain only alphabets");
            return false;
        }
        return true;
    }

    private boolean validateNumeric(EditText editText, String fieldName) {
        String value = editText.getText().toString().trim();
        if (!value.matches("\\d+")) {
            editText.setError(fieldName + " must be numeric only");
            return false;
        }
        return true;
    }

    public boolean isValidDOB(String dobString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            sdf.setLenient(false); // Ensures strict date validation

            // Parse the input date
            Date dob = sdf.parse(dobString);

            // Get calendar instance for the input date
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(dob);

            // Get the year from the input DOB
            int year = dobCal.get(Calendar.YEAR);

            // Set min and max year limits
            int minYear = 1900;
            int maxYear = Calendar.getInstance().get(Calendar.YEAR);

            // Check if the year is within the allowed range
            if (year < minYear || year > maxYear) {
                return false; // Invalid year
            }

            // Get today's date to ensure DOB is not a future date
            Calendar today = Calendar.getInstance();
            if (dobCal.after(today)) {
                return false; // DOB cannot be in the future
            }

            return true;
        }
        catch (Exception e) {
            dob1.setError("Enter valid date");
            return false;// Invalid date format
        }
    }

    private void saveSpecialistData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Specialists");

        String entryId = databaseReference.push().getKey(); // Generate a unique ID

        HashMap<String, Object> specialistData = new HashMap<>();
        specialistData.put("firstName", firstName.getText().toString().trim());
        specialistData.put("middleName", middleName.getText().toString().trim());
        specialistData.put("lastName", lastName.getText().toString().trim());
        specialistData.put("email", email1.getText().toString().trim());
        specialistData.put("state", selectedState);
        specialistData.put("city", selectedCity);
        specialistData.put("contactNumber", contactNumber.getText().toString().trim());
        specialistData.put("address", address.getText().toString().trim());
        specialistData.put("pincode", pincode1.getText().toString().trim());
        specialistData.put("dob", dob1.getText().toString().trim());
        specialistData.put("gender", rgGender.getCheckedRadioButtonId() == R.id.male ? "Male" : "Female");
        specialistData.put("category", selectedCategory);
        specialistData.put("subcategory", selectedSubcategory);
        specialistData.put("visit_charge", visit_charge.getText().toString().trim());
        specialistData.put("regular_charge", regular_charge.getText().toString().trim());
        specialistData.put("role", "Specialists");
        databaseReference.child(entryId).setValue(specialistData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUpActivitySpe.this, "Registration Successful! Please Login.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivitySpe.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivitySpe.this, "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}