package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName

data class VersionX(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)