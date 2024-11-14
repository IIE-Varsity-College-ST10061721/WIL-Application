package com.dillonwernich.feedingthefurballs

<<<<<<< Updated upstream
// Import necessary Android components and Firebase libraries
import android.app.ProgressDialog             // Used to show a loading dialog during data fetch
import android.os.Bundle                       // Bundle is used to pass data between activities
import android.view.View                       // Base class for UI components
import android.widget.AdapterView              // Interface to handle spinner selection events
import android.widget.ArrayAdapter             // Adapter to populate the spinner with data
import android.widget.Spinner                  // Spinner widget to select a month
import android.widget.TextView                 // TextView to display donation details
import androidx.appcompat.app.AppCompatActivity  // Base class for activities using modern Android features
import androidx.appcompat.app.AppCompatDelegate  // Used to control the day/night mode
import androidx.core.content.ContextCompat      // Helper for accessing color resources
import com.github.mikephil.charting.charts.PieChart  // PieChart widget to display donation data graphically
import com.github.mikephil.charting.data.PieData  // Data used in PieChart
import com.github.mikephil.charting.data.PieDataSet  // Data set for the PieChart entries
import com.github.mikephil.charting.data.PieEntry   // Entry in the PieChart representing a slice
import com.google.firebase.database.*             // Firebase Realtime Database for fetching donation data
import java.text.NumberFormat                     // Used for formatting currency values
import java.text.SimpleDateFormat                 // Used to get the current month
import java.util.*                                // Utilities like Locale for currency and date handling

// This class represents the user interface for displaying the monthly donation goal and progress
class Donation_Goal : AppCompatActivity() {

    // Declare UI elements and Firebase database reference
    private lateinit var donationsPieChart: PieChart    // Pie chart to visually represent the donation goal vs progress
    private lateinit var donationsDetails: TextView     // TextView to show detailed donation data
    private lateinit var database: DatabaseReference    // Reference to Firebase Realtime Database
    private lateinit var progressDialog: ProgressDialog // ProgressDialog to show loading indication during data fetch
    private lateinit var monthSpinner: Spinner          // Spinner to select the month for which donation data is shown

    // The onCreate method is called when the activity is first created
    // This is where we initialize the UI components and Firebase, and set up event listeners
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode for consistent visual design across the app
        // The app enforces light mode to maintain visual consistency across all screens
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Set the content view to the corresponding XML layout (activity_donation_goal.xml)
        setContentView(R.layout.activity_donation_goal)

        // Initialize UI elements by finding them in the layout using their respective IDs
        donationsPieChart = findViewById(R.id.donations_pieChart)   // PieChart for displaying donations vs goal
        donationsDetails = findViewById(R.id.donation_details_textView)  // TextView for detailed donation data
        monthSpinner = findViewById(R.id.month_spinner)             // Spinner for selecting the month

        // Initialize ProgressDialog to show during data fetch from Firebase
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading data...")  // Set loading message
            setCancelable(false)           // Prevent user from canceling while data is being loaded
        }

        // Initialize Firebase Database reference to fetch donation data
        database = FirebaseDatabase.getInstance().reference.child("donation_goals")

        // Populate the month spinner with the array of months from resources
        val months = resources.getStringArray(R.array.month_spinner)  // Get the months from strings.xml
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)  // Set up spinner adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter  // Attach the adapter to the Spinner
=======
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
>>>>>>> Stashed changes

        // Auto-select the current month in the spinner
        selectCurrentMonth(months)

<<<<<<< Updated upstream
        // Set listener for the spinner to load donation data when a month is selected
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Load the donation data for the selected month
                val selectedMonth = months[position]  // Get the selected month
                loadDonationData(selectedMonth)       // Load data from Firebase for the selected month
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action is needed if nothing is selected
=======
        // Set spinner item selected listener
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                loadDonationData(selectedMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
>>>>>>> Stashed changes
            }
        }
    }

