package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String
)