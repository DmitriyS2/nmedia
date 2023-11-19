package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentCurrentPhotoBinding
import ru.netology.nmedia.dto.CounterView
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.view.load
import ru.netology.nmedia.viewmodel.PostViewModel

class CurrentPhotoFragment : Fragment() {

    companion object {
        var Bundle.textArgument: String? by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCurrentPhotoBinding.inflate(
            inflater,
            container,
            false
        )

        val currentId = arguments?.textArgument?.toLong()

        viewModel.data.observe(viewLifecycleOwner) { list ->
            list.posts.find { it.id == currentId }?.let { currentPost ->

                binding.apply {
                    currentPhoto.load("http://192.168.1.10:9999/media/${currentPost.attachment?.url}")

                    likePhoto.isChecked = currentPost.likedByMe
                    likePhoto.text = CounterView.createCount(currentPost.likes)

                    likePhoto.setOnClickListener {
                        viewModel.likeById(currentPost)
                    }

                    buttonReturn.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }
        }

//        val currentPost = viewModel.data.value?.posts
//            ?.first { it.id == currentId }
//
//        if (currentPost != null) {
//            binding.apply {
//                currentPhoto.load("http://192.168.1.10:9999/media/${currentPost.attachment?.url}")
//
//                likePhoto.isChecked = currentPost.likedByMe
//                likePhoto.text = CounterView.createCount(currentPost.likes)
//
//                likePhoto.setOnClickListener {
//                    viewModel.likeById(currentPost)
//                }
//
//                buttonReturn.setOnClickListener {
//                    findNavController().navigateUp()
//                }
//            }
//        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}