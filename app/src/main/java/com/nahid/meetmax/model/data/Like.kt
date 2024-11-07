package com.nahid.meetmax.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    foreignKeys = [ForeignKey(
        entity = Post::class,
        parentColumns = ["postId"],
        childColumns = ["postLikedId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userLikedId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Like(
    @PrimaryKey(autoGenerate = true)
    val likeId: Long? = null,
    val postLikedId: Long,
    val userLikedId: Long
):Parcelable
