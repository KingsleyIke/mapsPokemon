package com.kings.locationmap.pokemoon.model.jsonclasses


import com.google.gson.annotations.SerializedName
import com.kings.locationmap.pokemoon.model.jsonclasses.MoveLearnMethod
import com.kings.locationmap.pokemoon.model.jsonclasses.VersionGroup

data class VersionGroupDetail(
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int,
    @SerializedName("move_learn_method")
    val moveLearnMethod: MoveLearnMethod,
    @SerializedName("version_group")
    val versionGroup: VersionGroup
)