package com.app.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "album")
class RecyclerItemAlbum(

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val description: String?,

    @SerializedName("albumId")
    val albumId: Int,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String

) {
    val title: String?
        get() = "album - $albumId"
}