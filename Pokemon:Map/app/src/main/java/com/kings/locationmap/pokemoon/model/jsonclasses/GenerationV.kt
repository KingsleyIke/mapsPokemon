package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName
import com.kings.locationmap.pokemoon.model.jsonclasses.BlackWhite

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)