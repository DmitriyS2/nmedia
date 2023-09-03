package ru.netology.nmedia.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        if(!arguments?.textArg.isNullOrEmpty()) {
//            arguments?.textArg
//                ?.let(binding.edit::setText)
            binding.edit.setText(arguments?.textArg)
        } else if (!editText.isNullOrEmpty()) {
            binding.edit.setText(editText)
        }

        binding.ok.setOnClickListener {
            viewModel.changeContentAndSave(binding.edit.text.toString())
            AndroidUtils.hideKeyboard(requireView())
            editText = ""
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            editText = binding.edit.text.toString()
            findNavController().navigateUp()
            Log.d("MyLog", "$editText")
        }
        return binding.root
    }
}