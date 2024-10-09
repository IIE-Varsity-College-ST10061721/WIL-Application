package com.dillonwernich.feedingthefurballs

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var monthSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_goal)

        // Initialize UI elements
        donationsPieChart = findViewById(R.id.donations_pieChart)
        donationsDetails = findViewById(R.id.donation_details_textView)
        monthSpinner = findViewById(R.id.month_spinner)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading data...")
            setCancelable(false)
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("donation_goals")

        // Populate the spinner with the months from the string array
        val months = resources.getStringArray(R.array.month_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter

        // Auto-select the current month in the spinner
        selectCurrentMonth(months)

        // Set spinner item selected listener
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                loadDonationData(selectedMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }

    // Select the current month in the Spinner
    private fun selectCurrentMonth(months: Array<String>) {
        // Get the current month name (e.g., "October")
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        val monthIndex = months.indexOf(currentMonth)

        if (monthIndex >= 0) {
            // Set the spinner to the current month
            monthSpinner.setSelection(monthIndex)
        }
    }

    // Load donation data for the selected month
    private fun loadDonationData(selectedMonth: String) {
        progressDialog.show()

        database.child(selectedMonth).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()

                if (snapshot.exists()) {
                    val monthlyGoal = snapshot.child("monthly_goal").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val monthlyDonations = snapshot.child("monthly_donations").getValue(String::class.java)?.toIntOrNull() ?: 0

                    updateUI(monthlyGoal, monthlyDonations, selectedMonth)
                } else {
                    // Display message when no data is found for the selected month
                    donationsDetails.text = "No data found for $selectedMonth"
                    donationsPieChart.clear()  // Clear the pie chart if no data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                donationsDetails.text = "Error fetching data!"
            }
        })
    }

    // Update UI with fetched donation data
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
