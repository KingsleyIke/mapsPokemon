package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName

data class Ability(
        @SerializedName("ability")
    val ability: AbilityX,
        @SerializedName("is_hidden")
    val isHidden: Boolean,
        @SerializedName("slot")
    val slot: Int
)