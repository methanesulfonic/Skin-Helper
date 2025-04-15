package com.uca.tflitetest.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.uca.tflitetest.R

// FragmentCancerInfoDetails.kt
class FragmentCancerInfoDetails : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_RES_ID = "imageResId"
        private const val ARG_LINK = "link"

        @JvmStatic
        fun newInstance(title: String, description: String, imageResId: Int, link: String) =
            FragmentCancerInfoDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DESCRIPTION, description)
                    putInt(ARG_IMAGE_RES_ID, imageResId)
                    putString(ARG_LINK, link)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cancer_info_details, container, false)

        arguments?.let {
            val title = it.getString(ARG_TITLE)
            val description = it.getString(ARG_DESCRIPTION)
            val imageResId = it.getInt(ARG_IMAGE_RES_ID)
            val link = it.getString(ARG_LINK)

            val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val linkTextView: TextView = view.findViewById(R.id.linkTextView)

            titleTextView.text = title
            descriptionTextView.text = description
            imageView.setImageResource(imageResId)
            linkTextView.text = link

            linkTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }

        return view
    }
}
