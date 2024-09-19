package com.dillonwernich.feedingthefurballs

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Donation_Goal : AppCompatActivity() {

    private lateinit var donationsPieChart: PieChart
    private lateinit var donationsDetails: TextView
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_goal)

        // Initialize UI elements
        donationsPieChart = findViewById(R.id.donations_pieChart)
        donationsDetails = findViewById(R.id.donation_details_textView)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Processing...")
            setCancelable(false)
        }

        // Get current month
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

        // Initialize Firebase Database reference for the current month
        database = FirebaseDatabase.getInstance().reference.child("donation_goals").child(currentMonth)

        // Show the progress dialog while loading data
        progressDialog.show()

        // Load donation data for the current month
        loadDonationData(currentMonth)
    }

    private fun loadDonationData(currentMonth: String) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()

                if (snapshot.exists()) {
                    val monthlyGoal = snapshot.child("monthly_goal").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val monthlyDonations = snapshot.child("monthly_donations").getValue(String::class.java)?.toIntOrNull() ?: 0

                    updateUI(monthlyGoal, monthlyDonations, currentMonth)
                } else {
                    donationsDetails.text = "No data found for $currentMonth"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                donationsDetails.text = "Error fetching data!"
            }
        })
    }

    private fun updateUI(goal: Int, donations: Int, month: String) {
        // Format amounts as South African Rand (ZAR)
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

        // Prepare pie chart entries for donations and remaining goal
        val entries = arrayListOf(
            PieEntry(donations.toFloat(), "Donations"),
            PieEntry((goal - donations).toFloat(), "Remaining")
        )

        // Create PieDataSet and apply custom colors
        val dataSet = PieDataSet(entries, "Donation Goal").apply {
            colors = listOf(
                ContextCompat.getColor(this@Donation_Goal, R.color.dark_purple),  // Custom dark purple color
                ContextCompat.getColor(this@Donation_Goal, R.color.light_purple)   // Custom light purple color
            )
            valueTextSize = 12f
        }

        // Create PieData and set it to the PieChart
        val data = PieData(dataSet)
        donationsPieChart.apply {
            this.data = data
            isDrawHoleEnabled = true
            setHoleColor(android.R.color.transparent)
            setUsePercentValues(true)
            animateY(1000)
            description.isEnabled = false  // Disable the description label
            invalidate()  // Refresh the chart
        }

        // Display formatted amounts in the TextView
        val detailsText = """
        Month: $month
        Monthly Goal: ${currencyFormatter.format(goal)}
        Total Monthly Donations: ${currencyFormatter.format(donations)}
    """.trimIndent()

        donationsDetails.text = detailsText
    }
}
