package com.nahid.meetmax.model.repository

import com.nahid.meetmax.model.data.Comment
import com.nahid.meetmax.model.data.Likes
import com.nahid.meetmax.model.data.Post
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.local.LocalDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDatabase: LocalDatabase
) : UserRepository {
    private val userDao = localDatabase.userDao()
    override suspend fun addPost(content: String, userId: Long, postImage: String?): Long {
        val post =
            Post(content = content, userOwnerId = userId, timestamp = System.currentTimeMillis(), imageUrl = postImage)
       return userDao.insertPost(post)
    }

    override suspend fun addComment(
        comment: String,
        postId: Long,
        userId: Long,
        commentUser: String
    ): Long {
        val comment = Comment(
            comment = comment,
            postOwnerId = postId,
            userCommenterId = userId,
            commentUser = commentUser
        )
       return userDao.insertComment(comment)
    }

    override suspend fun addLike(postId: Long, userId: Long) {
        val like = Likes(postLikedId = postId, userLikedId = userId)
        userDao.insertLike(like)
    }

    override suspend fun removeLike(postId: Long, userId: Long) {
        val likeToDelete = Likes(postLikedId = postId, userLikedId = userId)
        userDao.deleteLike(likeToDelete)
    }

    override suspend fun getLikeForPost(postId: Long, userId: Long): Likes? {
        return userDao.getLikeForPost(postId, userId)
    }

    override fun getAllPostsWithDetails(): Flow<List<PostWithCommentsAndLikes>> {
        return userDao.getAllPostsWithUserInfo()
    }

    override fun getLikeCount(postId: Long): Flow<Int> {
        return userDao.getLikeCount(postId)
    }

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUser()
    }
}