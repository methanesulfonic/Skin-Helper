package com.uca.tflitetest.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.uca.tflitetest.Activity.LoginActivity
import com.uca.tflitetest.R
import com.uca.tflitetest.TFLiteClassifier
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

//the saved work dumbass
class FragmentDashboard : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var cancerImage: ImageView
    private lateinit var ivProfile: ImageView
    private lateinit var textViewClass: TextView
    private lateinit var textViewClassValue: TextView
    private lateinit var textViewConfidenceValue: TextView
    private lateinit var tfliteClassifier: TFLiteClassifier
    private lateinit var tvFullName: TextView
    private lateinit var buttonSave: Button


    private var selectedImageUri: Uri? = null

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val IMAGE_PICK_CODE = 1000
    private val IMAGE_CROP_CODE = 2000
    private var croppedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancerImage = view.findViewById(R.id.ivCancerImage)
        ivProfile = view.findViewById(R.id.ivProfileImage)
        textViewClass = view.findViewById(R.id.textViewClass)
        textViewClassValue = view.findViewById(R.id.textViewClassValue)
        textViewConfidenceValue = view.findViewById(R.id.textViewConfidenceValue)
        val buttonUpload: Button = view.findViewById(R.id.buttonUpload)
        tvFullName = view.findViewById(R.id.tvFullName)
        val firebaseUser = firebaseAuth.currentUser
        buttonSave = view.findViewById(R.id.buttonSave)
        progressBar = view.findViewById(R.id.progressBar)
        tfliteClassifier = TFLiteClassifier(requireContext())

        if (firebaseUser != null) {
            // Set display name and email
            tvFullName.text = firebaseUser.displayName

            // Load profile picture using Glide
            firebaseUser.photoUrl?.let { url ->
                Glide.with(this).load(url)
                    .placeholder(R.drawable.profile_circle_svgrepo_com) // Placeholder image
                    .error(R.drawable.profile_circle_svgrepo_com) // Error image
                    .circleCrop()
                    .into(ivProfile) // ivProfile is your ImageView where you want to display the profile picture
            }

        } else {
            // User is not signed in, redirect to login screen
            startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finish()
        }

        buttonSave.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadImageAndSaveData(uri)
            } ?: Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }


        buttonUpload.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    data?.data?.let { uri ->
                        startCrop(uri)
                    }
                }

                IMAGE_CROP_CODE -> {
                    data?.let {
                        val resultUri = UCrop.getOutput(it)
                        resultUri?.let { uri ->
                            croppedImageUri = uri
                            handleSelectedImage(uri)
                        }
                    }
                }
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri =
            Uri.fromFile(File(requireContext().cacheDir, UUID.randomUUID().toString() + ".jpg"))
        val options = UCrop.Options().apply {
            setCompressionQuality(80)
            setHideBottomControls(true)
            setFreeStyleCropEnabled(true)
            setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
        }
        UCrop.of(uri, destinationUri).withOptions(options).withAspectRatio(1f, 1f)
            .start(requireContext(), this, IMAGE_CROP_CODE)
    }

    private fun handleSelectedImage(uri: Uri) {
        try {
            selectedImageUri = uri // Store the selected image URI
            cancerImage.setImageURI(uri)
            val imageStream = requireContext().contentResolver.openInputStream(uri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val resizedImage = Bitmap.createScaledBitmap(selectedImage, 150, 150, false)
            val imageByteBuffer = convertBitmapToByteBuffer(resizedImage)
            val (className, confidence) = tfliteClassifier.classify(imageByteBuffer)
            textViewClassValue.text = className
            textViewConfidenceValue.text = "${confidence * 100}%"
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 150 * 150 * 3) // 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(150 * 150)
        bitmap.getPixels(intValues, 0, 150, 0, 0, 150, 150)
        intValues.forEach { value ->
            byteBuffer.putFloat(((value shr 16) and 0xFF) / 255.0f)
            byteBuffer.putFloat(((value shr 8) and 0xFF) / 255.0f)
            byteBuffer.putFloat((value and 0xFF) / 255.0f)
        }
        return byteBuffer
    }

    private fun uploadImageAndSaveData(uri: Uri) {

        val progressDialog = ProgressDialog(context).apply {
            setMessage("Uploading image...")
            setCancelable(false)
            show()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${user?.uid}/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->

            progressDialog.dismiss()

            if (task.isSuccessful) {
                val downloadUri = task.result
                saveCancerData(downloadUri.toString())
            } else {
                progressDialog.dismiss()
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCancerData(imageUrl: String) {

        val progressDialog = ProgressDialog(context).apply {
            setMessage("Saving data...")
            setCancelable(false)
            show()
        }


        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        if (user != null) {
            val classValue = textViewClassValue.text.toString()
            val confidenceValue =
                textViewConfidenceValue.text.toString().removeSuffix("%").toFloat()

            // Use FieldValue.serverTimestamp() to get server-generated timestamp
            val timestamp = FieldValue.serverTimestamp()

            val userCancerData = hashMapOf(
                "classValue" to classValue,
                "confidenceValue" to confidenceValue,
                "timestamp" to timestamp,  // Store timestamp as a Firestore Timestamp
                "imageUrl" to imageUrl
            )

//            progressBar.visibility = View.VISIBLE

            db.collection("users").document(user.uid).collection("CancerData").add(userCancerData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
//                    progressBar.visibility = View.GONE
                    progressDialog.dismiss()
                }.addOnFailureListener { e ->
                    Toast.makeText(context, "Error saving data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
//                    progressBar.visibility = View.GONE
                    progressDialog.dismiss()
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
