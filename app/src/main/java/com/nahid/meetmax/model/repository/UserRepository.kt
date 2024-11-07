package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addPost(content: String, userId: Long)
    suspend fun addComment(comment: String, postId: Long, userId: Long)
    suspend fun addLike(postId: Long, userId: Long)


    fun getAllPostsWithDetails(): Flow<List<PostWithCommentsAndLikes>>
}