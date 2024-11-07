package com.nahid.meetmax.view_models

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @UserQualifier private val userRepository: UserRepository,
    @SignInQualifier private val signInRepository: SignInRepository
) :
    ViewModel() {

    private var user: User? = null
    var message = MutableSharedFlow<String>()
    var content = MutableStateFlow<String>("")

    fun getUser(email: String) {
        viewModelScope.launch(IO) {
            user = signInRepository.checkMailFound(email)
        }
    }

    fun addPost() {
        val content = content.value
        viewModelScope.launch(IO) {
            if (content.isEmpty()) {
                message.emit("Please write something")
            } else {
                userRepository.addPost(content, user?.userId!!)
            }

        }
    }

    fun fetchAllPosts() =
        userRepository.getAllPostsWithDetails().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
        )

    fun addLike(postId: Long, userId: Long) {
        viewModelScope.launch(IO) {
            userRepository.addLike(postId, userId)
        }
    }
}