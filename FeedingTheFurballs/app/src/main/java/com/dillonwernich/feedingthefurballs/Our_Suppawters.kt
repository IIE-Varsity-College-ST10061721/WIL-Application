package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Our_Suppawters : AppCompatActivity() {

    // Declare UI elements for social media buttons
    private lateinit var facebookButton: ImageButton
    private lateinit var twitterButton: ImageButton
    private lateinit var tiktokButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_our_suppawters)

        // Initialize buttons
        facebookButton = findViewById(R.id.facebook_button)
        twitterButton = findViewById(R.id.twitter_button)
        tiktokButton = findViewById(R.id.tiktok_button)

        // Set up click listener for the Facebook button
        facebookButton.setOnClickListener {
            openUrl("https://www.facebook.com/feedingthefurballs/?fref=ts")  // Open Facebook link
        }

        // Set up click listener for the Twitter button
        twitterButton.setOnClickListener {
            openUrl("https://x.com/i/flow/login?redirect_after_login=%2FFeedTheFurballs")  // Open Twitter link
        }

        // Set up click listener for the TikTok button
        tiktokButton.setOnClickListener {
            openUrl("https://www.tiktok.com/@farrahmaharajh") //Open TikTok link
        }
    }

    // Helper function to open a URL in the browser
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)  // Parse the URL
        }
        startActivity(intent)  // Start the intent to open the URL
    }
}
