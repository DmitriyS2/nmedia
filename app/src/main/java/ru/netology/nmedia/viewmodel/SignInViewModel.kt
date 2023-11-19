package ru.netology.nmedia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.SignIn
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import java.io.IOException

class SignInViewModel : ViewModel() {

    fun signIn(login: String, password: String) {

        viewModelScope.launch {
            try {
                val response = PostsApi.service.updateUser(login, password)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body =
                    response.body() ?: throw ApiError(response.code(), response.message())
                Log.d("MyLog", "id body=${body.id} token body=${body.token}")

                AppAuth.getInstance().setAuth(body.id, body.token)

            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                Log.d("MyLog", "ошибка signIn")
                //  throw MyUnknownError
            }

        }

    }

}