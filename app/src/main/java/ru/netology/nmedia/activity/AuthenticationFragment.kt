package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.netology.nmedia.databinding.FragmentAuthenticationBinding
import ru.netology.nmedia.viewmodel.SignInViewModel



class AuthenticationFragment : Fragment() {

    private val viewModel: SignInViewModel by activityViewModels()

    lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        binding.buttonSignIn.setOnClickListener {
            if (isFieldNotNull()) {

               viewModel.signIn(binding.login.text.toString(), binding.password.text.toString())

//                runBlocking {
//                    val response:String = if(viewModel.flagAuth) "Вы вошли как:${binding.login.text.toString()}" else "Ошибка входа"
//                    Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
//                 //   Toast.makeText(activity, "Вы вошли как:${binding.login.text.toString()}", Toast.LENGTH_SHORT).show()
//                    delay(2000)
//                }

                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    fun isFieldNotNull(): Boolean {
        var flag = true

        binding.apply {
            if (login.text.isNullOrEmpty()) {
                login.error = "Поле должно быть заполнено"
                flag = false
            }

            if (password.text.isNullOrEmpty()) {
                password.error = "Поле должно быть заполнено"
                flag = false
            }
        }
        return flag

    }
}