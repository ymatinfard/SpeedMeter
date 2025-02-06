package com.matin.core.network.model

import com.google.gson.annotations.SerializedName

data class NetworkPlayers(
    @SerializedName("results")
    val players: List<NetworkPlayer>?
)

data class NetworkPlayer(
    @SerializedName("name")
    val name: NetworkName?,
    @SerializedName("picture")
    val picture: NetworkPicture?
)

data class NetworkName(
    @SerializedName("title")
    val title: String?,
    @SerializedName("first")
    val first: String?,
    @SerializedName("last")
    val last: String?
)

data class NetworkPicture(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?
)

