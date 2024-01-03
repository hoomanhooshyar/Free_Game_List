package com.example.freegamelist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.freegamelist.data.local.entity.GameEntity

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games:List<GameEntity>)

    @Query("DELETE FROM gameentity WHERE title IN(:titles)")
    suspend fun deleteGames(titles:List<String>)

    @Query("SELECT * FROM gameentity")
    suspend fun getAllGames():List<GameEntity>


    @Query("SELECT * FROM gameentity WHERE id=:id")
    suspend fun getGameById(id:Int):GameEntity


    @Query("SELECT * FROM gameentity WHERE genre LIKE '%'||:genre||'%'")
    suspend fun getGameByGenre(genre:String):List<GameEntity>
}