package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dillonwernich.feedingthefurballs.R.layout

// Admin_Dashboard class that controls the admin dashboard activity
class Admin_Dashboard : AppCompatActivity() {

    // Declare buttons for managing gallery, donations, and donation goals
    private lateinit var manageGallery: Button
    private lateinit var manageDonations: Button
    private lateinit var manageDonationGoal: Button

    // onCreate method called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_admin_dashboard) // Set the layout for the activity

        // Initialize buttons by finding them by their ID in the layout
        manageGallery = findViewById(R.id.manage_gallery_button)
        manageDonations = findViewById(R.id.manage_item_donations_button)
        manageDonationGoal = findViewById(R.id.manage_monthly_goal_button)

        // Set click listener for the manageGallery button to open the Admin_Gallery activity
        manageGallery.setOnClickListener {
            val intent = Intent(this, Admin_Gallery::class.java)
            startActivity(intent)
        }

        // Set click listener for the manageDonations button to open the Admin_Item_Donations activity
        manageDonations.setOnClickListener {
            val intent = Intent(this, Admin_Item_Donations::class.java)
            startActivity(intent)
        }

        // Set click listener for the manageDonationGoal button to open the Admin_Donation_Goal activity
        manageDonationGoal.setOnClickListener {
            val intent = Intent(this, Admin_Donation_Goal::class.java)
            startActivity(intent)
        }
    }
}
