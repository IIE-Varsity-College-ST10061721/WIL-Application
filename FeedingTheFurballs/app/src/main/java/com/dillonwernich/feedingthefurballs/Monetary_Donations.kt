package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Monetary_Donations : AppCompatActivity() {

    // Declare buttons for different donation methods
    private lateinit var debitOrderFormButton: Button
    private lateinit var zapperButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monetary_donations)

        // Initialize buttons
        debitOrderFormButton = findViewById(R.id.debit_order_form_button)
        zapperButton = findViewById(R.id.zapper_button)

        // Set up click listeners for each button
        debitOrderFormButton.setOnClickListener {
            openUrl("https://www.feedingthefurballs.org/payment-mandate.php")  // Open the debit order form link
        }

        zapperButton.setOnClickListener {
            openUrl("https://www.zapper.com/payWithZapper/?qr=http%3A%2F%2F2.zap.pe%3Ft%3D8%26i%3D18686%3A16856%3A7%5B34%7C10.00-20.00-50.00-1%7C15%2C61%3A10%5B39%7CZAR%2C38%7CFeeding%20the%20Furballs")  // Open the Zapper payment link
        }
    }

    // Helper function to open a URL in the browser
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)  // Parse the URL
        }
        startActivity(intent)
    }
}
