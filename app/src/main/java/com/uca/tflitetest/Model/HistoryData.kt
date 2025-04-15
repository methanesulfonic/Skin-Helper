package com.uca.tflitetest.Model

import com.google.firebase.database.Exclude

data class HistoryData(
    val classValue: String = "",
    val confidenceValue: Float = 0.0f,
    val timestamp: com.google.firebase.Timestamp? = null,
    val imageUrl: String = "",
    @Exclude var documentId: String = "" // Exclude this from Firestore mapping
)

