package com.nahid.meetmax.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}