package ru.netology.nmedia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import java.io.IOException

class SignUpViewModel:ViewModel() {

    fun signUp(login: String, password: String, name:String) {

        viewModelScope.launch {
            try {
                val response = PostsApi.service.registerUser(login, password, name)
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
                Log.d("MyLog", "ошибка signUp")
                //  throw MyUnknownError
            }

        }
    }
}