package com.uca.tflitetest.fragments

import HistoryAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uca.tflitetest.Model.HistoryData
import com.uca.tflitetest.R

class FragmentHistory : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)

        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter and set it to the RecyclerView
        historyAdapter = HistoryAdapter { documentId -> deleteHistoryItem(documentId) }
        recyclerView.adapter = historyAdapter

        loadHistoryData()
    }

    private fun loadHistoryData() {
        val user = firebaseAuth.currentUser
        if (user != null) {

            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            firestore.collection("users").document(user.uid)
                .collection("CancerData")
                .get()
                .addOnSuccessListener { documents ->
                    val historyList = mutableListOf<HistoryData>()
                    for (document in documents) {
                        val data = document.toObject(HistoryData::class.java).apply {
                            documentId = document.id // Set the document ID
                        }
                        historyList.add(data)
                    }
                    historyAdapter.setData(historyList)
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
        }
    }

    private fun deleteHistoryItem(documentId: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Item")
            builder.setMessage("Are you sure you want to delete this item?")
            builder.setPositiveButton("Yes") { dialog, which ->
                firestore.collection("users").document(user.uid)
                    .collection("CancerData")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                        loadHistoryData() // Refresh the data
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error deleting item: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

}
