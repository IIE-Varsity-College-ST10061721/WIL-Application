package com.dillonwernich.feedingthefurballs

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class Splash_Screen : AppCompatActivity() {

    // Duration for the splash screen (in milliseconds)
    private val splashScreenDuration: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Post a delayed task to navigate to MainActivity after the splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an intent to navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)  // Start the MainActivity
            finish()  // Finish the Splash_Screen activity to prevent going back to it
        }, splashScreenDuration)
    }
}
