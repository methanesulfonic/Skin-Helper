package com.uca.tflitetest.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.uca.tflitetest.R

class RegisterActivity : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var editFullName: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editPasswordConf: EditText
    lateinit var btnRegister: Button
    lateinit var loginText: TextView
    lateinit var progressDialog: ProgressDialog


    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null)
            startActivity(Intent(this, MainActivity::class.java))
    }

    private fun processRegister() {
        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update display name
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = fullName
                    }
                    val user = firebaseAuth.currentUser
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            // Send verification email
                            user.sendEmailVerification()
                                .addOnCompleteListener { emailTask ->
                                    progressDialog.dismiss()
                                    if (emailTask.isSuccessful) {
                                        Toast.makeText(this, "Registration successful. Verification email sent to $email.", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { error ->
                                    progressDialog.dismiss()
                                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { error ->
                            progressDialog.dismiss()
                            Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, task.exception?.localizedMessage ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_register)
        editFullName = findViewById(R.id.edtFullname)
        editEmail = findViewById(R.id.edtEmail)
        editPassword = findViewById(R.id.edtPassword)
        editPasswordConf = findViewById(R.id.edtPasswordConf)
        btnRegister = findViewById(R.id.btnRegister)
        loginText = findViewById(R.id.tvHaveAccount)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Please wait")

        loginText.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener{
            val isFullNameFilled = editFullName.text.isNotEmpty()
            val isEmailFilled = editEmail.text.isNotEmpty()
            val isPasswordFilled = editPassword.text.isNotEmpty()
            val isPasswordConfFilled = editPasswordConf.text.isNotEmpty()
            if (isFullNameFilled && isEmailFilled && isPasswordFilled && isPasswordConfFilled) {
                // Check if passwords match
                if (editPassword.text.toString() == editPasswordConf.text.toString()) {
                    processRegister() // Launch Register
                } else {
                    editPassword.error = "Passwords do not match"
                    editPasswordConf.error = "Passwords do not match"
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Highlight empty fields
                if (!isFullNameFilled) {
                    editFullName.error = "Full name is required"
                }
                if (!isEmailFilled) {
                    editEmail.error = "Email is required"
                }
                if (!isPasswordFilled) {
                    editPassword.error = "Password is required"
                }
                if (!isPasswordConfFilled) {
                    editPasswordConf.error = "Confirm Password is required"
                }
                Toast.makeText(this, "Please fill out all the forms", Toast.LENGTH_SHORT).show()
            }

//            if (editFullName.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty() && editPasswordConf.text.isNotEmpty()){
//                if (editPassword.text.toString() == editPasswordConf.text.toString()){
//                    processRegister() //Launch Register
//
//                }
//                else{
//                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                }
//            }else{
//                Toast.makeText(this, "Please fill out all the forms", Toast.LENGTH_SHORT).show()
//            }
        }

    }
}