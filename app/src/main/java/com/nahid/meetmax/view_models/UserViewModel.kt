package com.nahid.meetmax.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.meetmax.di.qualifier.SignInQualifier
import com.nahid.meetmax.di.qualifier.UserQualifier
import com.nahid.meetmax.model.data.User
import com.nahid.meetmax.model.repository.SignInRepository
import com.nahid.meetmax.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "UserViewModel"
@HiltViewModel
class UserViewModel @Inject constructor(
    @UserQualifier private val userRepository: UserRepository,
    @SignInQualifier private val signInRepository: SignInRepository
) :
    ViewModel() {

    private var user: User? = null
    var message = MutableSharedFlow<String>()
    var content = MutableStateFlow<String>("")
    var comment = MutableStateFlow<String>("")

    fun getUser(email: String) {
        viewModelScope.launch(IO) {
            user = signInRepository.checkMailFound(email)
        }
    }

    fun addPost(onResult: (Boolean, String) -> Unit) {
        val content = content.value
        viewModelScope.launch {
            if (content.isEmpty()) {
                message.emit("Please write something")
            } else {
                Log.d(TAG, "addPost: $content ${user?.userId}")
                val result = userRepository.addPost(content, user?.userId!!)
                if (result != -1L) {
                    onResult(true, "Post added successfully")
                } else {
                    onResult(false, "Something went wrong")
                }
            }

        }
    }

    fun fetchAllPosts() =
        userRepository.getAllPostsWithDetails().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
        )

    fun fetchLikeCount(postId: Long) = userRepository.getLikeCount(postId).shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(), 0
    )

    fun addLike(postId: Long, userId: Long) {
        viewModelScope.launch {
            val existingLike = userRepository.getLikeForPost(postId, userId)
            Log.d(TAG, "addLike: $existingLike clickedUser: $userId postId: $postId")
            if (existingLike != null) {
                userRepository.removeLike(postId, userId)
            } else {
                userRepository.addLike(postId, userId)
            }
        }
    }

    fun addComment(postId: Long, userId: Long) {
        val comments = comment.value
        viewModelScope.launch {
            if (comments.isEmpty()) {
                Log.d(TAG, "addComment: empty")
            } else {
                val result = userRepository.addComment(comments, postId, userId)
                if (result != -1L) {
                    comment.value = ""
                }
            }
        }
    }
}