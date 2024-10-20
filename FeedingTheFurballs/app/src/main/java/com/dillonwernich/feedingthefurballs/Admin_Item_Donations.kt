package com.dillonwernich.feedingthefurballs

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.*

class Admin_Item_Donations : AppCompatActivity() {

    // UI elements
    private lateinit var userNameSpinner: Spinner
    private lateinit var itemDonation: EditText
    private lateinit var contactNumber: EditText
    private lateinit var emailAddress: EditText
    private lateinit var deleteRequest: Button

    // Firebase database reference
    private lateinit var database: DatabaseReference

    // Progress dialog for loading indication
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_admin_item_donations)

        // Initialize UI elements
        userNameSpinner = findViewById(R.id.name_spinner)
        itemDonation = findViewById(R.id.item)
        contactNumber = findViewById(R.id.contact_number)
        emailAddress = findViewById(R.id.email_address)
        deleteRequest = findViewById(R.id.delete_item_donation_button)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
        }

        // Fetch user names from Firebase to populate the spinner
        fetchUserNamesFromFirebase()

        // Set delete request click listener
        deleteRequest.setOnClickListener {
            val selectedUserName = userNameSpinner.selectedItem?.toString()
            if (selectedUserName != null) {
                showProgressDialog("Deleting request...")
                deleteRequestFromFirebase(selectedUserName)
            } else {
                Toast.makeText(this, "Please select a user first", Toast.LENGTH_SHORT).show()
            }
        }

        // Set spinner item selection listener to fetch and display user details
        userNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedUserName = parent.getItemAtPosition(position).toString()
                showProgressDialog("Fetching user details...")
                fetchUserDetails(selectedUserName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Fetch user names from Firebase and populate the spinner
    private fun fetchUserNamesFromFirebase() {
        database.child("donations")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userNames = mutableListOf<String>()
                    for (snapshot in dataSnapshot.children) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        name?.let { userNames.add(it) }
                    }
                    // Populate spinner with user names
                    val adapter = ArrayAdapter(this@Admin_Item_Donations, android.R.layout.simple_spinner_item, userNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    userNameSpinner.adapter = adapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@Admin_Item_Donations, "Failed to retrieve user names!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Fetch user details (item donation, contact, email) based on selected user name
    private fun fetchUserDetails(userName: String) {
        database.child("donations")
            .orderByChild("name")
            .equalTo(userName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val selectedItem = snapshot.child("item").getValue(String::class.java)
                            val contact = snapshot.child("contact").getValue(String::class.java)
                            val email = snapshot.child("email").getValue(String::class.java)

                            // Populate EditText fields with fetched details
                            itemDonation.setText(selectedItem)
                            contactNumber.setText(contact)
                            emailAddress.setText(email)
                        }
                    } else {
                        Toast.makeText(this@Admin_Item_Donations, "No data found for user!", Toast.LENGTH_SHORT).show()
                    }
                    hideProgressDialog()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    hideProgressDialog()
                    Toast.makeText(this@Admin_Item_Donations, "Failed to retrieve user details!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Delete donation request for the selected user from Firebase
    private fun deleteRequestFromFirebase(userName: String) {
        database.child("donations")
            .orderByChild("name")
            .equalTo(userName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                            .addOnSuccessListener {
                                hideProgressDialog()
                                Toast.makeText(this@Admin_Item_Donations, "Request deleted successfully!", Toast.LENGTH_SHORT).show()

                                // Clear the input fields after deletion
                                itemDonation.setText("")
                                contactNumber.setText("")
                                emailAddress.setText("")

                                // Refresh the spinner with updated user list
                                fetchUserNamesFromFirebase()
                            }
                            .addOnFailureListener { e ->
                                hideProgressDialog()
                                Toast.makeText(this@Admin_Item_Donations, "Failed to delete request!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    hideProgressDialog()
                    Toast.makeText(this@Admin_Item_Donations, "Failed to delete request!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Show progress dialog with custom message
    private fun showProgressDialog(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    // Hide the progress dialog
    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
