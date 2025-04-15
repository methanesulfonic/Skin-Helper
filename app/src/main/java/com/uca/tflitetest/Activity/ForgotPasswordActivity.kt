package com.uca.tflitetest.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.uca.tflitetest.R


class ForgotPasswordActivity : AppCompatActivity() {

    var firebaseAuth = FirebaseAuth.getInstance()

    lateinit var edtEmail: EditText
    lateinit var btnForgot: Button
    lateinit var registerText: TextView
    lateinit var loginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val firebaseAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edtEmailForgot)
        btnForgot = findViewById(R.id.btnSendForgotPassword)
        registerText = findViewById(R.id.tvDontHaveAccount)
        loginText = findViewById(R.id.tvToLogin)

        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnForgot.setOnClickListener {
            if (edtEmail.text.isNotEmpty()) {
                //process lupa pass berhasil
                processForgot()
            } else {
                Toast.makeText(
                    this, "Silahkan isi email terlebih dahulu", Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun processForgot() {

        val email = edtEmail.text.toString()

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Check email to reset password", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this, "Failed to reset password", Toast.LENGTH_SHORT).show()
            }
        }
    }

}