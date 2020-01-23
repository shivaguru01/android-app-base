package com.app.core.repo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.core.model.RecyclerItem

@Database(
    entities = [RecyclerItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

}