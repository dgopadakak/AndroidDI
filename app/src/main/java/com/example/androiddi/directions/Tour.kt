package com.example.androiddi.directions

data class Tour(
    val country: String,
    val duration: String,
    val rate: Int,
    val startDate: String,
    val endDate: String,
    val cost: Int,
    val isAvailable: Int,
    val comment: String
)
