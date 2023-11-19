package ru.netology.nmedia.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentSigninDialogBinding

class SignInDialogFragment(val title: String, val text: String, val icon: Int, val textPosButton:String, val textNegButton:String, val flagSignIn:Boolean = true) : DialogFragment() {
    lateinit var binding: FragmentSigninDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentSigninDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        binding.fragmentDialog.text = text
        builder.setView(binding.root)
        return builder
            .setIcon(icon)
            .setTitle(title)
            .setCancelable(true)
            .setPositiveButton(textPosButton) { _,_ ->
                dialog?.cancel()
                if(flagSignIn) {
                    findNavController().navigate(R.id.authenticationFragment)
                } else {
                    AppAuth.getInstance().removeAuth()
                    findNavController().navigate(R.id.feedFragment)
                }

            }
            .setNegativeButton(textNegButton) { _, _, ->
                dialog?.cancel()
                if(flagSignIn) {
                    findNavController().navigate(R.id.feedFragment)
                }
            }
            .create()
    }
}