<<<<<<< Updated upstream
    // Select the current month in the Spinner based on the system date
    private fun selectCurrentMonth(months: Array<String>) {
        // Get the current month name (e.g., "October") using the system's date and time
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        val monthIndex = months.indexOf(currentMonth)  // Find the index of the current month in the array

        if (monthIndex >= 0) {
            // Set the spinner to the current month if it exists in the array
=======
    // Select the current month in the Spinner
    private fun selectCurrentMonth(months: Array<String>) {
        // Get the current month name (e.g., "October")
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        val monthIndex = months.indexOf(currentMonth)

        if (monthIndex >= 0) {
            // Set the spinner to the current month
>>>>>>> Stashed changes
            monthSpinner.setSelection(monthIndex)
        }
    }

<<<<<<< Updated upstream
    // Load donation data from Firebase for the selected month
    private fun loadDonationData(selectedMonth: String) {
        progressDialog.show()  // Show the loading dialog while data is being fetched

        // Query Firebase for the donation goal and donation progress for the selected month
        database.child(selectedMonth).addListenerForSingleValueEvent(object : ValueEventListener {
            // Called when data is successfully fetched from Firebase
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()  // Dismiss the loading dialog once data is retrieved

                if (snapshot.exists()) {
                    // Extract the donation goal and the amount donated so far
                    val monthlyGoal = snapshot.child("monthly_goal").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val monthlyDonations = snapshot.child("monthly_donations").getValue(String::class.java)?.toIntOrNull() ?: 0

                    // Update the UI with the fetched donation data
                    updateUI(monthlyGoal, monthlyDonations, selectedMonth)
                } else {
                    // Display a message when no data is found for the selected month
                    donationsDetails.text = "No data found for $selectedMonth"
                    donationsPieChart.clear()  // Clear the pie chart if no data is found
                }
            }

            // Called if there is an error while fetching data from Firebase
            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()  // Dismiss the loading dialog even if there is an error
                donationsDetails.text = "Error fetching data!"  // Show error message in the TextView
=======
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
>>>>>>> Stashed changes
            }
        })
    }

<<<<<<< Updated upstream
    // Update the UI with the fetched donation data
    private fun updateUI(goal: Int, donations: Int, month: String) {
        // Format the amounts as South African Rand (ZAR)
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))  // Set up currency formatter

        // Prepare PieChart entries for donations and the remaining goal
        val entries = arrayListOf(
            PieEntry(donations.toFloat(), "Donations"),          // Create entry for donations received
            PieEntry((goal - donations).toFloat(), "Remaining")  // Create entry for the remaining goal
        )

        // Create PieDataSet and apply custom colors to the chart
        val dataSet = PieDataSet(entries, "Donation Goal").apply {
            // Set custom colors for the pie chart slices
            colors = listOf(
                ContextCompat.getColor(this@Donation_Goal, R.color.dark_purple),  // Dark purple for donations
                ContextCompat.getColor(this@Donation_Goal, R.color.light_purple)  // Light purple for remaining goal
            )
            valueTextSize = 12f  // Set the size of the text displayed on the pie chart
=======
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
>>>>>>> Stashed changes
        }

        // Create PieData and set it to the PieChart
        val data = PieData(dataSet)
        donationsPieChart.apply {
<<<<<<< Updated upstream
            this.data = data  // Set the pie chart data
            isDrawHoleEnabled = true  // Enable the hole in the center of the chart
            setHoleColor(android.R.color.transparent)  // Set the hole color to be transparent
            setUsePercentValues(true)  // Display values as percentages
            animateY(1000)  // Animate the chart when it loads
            description.isEnabled = false  // Disable the default description label
            invalidate()  // Refresh the chart to display the updated data
        }

        // Display the donation details in the TextView
=======
            this.data = data
            isDrawHoleEnabled = true
            setHoleColor(android.R.color.transparent)
            setUsePercentValues(true)
            animateY(1000)
            description.isEnabled = false  // Disable the description label
            invalidate()  // Refresh the chart
        }

        // Display formatted amounts in the TextView
>>>>>>> Stashed changes
        val detailsText = """
        Month: $month
        Monthly Goal: ${currencyFormatter.format(goal)}
        Total Monthly Donations: ${currencyFormatter.format(donations)}
<<<<<<< Updated upstream
        """.trimIndent()  // Format the text to show the month, goal, and donations

        donationsDetails.text = detailsText  // Set the formatted text to the TextView
=======
        """.trimIndent()

        donationsDetails.text = detailsText
>>>>>>> Stashed changes
    }
}
