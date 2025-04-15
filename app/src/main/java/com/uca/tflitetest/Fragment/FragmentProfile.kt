package com.uca.tflitetest.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uca.tflitetest.Activity.LoginActivity
import com.uca.tflitetest.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FragmentProfile : Fragment() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var textFullName: TextView
    private lateinit var textEmail: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var btnLogout: Button
    private lateinit var btnForgotPass: Button
    private lateinit var btnHospitalNearMe: Button
    private lateinit var btnDoctorNearMe: Button
    private lateinit var btnContactOwner: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val firebaseUser = firebaseAuth.currentUser

        textFullName = view.findViewById(R.id.tvFullName)
        textEmail = view.findViewById(R.id.tvEmail)
        ivProfile = view.findViewById(R.id.ivProfile)

        btnLogout = view.findViewById(R.id.btnLogout)
        btnForgotPass = view.findViewById(R.id.btnForgotPass)
        btnDoctorNearMe = view.findViewById(R.id.btnDoctorNearMe)
        btnHospitalNearMe = view.findViewById(R.id.btnHospitalNearMe)
        btnContactOwner = view.findViewById(R.id.btnContactOwner)

        if (firebaseUser != null) {
            // Set display name and email
            textFullName.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email

            // Load profile picture using Glide
            firebaseUser.photoUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.profile_circle_svgrepo_com) // Placeholder image
                    .error(R.drawable.profile_circle_svgrepo_com) // Error image
                    .circleCrop()
                    .into(ivProfile) // ivProfile is your ImageView where you want to display the profile picture
            }

            for (profile in firebaseUser.providerData) {
                if (profile.providerId == GoogleAuthProvider.PROVIDER_ID) {
                    btnForgotPass.visibility = View.GONE
                    break
                }
            }

        } else {
            // User is not signed in, redirect to login screen
            startActivity(Intent(activity, LoginActivity::class.java))
            requireActivity().finish()
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        btnForgotPass.setOnClickListener {
            firebaseUser?.email?.let { email ->
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()

                            // Disable the button
                            btnForgotPass.isEnabled = false
                            btnForgotPass.setBackgroundColor(Color.GRAY)  // Optional: Change the background color to grey

                            // Re-enable the button after 60 seconds
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(60000)  // 60,000 milliseconds = 60 seconds
                                btnForgotPass.isEnabled = true
                                btnForgotPass.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.maintheme))  // Restore the original background color
                            }
                        } else {
                            Toast.makeText(activity, "Failed to send reset email. Try again later.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        btnHospitalNearMe.setOnClickListener {

            findNearbyHospitals()
        }


        btnDoctorNearMe.setOnClickListener {

            findNearbyDermatologists()
        }

        btnContactOwner.setOnClickListener {

            sendEmail2()
        }

        return view
    }

    private fun findNearbyDermatologists() {
        // Create a Uri from an intent string. Open map using intent to search for "Dokter kulit terdekat"
        val mapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=Dokter+kulit+terdekat")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }

    private fun findNearbyHospitals() {
        // Create a Uri from an intent string. Open map using intent to search for "Rumah sakit terdekat"
        val mapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=Rumah+sakit+terdekat")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:methanesulfonic@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Skin Helper Feedback")
            putExtra(Intent.EXTRA_TEXT, "Hi, I'm giving you my feedback regarding the Skin Helper App")
        }
        val packageManager = requireContext().packageManager
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        }
    }

    private fun sendEmail2() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:methanesulfonic@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Skin Helper Feedback")
            putExtra(Intent.EXTRA_TEXT, "Hi, I'm giving you my feedback regarding the Skin Helper App")
        }

        startActivity(Intent.createChooser(emailIntent, "Send Email..."))
    }


}