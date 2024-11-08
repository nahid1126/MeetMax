package com.nahid.meetmax.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahid.meetmax.databinding.RowCommentItemBinding
import com.nahid.meetmax.model.data.Comment
import javax.inject.Inject

class CommentAdapter @Inject constructor() :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var commentList = listOf<Comment>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val view = RowCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.binding(commentList[position])
    }

    override fun getItemCount(): Int = commentList.size

    fun setCommentList(commentList: List<Comment>) {
        this.commentList = commentList
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(val binding: RowCommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(comment: Comment) {
            binding.commentModel = comment
        }
    }
}