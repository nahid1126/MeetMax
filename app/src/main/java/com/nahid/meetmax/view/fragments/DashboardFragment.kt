package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentDashboardBinding
import com.nahid.meetmax.model.data.PostWithCommentsAndLikes
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.ApplicationCallBack
import com.nahid.meetmax.view.adapter.PostAdapter
import com.nahid.meetmax.view_models.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var postWithCommentsAndLikes: ArrayList<PostWithCommentsAndLikes>


    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var postAdapter: PostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        postWithCommentsAndLikes = arrayListOf()
        setUpRecyclerView()
        binding.post.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_createPostFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.fetchAllPosts().collect {
                    if (it.isNotEmpty()) {
                        postWithCommentsAndLikes = it as ArrayList
                        postAdapter.setPostList(postWithCommentsAndLikes)
                    }
                }
            }
        }
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        with(postAdapter) {
            setItemClick(object : ApplicationCallBack.AdapterClickListener {
                override fun onLikeClick(postId: Long, userId: Long) {
                    userViewModel.addLike(postId, userId)
                }

                override fun onCommentClick(postId: Long, userId: Long) {
                    Toast.makeText(requireContext(), "comment clicked", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


}