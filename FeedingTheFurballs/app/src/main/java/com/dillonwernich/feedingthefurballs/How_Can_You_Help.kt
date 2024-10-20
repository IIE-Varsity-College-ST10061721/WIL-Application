package com.dillonwernich.feedingthefurballs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class How_Can_You_Help : AppCompatActivity() {

    // Declare buttons for monetary and item donations
    private lateinit var monetaryDonations: Button
    private lateinit var itemDonations: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_how_can_you_help)

        // Initialize buttons
        monetaryDonations = findViewById(R.id.monetary_donations_button)
        itemDonations = findViewById(R.id.item_donations_button)

        // Set up click listener for the monetary donations button
        monetaryDonations.setOnClickListener {
            // Open the Monetary_Donations activity
            val intent = Intent(this, Monetary_Donations::class.java)
            startActivity(intent)
        }

        // Set up click listener for the item donations button
        itemDonations.setOnClickListener {
            // Open the Item_Donations activity
            val intent = Intent(this, Item_Donations::class.java)
            startActivity(intent)
        }
    }
}
