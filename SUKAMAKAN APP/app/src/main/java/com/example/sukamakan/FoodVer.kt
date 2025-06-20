package com.example.sukamakan

import java.io.Serializable

data class FoodVer(
    val foodImage: Int,
    val foodName: String,
    val foodPrice: String,
    val foodRating: Int,
    val foodRate: String,
    val foodTime: String,
    var cartItemId: String? = null  // Added cartItemId field with default value null
) : Serializable
