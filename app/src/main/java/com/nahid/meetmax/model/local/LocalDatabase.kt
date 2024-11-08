package com.nahid.meetmax.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nahid.meetmax.model.data.Comment
import com.nahid.meetmax.model.data.Likes
import com.nahid.meetmax.model.data.Post
import com.nahid.meetmax.model.data.User

@Database(entities = [User::class, Post::class, Comment::class, Likes::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}