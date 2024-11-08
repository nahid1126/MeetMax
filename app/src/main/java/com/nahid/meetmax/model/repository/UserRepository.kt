package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.Likes
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.model.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun addPost(content: String, userId: Long, postImage: String?): Long
    suspend fun addComment(comment: String, postId: Long, userId: Long, commentUser: String): Long

    suspend fun addLike(postId: Long, userId: Long)
    suspend fun removeLike(postId: Long, userId: Long)
    suspend fun getLikeForPost(postId: Long, userId: Long): Likes?



    fun getAllPostsWithDetails(): Flow<List<PostWithCommentsAndLikes>>

    fun getLikeCount(postId: Long):Flow<Int>
}