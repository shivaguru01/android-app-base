package com.app.core.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.core.model.RecyclerItem

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RecyclerItem>)

    @Query(
        "SELECT * FROM repo WHERE (name LIKE :queryString) OR (description LIKE " +
                ":queryString) ORDER BY name ASC"
    )
    fun searchRepoByName(queryString: String): androidx.paging.DataSource.Factory<Int, RecyclerItem>

}