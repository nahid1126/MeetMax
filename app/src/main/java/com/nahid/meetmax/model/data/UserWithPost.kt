package com.nahid.meetmax.model.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPost(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userOwnerId"
    )
    val posts: List<Post>
)
