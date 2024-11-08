package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.Likes
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addPost(content: String, userId: Long):Long
    suspend fun addComment(comment: String, postId: Long, userId: Long):Long

    suspend fun addLike(postId: Long, userId: Long)
    suspend fun removeLike(postId: Long, userId: Long)
    suspend fun getLikeForPost(postId: Long, userId: Long): Likes?



    fun getAllPostsWithDetails(): Flow<List<PostWithCommentsAndLikes>>

    fun getLikeCount(postId: Long):Flow<Int>
}