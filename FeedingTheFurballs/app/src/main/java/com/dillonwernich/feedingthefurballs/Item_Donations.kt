package com.dillonwernich.feedingthefurballs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class Item_Donations : AppCompatActivity() {

    // Firebase database reference
    private lateinit var database: DatabaseReference

    // UI elements
    private lateinit var generalItemsSpinner: Spinner
    private lateinit var otherItemsSpinner: Spinner
    private lateinit var nameSurnameField: EditText
    private lateinit var selectedItemField: EditText
    private lateinit var contactNumberField: EditText
    private lateinit var emailAddressField: EditText
    private lateinit var sendRequestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_donations)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize UI elements
        generalItemsSpinner = findViewById(R.id.general_items_spinner)
        otherItemsSpinner = findViewById(R.id.other_items_spinner)
        nameSurnameField = findViewById(R.id.name_and_surname)
        selectedItemField = findViewById(R.id.your_selected_item)
        contactNumberField = findViewById(R.id.contact_number)
        emailAddressField = findViewById(R.id.email_address)
        sendRequestButton = findViewById(R.id.send_request_button)

        // Disable manual input for the selected item field
        selectedItemField.isFocusable = false
        selectedItemField.isClickable = false

        // Set up adapters for spinners
        setupSpinnerAdapters()

        // Set up item selected listeners for spinners
        setupSpinnerListeners()

        // Handle send request button click
        sendRequestButton.setOnClickListener {
            if (validateForm()) {
                saveDataToFirebase()  // Only save to Firebase if validation passes
            }
        }
    }

    // Set up adapters for the general items and other items spinners
    private fun setupSpinnerAdapters() {
        val generalAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.general_items,
            android.R.layout.simple_spinner_item
        )
        generalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        generalItemsSpinner.adapter = generalAdapter

        val otherAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.other_items,
            android.R.layout.simple_spinner_item
        )
        otherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        otherItemsSpinner.adapter = otherAdapter
    }

    // Set up item selected listeners for both spinners
    private fun setupSpinnerListeners() {
        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                selectedItemField.setText(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }

        generalItemsSpinner.onItemSelectedListener = itemSelectedListener
        otherItemsSpinner.onItemSelectedListener = itemSelectedListener
    }

    // Validate the form fields
    private fun validateForm(): Boolean {
        val nameSurname = nameSurnameField.text.toString().trim()
        val selectedItem = selectedItemField.text.toString().trim()
        val contactNumber = contactNumberField.text.toString().trim()
        val emailAddress = emailAddressField.text.toString().trim()

        var isValid = true

        // Check if the name field is empty
        if (nameSurname.isEmpty()) {
            nameSurnameField.error = "Name and surname are required"
            isValid = false
        }

        // Check if an item is selected
        if (selectedItem.isEmpty() || selectedItem == "Please Select an Item") {
            selectedItemField.error = "Please select an item"
            isValid = false
        }

        // Check if the contact number field is empty or invalid
        if (contactNumber.isEmpty()) {
            contactNumberField.error = "Contact number is required"
            isValid = false
        } else if (contactNumber.length != 10) {
            contactNumberField.error = "Invalid contact number"
            isValid = false
        }

        // Check if the email field is empty or invalid
        if (emailAddress.isEmpty()) {
            emailAddressField.error = "Email address is required"
            isValid = false
        } else {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!Pattern.matches(emailPattern, emailAddress)) {
                emailAddressField.error = "Invalid email address"
                isValid = false
            }
        }

        return isValid
    }

    // Save the validated data to Firebase
    private fun saveDataToFirebase() {
        val nameSurname = nameSurnameField.text.toString().trim()
        val selectedItem = selectedItemField.text.toString().trim()
        val contactNumber = contactNumberField.text.toString().trim()
        val emailAddress = emailAddressField.text.toString().trim()

        val donationRequest = DonationRequest(nameSurname, selectedItem, contactNumber, emailAddress)

        val requestId = database.child("donations").push().key
        if (requestId != null) {
            database.child("donations").child(requestId).setValue(donationRequest)
                .addOnSuccessListener {
                    // Show success message only after data is saved successfully
                    Toast.makeText(this, "Request sent successfully!", Toast.LENGTH_SHORT).show()
                    resetFields() // Reset the fields after successful save
                }
                .addOnFailureListener {
                    // Handle any failure in saving data
                    Toast.makeText(this, "Failed to send request!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Reset all fields after the request is sent
    private fun resetFields() {
        nameSurnameField.text.clear()
        selectedItemField.text.clear()
        contactNumberField.text.clear()
        emailAddressField.text.clear()
        generalItemsSpinner.setSelection(0)
        otherItemsSpinner.setSelection(0)
    }

    // Data class to represent a donation request
    data class DonationRequest(
        val name: String,
        val item: String,
        val contact: String,
        val email: String
    )
}
