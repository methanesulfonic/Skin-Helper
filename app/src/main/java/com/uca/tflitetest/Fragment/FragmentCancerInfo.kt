package com.uca.tflitetest.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uca.tflitetest.Activity.CancerInfoDetailsActivity
import com.uca.tflitetest.Adapter.CancerInfoAdapter
import com.uca.tflitetest.Model.CancerInfo
import com.uca.tflitetest.R
// FragmentCancerInfo.kt
// FragmentCancerInfo.kt
class FragmentCancerInfo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cancerInfoAdapter: CancerInfoAdapter

    private lateinit var cancerInfoList: List<CancerInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cancer_info, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCancerInfo)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the list using string resources
        context?.let {
            cancerInfoList = listOf(
                CancerInfo(
                    R.drawable.actinic_keratoses,
                    it.getString(R.string.Actinic_keratosis),
                    it.getString(R.string.Actinic_keratosis_description),
                    it.getString(R.string.Actinic_keratosis_description)
                ),
                CancerInfo(
                    R.drawable.basal_cell_carcinoma,
                    it.getString(R.string.Basal_Cell_Carcinoma),
                    it.getString(R.string.Basal_Cell_Carcinoma_description),
                    it.getString(R.string.Basal_Cell_Carcinoma_link)
                ),
                CancerInfo(
                    R.drawable.benign_keratosis_like_lesions,
                    it.getString(R.string.Seborrheic_Keratosis),
                    it.getString(R.string.Seborrheic_Keratosis_description),
                    it.getString(R.string.Seborrheic_Keratosis_link)
                ),
                CancerInfo(
                    R.drawable.dermatofibroma,
                    it.getString(R.string.Dermatofibroma),
                    it.getString(R.string.Dermatofibroma_description),
                    it.getString(R.string.Dermatofibroma_link)
                ),
                CancerInfo(
                    R.drawable.melanoma,
                    it.getString(R.string.Melanoma),
                    it.getString(R.string.Melanoma_description),
                    it.getString(R.string.Melanoma_link)
                ),
                CancerInfo(
                    R.drawable.melanocytic_nevi,
                    it.getString(R.string.Melanocytic_Nevi),
                    it.getString(R.string.Melanocytic_Nevi_description),
                    it.getString(R.string.Melanocytic_Nevi_link)
                ),
                CancerInfo(
                    R.drawable.vascular_lesions,
                    it.getString(R.string.Vascular_lesions),
                    it.getString(R.string.Vascular_lesions_description),
                    it.getString(R.string.Vascular_lesions_link)
                )
            )
        }

        cancerInfoAdapter = CancerInfoAdapter(cancerInfoList) { cancerInfo ->
            val intent = Intent(context, CancerInfoDetailsActivity::class.java).apply {
                putExtra("imageResId", cancerInfo.imageResId)
                putExtra("title", cancerInfo.title)
                putExtra("description", cancerInfo.description)
                putExtra("websiteUrl", cancerInfo.websiteUrl)
            }
            startActivity(intent)
        }
        recyclerView.adapter = cancerInfoAdapter

        return view
    }
}

