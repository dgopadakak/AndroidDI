package com.example.androiddi.directions

data class Direction(
    val name: String,
    var listOfTours: ArrayList<Tour> = ArrayList()
)
