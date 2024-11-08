package com.nahid.meetmax.model.data

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithCommentsAndLikes(
    @Embedded val post: Post,
    @Relation(
        parentColumn = "userOwnerId",
        entityColumn = "userId"
    )
    val user: User,

    @Relation(
        parentColumn = "postId",
        entityColumn = "postOwnerId"
    )
    val comments: List<Comment>,
    @Relation(
        parentColumn = "postId",
        entityColumn = "postLikedId"
    )
    val likes: List<Likes>
)
