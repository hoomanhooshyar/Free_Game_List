package com.example.freegamelist.data.remote.dto


import com.example.freegamelist.data.local.entity.GameEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameDto(
    @Json(name = "developer")
    val developer: String,
    @Json(name = "freetogame_profile_url")
    val freetogameProfileUrl: String,
    @Json(name = "game_url")
    val gameUrl: String,
    @Json(name = "genre")
    val genre: String,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "platform")
    val platform: String,
    @Json(name = "publisher")
    val publisher: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "short_description")
    val shortDescription: String,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "title")
    val title: String
){
    fun toGameEntity():GameEntity{
        return GameEntity(
            developer = developer,
            freeToGameProfileUrl = freetogameProfileUrl,
            gameUrl = gameUrl,
            genre = genre,
            id = id,
            platform = platform,
            publisher = publisher,
            releaseDate = releaseDate,
            shortDescription = shortDescription,
            thumbnail = thumbnail,
            title = title
        )
    }
}