package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class Contact_Us : AppCompatActivity() {

    // Declare UI elements for social media buttons and contact details
    private lateinit var facebookButton: ImageButton
    private lateinit var twitterButton: ImageButton
    private lateinit var farrahEmail: TextView
    private lateinit var adminEmail: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var address: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Initialize UI elements
        facebookButton = findViewById(R.id.facebook_btn)
        twitterButton = findViewById(R.id.twitter_btn)
        farrahEmail = findViewById(R.id.farrah_email_txt)
        adminEmail = findViewById(R.id.email_details_txt)
        phoneNumber = findViewById(R.id.farrah_number_txt)
        address = findViewById(R.id.address_details_txt)

        // Set up listeners for social media buttons
        facebookButton.setOnClickListener {
            openUrl("https://www.facebook.com/feedingthefurballs/?fref=ts")
        }

        twitterButton.setOnClickListener {
            openUrl("https://x.com/i/flow/login?redirect_after_login=%2FFeedTheFurballs")
        }

        // Set up listeners for emails, phone number, and address clicks
        farrahEmail.setOnClickListener {
            sendEmail("farrah@feedingthefurballs.org")
        }

        adminEmail.setOnClickListener {
            sendEmail("techforgoodgroup@gmail.com")
        }

        phoneNumber.setOnClickListener {
            callPhoneNumber("+27837936897")
        }

        address.setOnClickListener {
            openMap("26 Parthab Road, Avoca, Durban North, 4051")
        }
    }

    // Function to open a URL in the browser
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    // Function to open the email client with a predefined email
    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        startActivity(intent)
    }

    // Function to open the dialer with a predefined phone number
    private fun callPhoneNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    // Function to open a map application with a predefined address
    private fun openMap(address: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
        startActivity(intent)
    }
}
