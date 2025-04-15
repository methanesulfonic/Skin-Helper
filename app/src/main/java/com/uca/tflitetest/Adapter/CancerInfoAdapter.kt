package com.uca.tflitetest.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uca.tflitetest.Model.CancerInfo
import com.uca.tflitetest.R
// CancerInfoAdapter.kt
// CancerInfoAdapter.kt
class CancerInfoAdapter(
    private val cancerInfoList: List<CancerInfo>,
    private val onItemClicked: (CancerInfo) -> Unit
) : RecyclerView.Adapter<CancerInfoAdapter.CancerInfoViewHolder>() {

    class CancerInfoViewHolder(view: View, private val onItemClicked: (CancerInfo) -> Unit) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

        fun bind(cancerInfo: CancerInfo) {
            imageView.setImageResource(cancerInfo.imageResId)
            titleTextView.text = cancerInfo.title
            descriptionTextView.text = cancerInfo.description

            itemView.setOnClickListener {
                onItemClicked(cancerInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancerInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cancer_info, parent, false)
        return CancerInfoViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: CancerInfoViewHolder, position: Int) {
        holder.bind(cancerInfoList[position])
    }

    override fun getItemCount(): Int = cancerInfoList.size
}

