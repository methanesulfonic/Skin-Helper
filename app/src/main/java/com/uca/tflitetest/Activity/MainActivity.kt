package com.uca.tflitetest.Activity

import com.uca.tflitetest.Fragment.FragmentCancerInfo
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uca.tflitetest.Fragment.FragmentProfile
import com.uca.tflitetest.R
import com.uca.tflitetest.fragments.FragmentDashboard
import com.uca.tflitetest.fragments.FragmentHistory

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        }

        setContentView(R.layout.activity_main)

        loadFragment(FragmentDashboard()) // Load the home fragment initially

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    loadFragment(FragmentDashboard())
                    true
                }
                R.id.historyMenu -> {
                    loadFragment(FragmentHistory())
                    true
                }
                R.id.learnMenu -> {
                    loadFragment(FragmentCancerInfo())
                    true
                }
                R.id.profileMenu -> {
                    loadFragment(FragmentProfile())
                    true
                }
                else -> false
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }


}
