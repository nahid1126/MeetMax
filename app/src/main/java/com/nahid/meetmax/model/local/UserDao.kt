package com.nahid.meetmax.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nahid.meetmax.model.data.Comment
import com.nahid.meetmax.model.data.Likes
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

  @Query("SELECT * FROM User")
  fun getAllUser(): Flow<List<User>>

    // Post-related methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post): Long

    @Transaction
    @Query("SELECT * FROM Post WHERE userOwnerId = :userId")
    suspend fun getPostsByUser(userId: Long): List<PostWithCommentsAndLikes>

    // Comment-related methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment): Long

    // Likes-related methods
    @Insert
    suspend fun insertLike(like: Likes)

    @Delete
    suspend fun deleteLike(like: Likes)

    @Query("SELECT * FROM Likes WHERE postLikedId = :postId AND userLikedId = :userId")
    suspend fun getLikeForPost(postId: Long, userId: Long): Likes?

    // Retrieve the count of likes for a specific post
    @Query("SELECT COUNT(*) FROM Likes WHERE postLikedId = :postId")
    fun getLikeCount(postId: Long): Flow<Int>

    // Queries to get all posts and their relationships
    @Transaction
    @Query("SELECT * FROM Post")
    fun getAllPostsWithUserInfo(): Flow<List<PostWithCommentsAndLikes>>

    @Transaction
    @Query("SELECT * FROM Post WHERE postId = :postId")
    suspend fun getPostWithCommentsAndLikes(postId: Long): List<PostWithCommentsAndLikes>
}