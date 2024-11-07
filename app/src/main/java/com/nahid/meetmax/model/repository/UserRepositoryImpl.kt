package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.Comment
import com.nahid.meetmax.model.data.Like
import com.nahid.meetmax.model.data.Post
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.model.local.LocalDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDatabase: LocalDatabase
) : UserRepository {
    private val userDao = localDatabase.userDao()
    override suspend fun addPost(content: String, userId: Long) {
        val post =
            Post(content = content, userOwnerId = userId, timestamp = System.currentTimeMillis())
        userDao.insertPost(post)
    }

    override suspend fun addComment(comment: String, postId: Long, userId: Long) {
        val comment = Comment(comment = comment, postOwnerId = postId, userCommenterId = userId)
        userDao.insertComment(comment)
    }

    override suspend fun addLike(postId: Long, userId: Long) {
        val like = Like(postLikedId = postId, userLikedId = userId)
        userDao.insertLike(like)
    }

    override fun getAllPostsWithDetails(): Flow<List<PostWithCommentsAndLikes>> {
        return userDao.getAllPostsWithUserInfo()
    }
}