package com.uca.tflitetest.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.uca.tflitetest.R

class CancerInfoDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancer_info_details)

        val imageView: ImageView = findViewById(R.id.detailImageView)
        val titleTextView: TextView = findViewById(R.id.detailTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.detailDescriptionTextView)
        val buttonOpenWebsite: Button = findViewById(R.id.buttonOpenWebsite)

        val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_launcher_background)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val websiteUrl = intent.getStringExtra("websiteUrl")

        imageView.setImageResource(imageResId)
        titleTextView.text = title
        descriptionTextView.text = description

        buttonOpenWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
    }
}