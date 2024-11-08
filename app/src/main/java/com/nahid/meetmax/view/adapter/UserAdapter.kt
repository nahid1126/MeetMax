package com.nahid.meetmax.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahid.meetmax.databinding.RowProfileItemBinding
import com.nahid.meetmax.model.data.User
import javax.inject.Inject

class UserAdapter @Inject constructor() : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var userList = listOf<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view = RowProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.binding(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun setUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: RowProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(user: User) {
            binding.user = user
        }
    }


}