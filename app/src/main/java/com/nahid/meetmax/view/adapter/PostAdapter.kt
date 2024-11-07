package com.nahid.meetmax.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahid.meetmax.databinding.RowPostItemBinding
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.utils.ApplicationCallBack
import javax.inject.Inject

class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postList = listOf<PostWithCommentsAndLikes>()
    private lateinit var adapterClickListener: ApplicationCallBack.AdapterClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val view = RowPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        holder.binding(postList[position])
    }

    fun setPostList(postList: List<PostWithCommentsAndLikes>) {
        this.postList = postList
        notifyDataSetChanged()
    }
    fun setItemClick(adapterClickListener: ApplicationCallBack.AdapterClickListener) {
        this.adapterClickListener = adapterClickListener
    }
    override fun getItemCount(): Int = postList.size
    inner class PostViewHolder(val binding: RowPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(post: PostWithCommentsAndLikes) {
            binding.model = post
            binding.adapterClickListener = adapterClickListener
        }
    }
}