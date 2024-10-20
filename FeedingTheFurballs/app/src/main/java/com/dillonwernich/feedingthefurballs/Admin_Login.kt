package com.dillonwernich.feedingthefurballs

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class Admin_Login : AppCompatActivity() {

    // UI elements
    private lateinit var employeeEmail: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button

    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // ProgressDialog to show during login
    private lateinit var progressDialog: ProgressDialog

    // onCreate method to initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_admin_login)

        // Initialize UI elements
        employeeEmail = findViewById(R.id.admin_email_address)
        password = findViewById(R.id.admin_password)
        loginBtn = findViewById(R.id.login_button)

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance()

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Logging in...")
            setCancelable(false)  // Prevent user interaction during login process
        }

        // Set the login button click listener
        loginBtn.setOnClickListener {
            val email = employeeEmail.text.toString().trim()
            val userPassword = password.text.toString().trim()

            // Validate email input
            if (email.isEmpty()) {
                employeeEmail.error = "Please enter an email"
                employeeEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate password input
            if (userPassword.isEmpty()) {
                password.error = "Please enter a password"
                password.requestFocus()
                return@setOnClickListener
            }

            // Show the ProgressDialog while attempting login
            progressDialog.show()

            // Attempt to sign in with Firebase Authentication
            auth.signInWithEmailAndPassword(email, userPassword)
                .addOnCompleteListener(this) { task ->
                    // Dismiss the progress dialog after login attempt
                    progressDialog.dismiss()

                    if (task.isSuccessful) {
                        // Successful login, navigate to Admin Dashboard
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Admin_Login, Admin_Dashboard::class.java)
                        startActivity(intent)
                        finish() // Close login activity
                    } else {
                        // Failed login, show error message
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
