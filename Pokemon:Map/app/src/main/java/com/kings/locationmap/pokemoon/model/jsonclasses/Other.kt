package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName
import com.kings.locationmap.pokemoon.model.jsonclasses.DreamWorld
import com.kings.locationmap.pokemoon.model.jsonclasses.OfficialArtwork

data class Other(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)