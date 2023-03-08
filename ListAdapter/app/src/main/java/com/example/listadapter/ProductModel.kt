package com.example.listadapter

import android.graphics.drawable.Drawable

data class ProductModel(
    val id: Long,
    var thumbnail: Drawable,
    var title: String,
    var price: String,
)
