package com.example.beatflowplayer.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.beatflowplayer.data.entities.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertAll(tracks: List<TrackEntity>)

    @Query("SELECT * FROM tracks")
    suspend fun getAll(): List<TrackEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll()

    @Delete
    suspend fun deleteAll()

    @Query("SELECT * FROM tracks WHERE is_favourite = 1")
    suspend fun getFavourites(): List<TrackEntity>
}