package com.matin.model

data class Players(
    val players: List<Player>
)

data class Player(
    val id: String = "0", // Server should return unique id
    val name: Name,
    val picture: Picture
)

data class Name(
    val title: String,
    val first: String,
    val last: String
) {
    val fullName: String = "$title $first $last"

    companion object {
        fun empty() = Name("", "", "")
    }
}

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
) {
    companion object {
        fun empty() = Picture("", "", "")
    }
}
