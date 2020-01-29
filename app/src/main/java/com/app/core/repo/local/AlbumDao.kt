package com.app.core.repo.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.core.model.RecyclerItemAlbum

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(albums: List<RecyclerItemAlbum>)

    @Query("SELECT * FROM album")
    fun loadAllAlbums(): DataSource.Factory<Int, RecyclerItemAlbum>

}