package com.nahid.meetmax.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Post(
    @PrimaryKey(autoGenerate = true)
    val postId: Long? = null,
    val content: String,
    val timestamp: Long,
    val userOwnerId: Long
):Parcelable
