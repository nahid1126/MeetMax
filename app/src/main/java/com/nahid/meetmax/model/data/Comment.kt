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
        childColumns = ["postOwnerId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userCommenterId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val commentId: Long? = null,
    val comment: String,
    val postOwnerId: Long,
    val userCommenterId: Long
):Parcelable
