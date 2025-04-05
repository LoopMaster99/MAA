package com.practice.medtechapp.data

data class Medication(
    val id: String,
    val name: String,
    val dosage: String,
    val scheduledTime: String,
    val status: String? = null, // "taken", "missed", or null for pending
    val isOverdue: Boolean = false
)