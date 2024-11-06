package com.nahid.meetmax.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nahid.meetmax.databinding.FragmentCreatePostBinding

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostBinding.inflate(layoutInflater)
        return binding.root
    }

}