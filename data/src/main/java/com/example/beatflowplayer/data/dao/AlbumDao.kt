package com.example.beatflowplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface AlbumDao {
    // CRUD

    @Insert
    fun insertAll(albums: AlbumDao)
}