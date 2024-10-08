package com.dillonwernich.feedingthefurballs

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Admin_Donation_Goal : AppCompatActivity() {

    // Declare UI elements and Firebase database reference
    private lateinit var monthSpinner: Spinner
    private lateinit var totalMonthlyDonations: EditText
    private lateinit var totalMonthlyGoal: EditText
    private lateinit var saveButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_donation_goal)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize UI elements
        monthSpinner = findViewById(R.id.month_spinner)
        totalMonthlyDonations = findViewById(R.id.total_monthly_donations)
        totalMonthlyGoal = findViewById(R.id.total_monthly_goal)
        saveButton = findViewById(R.id.save_button)

        // Populate the Spinner with the months array from resources
        val months = resources.getStringArray(R.array.month_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter

        // Set the current month in the Spinner
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        val monthIndex = months.indexOf(currentMonth)
        if (monthIndex >= 0) {
            monthSpinner.setSelection(monthIndex)
        }

        // Set up the save button click listener
        saveButton.setOnClickListener {
            validateFieldsAndSave()  // Call validateFieldsAndSave method on button click
        }
    }

    // Method to validate individual fields and save donation data to Firebase
    private fun validateFieldsAndSave() {
        val month = monthSpinner.selectedItem.toString()
        val monthlyDonations = totalMonthlyDonations.text.toString()
        val monthlyGoal = totalMonthlyGoal.text.toString()

        // Validate monthly donations field
        if (monthlyDonations.isEmpty()) {
            totalMonthlyDonations.error = "Please enter the total monthly donations!"
            totalMonthlyDonations.requestFocus()
            return
        }

        // Validate monthly goal field
        if (monthlyGoal.isEmpty()) {
            totalMonthlyGoal.error = "Please enter the monthly goal!"
            totalMonthlyGoal.requestFocus()
            return
        }

        // Hide the keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(saveButton.windowToken, 0)

        // If all validations pass, save data to Firebase
        val data = mapOf(
            "month" to month,
            "monthly_donations" to monthlyDonations,
            "monthly_goal" to monthlyGoal
        )

        // Save data under the selected month in Firebase
        database.child("donation_goals").child(month).setValue(data)
            .addOnSuccessListener {
                // Show success message and clear input fields
                Toast.makeText(applicationContext, "Goals uploaded successfully!", Toast.LENGTH_SHORT).show()
                totalMonthlyDonations.text.clear()
                totalMonthlyGoal.text.clear()
            }
            .addOnFailureListener {
                // Show failure message if saving fails
                Toast.makeText(applicationContext, "Failed to upload goals!", Toast.LENGTH_SHORT).show()
            }
    }
}
