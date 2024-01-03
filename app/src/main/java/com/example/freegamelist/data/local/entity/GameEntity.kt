package com.example.freegamelist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.freegamelist.domain.model.Game

@Entity
data class GameEntity(
    val developer: String,
    val freeToGameProfileUrl: String,
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val releaseDate: String,
    val shortDescription: String,
    val thumbnail: String,
    val title: String,
    @PrimaryKey(autoGenerate = false) val id: Int?
){
    fun toGame():Game{
        return Game(
            developer = developer,
            freeToGameProfileUrl = freeToGameProfileUrl,
            gameUrl = gameUrl,
            genre = genre,
            platform = platform,
            publisher = publisher,
            releaseDate = releaseDate,
            shortDescription = shortDescription,
            thumbnail = thumbnail,
            title = title,
            id = id
        )
    }
}
