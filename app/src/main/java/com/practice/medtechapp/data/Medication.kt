package com.practice.medtechapp.data

data class Medication(
    val id: String,
    val name: String,
    val dose: String,
    val time: String,
    val status: String = "pending" // Added with default value of "pending"
)