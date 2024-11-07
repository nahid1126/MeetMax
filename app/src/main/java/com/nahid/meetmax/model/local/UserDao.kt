package com.nahid.meetmax.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nahid.meetmax.model.data.Comment
import com.nahid.meetmax.model.data.Like
import com.nahid.meetmax.model.data.Post
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.model.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(movies: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM User WHERE email = :email and password = :password")
   suspend fun checkValidUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun checkUserByEmail(email: String): User?


    @Query("DELETE FROM User WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)

    // Post-related methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post): Long

    @Transaction
    @Query("SELECT * FROM Post WHERE userOwnerId = :userId")
    suspend fun getPostsByUser(userId: Long): List<PostWithCommentsAndLikes>

    // Comment-related methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment): Long

    // Like-related methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: Like): Long

    // Queries to get all posts and their relationships
    @Transaction
    @Query("SELECT * FROM Post")
    suspend fun getAllPostsWithUserInfo(): List<PostWithCommentsAndLikes>

    @Transaction
    @Query("SELECT * FROM Post WHERE postId = :postId")
    suspend fun getPostWithCommentsAndLikes(postId: Long): List<PostWithCommentsAndLikes>
}