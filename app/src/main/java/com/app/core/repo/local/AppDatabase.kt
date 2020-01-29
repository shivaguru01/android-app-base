package com.app.core.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.core.model.RecyclerItem
import com.app.core.model.RecyclerItemAlbum

@Database(
    entities = [RecyclerItem::class, RecyclerItemAlbum::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    abstract fun albumDao(): AlbumDao

}


