package com.uca.tflitetest.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.GoogleAuthProvider
import com.uca.tflitetest.R

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 1000
    }

    private var firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnGoogle: Button
    private lateinit var registerText2: TextView
    private lateinit var forgotPassText: TextView
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) startActivity(Intent(this, MainActivity::class.java))
    }

    private fun processLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val user = firebaseAuth.currentUser
            if (user != null && user.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut() // Sign out the user since their email is not verified
            }
        }.addOnFailureListener { error ->
            progressDialog.dismiss()
            when (error) {
                is FirebaseAuthInvalidUserException -> {
                    Toast.makeText(this, "Account Not Found", Toast.LENGTH_SHORT).show()
                }

                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
                }

                is FirebaseAuthEmailException -> {
                    Toast.makeText(this, "Incorrect email", Toast.LENGTH_SHORT).show()
                }

                is FirebaseAuthRecentLoginRequiredException -> {
                    Toast.makeText(this, "Recent login required", Toast.LENGTH_SHORT).show()
                    // Implement re-authentication flow if necessary
                }

                is FirebaseNetworkException -> {
                    Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
                }

                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(this, "Server Busy, Try Again", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Handle other errors
                    Toast.makeText(
                        this, "Authentication failed: ${error.localizedMessage}", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }.addOnCompleteListener {
            progressDialog.dismiss()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        editEmail = findViewById(R.id.edtEmail)
        editPassword = findViewById(R.id.edtPassword)
        registerText2 = findViewById(R.id.tvToRegister)
        forgotPassText = findViewById(R.id.tvForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogle = findViewById(R.id.btnLoginGoogle)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in ...")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        btnLogin.setOnClickListener {
            if (editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()) {
                //login berhasil
                processLogin()
            } else if (editEmail.text.isEmpty()) {
                editEmail.error = "Write an email"
                Toast.makeText(this, "Please fill out the email form", Toast.LENGTH_SHORT).show()
            } else if (editPassword.text.isEmpty()) {
//                editPassword.error = "Write a password"
                editPassword.setError("Write a password", null)
                Toast.makeText(this, "Please fill out the password form", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this, "Please fill out the email and password form", Toast.LENGTH_SHORT
                ).show()
            }
        }

        registerText2.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassText.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        progressDialog.show()
        val credetial = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credetial).addOnSuccessListener {
            startActivity(Intent(this, MainActivity::class.java))
        }.addOnFailureListener { error ->
            progressDialog.dismiss()
            when (error) {
                is FirebaseNetworkException -> {
                    Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
                }

                is FirebaseTooManyRequestsException -> {
                    Toast.makeText(this, "Server Busy, Try Again", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Handle other errors
                    Toast.makeText(
                        this, "Authentication failed: ${error.localizedMessage}", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }.addOnCompleteListener {
            progressDialog.dismiss()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            //menangani login google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //jika berhasil
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }
}