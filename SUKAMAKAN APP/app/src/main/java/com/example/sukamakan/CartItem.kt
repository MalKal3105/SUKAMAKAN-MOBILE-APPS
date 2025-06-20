package com.example.sukamakan

data class CartItem(
    val cartID: String?,
    val productImage: Int,
    val productName: String,
    val productName2: String,
    val productName3: String,
    var quantity: Int,
    val quantity2: Int,
    val quantity3: Int,
    val productPrice: Double,
    val timeZone: String?
) {
    constructor() : this("", 0, "", "","",0, 0,0,0.0,"")
}

