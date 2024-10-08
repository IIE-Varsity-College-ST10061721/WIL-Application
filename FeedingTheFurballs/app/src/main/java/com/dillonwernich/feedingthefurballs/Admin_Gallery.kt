package com.dillonwernich.feedingthefurballs

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class Admin_Gallery : AppCompatActivity() {

    // UI elements
    private lateinit var chooseImageBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var deleteBtn: Button
    private lateinit var imageSpinner: Spinner

    // Firebase storage reference
    private lateinit var storageReference: StorageReference

    // Variables for selected image
    private var selectedImageUri: Uri? = null
    private var selectedImageName: String? = null

    // Request code for gallery selection
    private val GALLERY_REQUEST_CODE = 200

    // ProgressDialog for upload/delete actions
    private lateinit var progressDialog: ProgressDialog

    // onCreate method to initialize UI and Firebase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_gallery)

        // Initialize UI elements
        chooseImageBtn = findViewById(R.id.add_image_button)
        imageView = findViewById(R.id.admin_display_imageView)
        deleteBtn = findViewById(R.id.delete_image_button)
        imageSpinner = findViewById(R.id.image_name_spinner)

        // Initialize Firebase storage reference
        storageReference = FirebaseStorage.getInstance().reference

        // Initialize progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Processing...")
            setCancelable(false)
        }

        // Set click listener for selecting an image and uploading it
        chooseImageBtn.setOnClickListener {
            choosePhotoFromGallery()
        }

        // Set click listener for deleting selected image
        deleteBtn.setOnClickListener {
            selectedImageName?.let {
                showProgressDialog("Deleting image...")
                deleteImageFromFirebase(it)
            } ?: Toast.makeText(this, "No image selected to delete!", Toast.LENGTH_SHORT).show()
        }

        // Fetch available image names from Firebase
        fetchImageNamesFromFirebase()

        // Set listener for spinner item selection to display selected image
        imageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedImageName = parent.getItemAtPosition(position).toString()
                displayImageFromFirebase(selectedImageName!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Function to open the gallery and select an image
    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // Handle the result from the gallery selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            data?.data?.let { imageUri ->
                selectedImageUri = imageUri
                imageView.setImageURI(imageUri)

                // Extract filename from URI
                val cursor = contentResolver.query(imageUri, null, null, null, null)
                cursor?.let {
                    if (it.moveToFirst()) {
                        val displayNameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                        selectedImageName = it.getString(displayNameIndex)
                    }
                    it.close()
                }

                // Automatically upload the image after selection
                showProgressDialog("Uploading image...")
                uploadImageToFirebase(imageUri, selectedImageName ?: UUID.randomUUID().toString())
            } ?: Toast.makeText(this, "Failed to get image from gallery!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to upload selected image to Firebase
    private fun uploadImageToFirebase(imageUri: Uri, fileName: String) {
        val ref = storageReference.child("images/$fileName")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                hideProgressDialog()
                Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                fetchImageNamesFromFirebase()
            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Toast.makeText(this, "Failed to upload image!", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to fetch image names from Firebase and populate the spinner
    private fun fetchImageNamesFromFirebase() {
        val imagesRef = storageReference.child("images")
        imagesRef.listAll()
            .addOnSuccessListener { listResult ->
                val imageNames = listResult.items.map { it.name }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, imageNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                imageSpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch image names!", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to display selected image from Firebase
    private fun displayImageFromFirebase(imageName: String) {
        val imageRef = storageReference.child("images/$imageName")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(imageView)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load image!", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to delete selected image from Firebase
    private fun deleteImageFromFirebase(imageName: String) {
        val imageRef = storageReference.child("images/$imageName")
        imageRef.delete()
            .addOnSuccessListener {
                hideProgressDialog()
                Toast.makeText(this, "Image deleted successfully!", Toast.LENGTH_SHORT).show()
                fetchImageNamesFromFirebase()
                imageView.setImageResource(R.drawable.placeholder)  // Reset image view
            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Toast.makeText(this, "Failed to delete image!", Toast.LENGTH_SHORT).show()
            }
    }

    // Helper function to show the progress dialog
    private fun showProgressDialog(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    // Helper function to hide the progress dialog
    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
