package com.nahid.meetmax.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nahid.meetmax.databinding.RowPostItemBinding
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.utils.ApplicationCallBack
import com.nahid.meetmax.view_models.UserViewModel
import javax.inject.Inject

private const val TAG = "PostAdapter"
class PostAdapter @Inject constructor() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postList = listOf<PostWithCommentsAndLikes>()
    private lateinit var adapterClickListener: ApplicationCallBack.AdapterClickListener
    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var commentAdapter: CommentAdapter
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
    fun setViewModel(viewModel: UserViewModel) {
        this.userViewModel = viewModel
    }
    fun setItemClick(adapterClickListener: ApplicationCallBack.AdapterClickListener) {
        this.adapterClickListener = adapterClickListener
    }
    override fun getItemCount(): Int = postList.size
    inner class PostViewHolder(val binding: RowPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(post: PostWithCommentsAndLikes) {
            binding.model = post
            post.likes.size
            binding.adapterClickListener = adapterClickListener
            binding.likeCount = post.likes.size
            Log.d(TAG, "binding: ")
            binding.imageShow = !post.post.imageUrl.isNullOrEmpty()
            binding.userViewModel = userViewModel
            binding.recyclerviewComment.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = commentAdapter
            }
            commentAdapter.setCommentList(post.comments)
        }
    }
}