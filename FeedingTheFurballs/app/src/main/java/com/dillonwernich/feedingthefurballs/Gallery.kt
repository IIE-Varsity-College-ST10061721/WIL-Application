package com.dillonwernich.feedingthefurballs

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Gallery : AppCompatActivity() {

    // Declare an array of ImageViews for the gallery
    private lateinit var imageViews: Array<ImageView>

    // Firebase storage reference to the "images" folder
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("images")

    // Declare the progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_gallery)

        // Initialize the progress dialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading images...")
            setCancelable(false)
        }

        // Initialize all ImageViews by referencing them from the layout
        imageViews = arrayOf(
            findViewById(R.id.gallery1),
            findViewById(R.id.gallery2),
            findViewById(R.id.gallery3),
            findViewById(R.id.gallery4),
            findViewById(R.id.gallery5),
            findViewById(R.id.gallery6),
            findViewById(R.id.gallery7),
            findViewById(R.id.gallery8),
            findViewById(R.id.gallery9),
            findViewById(R.id.gallery10),
            findViewById(R.id.gallery11),
            findViewById(R.id.gallery12),
            findViewById(R.id.gallery13),
            findViewById(R.id.gallery14),
            findViewById(R.id.gallery15),
            findViewById(R.id.gallery16),
            findViewById(R.id.gallery17),
            findViewById(R.id.gallery18),
            findViewById(R.id.gallery19),
            findViewById(R.id.gallery20),
            findViewById(R.id.gallery21),
            findViewById(R.id.gallery22),
            findViewById(R.id.gallery23),
            findViewById(R.id.gallery24),
            findViewById(R.id.gallery25),
            findViewById(R.id.gallery26),
            findViewById(R.id.gallery27),
            findViewById(R.id.gallery28),
            findViewById(R.id.gallery29),
            findViewById(R.id.gallery30),
            findViewById(R.id.gallery31),
            findViewById(R.id.gallery32),
            findViewById(R.id.gallery33),
            findViewById(R.id.gallery34),
            findViewById(R.id.gallery35),
            findViewById(R.id.gallery36),
            findViewById(R.id.gallery37),
            findViewById(R.id.gallery38),
            findViewById(R.id.gallery39),
            findViewById(R.id.gallery40),
            findViewById(R.id.gallery41),
            findViewById(R.id.gallery42),
            findViewById(R.id.gallery43),
            findViewById(R.id.gallery44),
            findViewById(R.id.gallery45),
            findViewById(R.id.gallery46),
            findViewById(R.id.gallery47),
            findViewById(R.id.gallery48)
        )

        // Show the progress dialog
        progressDialog.show()

        // Load the images from Firebase storage into the ImageViews
        loadImages()

        // Set up click listeners for each ImageView to display images fullscreen
        for (imageView in imageViews) {
            imageView.setOnClickListener {
                showFullScreenImage(imageView)
            }
        }
    }

    // Function to load images from Firebase storage
    private fun loadImages() {
        storageReference.listAll()
            .addOnSuccessListener { listResult ->
                // Sort the images by name in descending order
                val sortedItems = listResult.items.sortedByDescending { it.name }
                // Ensure the number of items does not exceed the number of available ImageViews
                val itemCount = sortedItems.size.coerceAtMost(imageViews.size)

                for (i in 0 until itemCount) {
                    val imageRef = sortedItems[i]
                    val imageView = imageViews[i]

                    // Download the image URL and load it into the ImageView
                    imageRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            // Store the image URL in the ImageView's tag for later use
                            imageView.tag = uri.toString()

                            // Use Glide to load the image into the ImageView
                            Glide.with(this@Gallery)
                                .load(uri)
                                .into(imageView)
                        }
                        .addOnFailureListener {
                            // Handle image loading failure
                        }
                }

                // Dismiss the progress dialog once the images are loaded
                progressDialog.dismiss()
            }
            .addOnFailureListener {
                // Handle Firebase storage listing failure
                progressDialog.dismiss()  // Dismiss the progress dialog in case of failure
            }
    }

    // Function to display an image in fullscreen mode
    private fun showFullScreenImage(imageView: ImageView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_image_fullscreen)

        val fullScreenImageView: ImageView = dialog.findViewById(R.id.fullscreen_image)

        // Retrieve the image URL from the ImageView's tag
        val imageUrl = imageView.tag as? String

        if (imageUrl != null) {
            // Use Glide to load the image into the fullscreen ImageView
            Glide.with(this)
                .load(imageUrl)
                .into(fullScreenImageView)
        } else {
            // Handle the case where the image URL is not available
        }

        // Set the dialog to match the screen size
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.show()
    }
}
