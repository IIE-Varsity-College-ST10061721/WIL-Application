package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // UI element for navigation spinner
    private lateinit var selectSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the spinner
        selectSpinner = findViewById(R.id.navigation_spinner)

        // Set up the adapter with the spinner items from resources
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,  // Resource array for spinner items
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectSpinner.adapter = adapter

        // Set the listener for item selection
        selectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    1 -> navigateToActivity(How_Can_You_Help::class.java)  // Navigate to "How Can You Help"
                    2 -> navigateToActivity(Our_Suppawters::class.java)    // Navigate to "Our Suppawters"
                    3 -> navigateToActivity(Gallery::class.java)           // Navigate to "Gallery"
                    4 -> navigateToActivity(Donation_Goal::class.java)     // Navigate to "Donation Goal"
                    5 -> navigateToActivity(Contact_Us::class.java)        // Navigate to "Contact Us"
                    6 -> openBrowserWithUrl("https://popia.co.za/")        // Open a URL in the browser
                    7 -> navigateToActivity(Admin_Login::class.java)       // Navigate to "Admin Login"
                    else -> Log.d("MainActivity", "No valid selection made!")  // Log for no valid selection
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed when nothing is selected
            }
        }
    }

    // Helper function to navigate to another activity
    private fun <T> navigateToActivity(activityClass: Class<T>) {
        try {
            // Create and start an intent for the specified activity
            val intent = Intent(this@MainActivity, activityClass)
            startActivity(intent)
        } catch (e: Exception) {
            // Handle any exceptions during navigation
            Toast.makeText(this, "Failed to open activity!", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper function to open a URL in the browser
    private fun openBrowserWithUrl(url: String) {
        try {
            // Create and start an intent to open the URL in a browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (e: Exception) {
            // Handle any exceptions during URL opening
            Toast.makeText(this, "Failed to open URL!", Toast.LENGTH_SHORT).show()
        }
    }

    // Reset spinner selection to the first item when the activity resumes
    override fun onResume() {
        super.onResume()
        selectSpinner.setSelection(0)  // Reset to the default position
    }

    // Handle the back button press to reset the spinner if an item is selected
    override fun onBackPressed() {
        if (selectSpinner.selectedItemPosition > 0) {
            selectSpinner.setSelection(0)  // Reset the spinner if an item is selected
        } else {
            super.onBackPressed()  // Perform the default back action
        }
    }
}
