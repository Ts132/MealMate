package com.example.mealmate2.models

import com.google.gson.annotations.SerializedName


data class CategoryResponse(
    @SerializedName("categories") val categories: List<Category>
)