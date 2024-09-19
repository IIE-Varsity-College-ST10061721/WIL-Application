package com.dillonwernich.feedingthefurballs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Admin_Dashboard : AppCompatActivity() {

    // Declare buttons for managing gallery, donations, and donation goals
    private lateinit var manageGallery: Button
    private lateinit var manageDonations: Button
    private lateinit var manageDonationGoal: Button

    // Request code for storage permission
    private val READ_MEDIA_IMAGES_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Initialize buttons
        manageGallery = findViewById(R.id.manage_gallery_button)
        manageDonations = findViewById(R.id.manage_item_donations_button)
        manageDonationGoal = findViewById(R.id.manage_monthly_goal_button)

        // Set click listener for the manageGallery button
        manageGallery.setOnClickListener {
            checkStoragePermissionAndOpenGallery()
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

    // Function to check storage permission and open the gallery activity if granted
    private fun checkStoragePermissionAndOpenGallery() {
        if (isStoragePermissionGranted()) {
            // Permission is already granted, open the gallery
            openGallery()
        } else {
            // Permission is not granted, request it
            requestStoragePermission()
        }
    }

    // Function to check if storage permission is granted
    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API level 33)
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For devices below Android 13
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Function to request storage permission
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API level 33)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                READ_MEDIA_IMAGES_REQUEST_CODE
            )
        } else {
            // For devices below Android 13
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_MEDIA_IMAGES_REQUEST_CODE
            )
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_MEDIA_IMAGES_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the gallery
                openGallery()
            } else {
                // Permission denied
                Toast.makeText(
                    this,
                    "Permission denied to access the gallery.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // Function to open the Admin_Gallery activity
    private fun openGallery() {
        val intent = Intent(this, Admin_Gallery::class.java)
        startActivity(intent)
    }
}
