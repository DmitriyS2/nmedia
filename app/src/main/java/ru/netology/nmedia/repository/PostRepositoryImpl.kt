package ru.netology.nmedia.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://192.168.1.10:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun likeById(id: Long) {

        //получаю post с сервера по id
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        val call = client.newCall(request)
        val response = call.execute()
        val responseString = response.body?.string() ?: error("no body")
        var post = gson.fromJson(responseString, Post::class.java)

//        var post:Post = client.newCall(request)
//            .execute()
//            .let { it.body?.string() ?: throw RuntimeException("body is null") }
//            .let {
//                gson.fromJson(it, Post::class.java)
//            }

        //меняю состояние и счетчик лайков
        post = post.copy(likes = if(post.likedByMe) post.likes-1 else post.likes+1, likedByMe = !post.likedByMe)

        Log.d("MyLog", "post ${post.toString()}")

            //запрос на внесение измененного поста
        val requestToChange: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

            //вызываю запрос
        val callToChange = client.newCall(requestToChange)
        val responseChange = callToChange.execute()
        responseChange.close()

//        if(post.likedByMe) {
//            val requestTrue: Request = Request.Builder()
//                .delete()
//                .url("${BASE_URL}/api/slow/posts/$id/likes")
//                .build()
//            val callTrue = client.newCall(requestTrue)
//            val responseTrue = callTrue.execute()
//            val responseStringTrue = responseTrue.body?.string() ?: error("no body")
//
//            Log.d("MyLog", "responseStringTrue true ${responseStringTrue}")

//            post = gson.fromJson(responseStringTrue, Post::class.java)
//            responseTrue.close()
//        } else {
//            post = post.copy(likes = if(post.likedByMe) post.likes-1 else post.likes+1, likedByMe = !post.likedByMe)
//            Log.d("MyLog", "else ${post.toString()}")
//
//            val requestFalse: Request = Request.Builder()
//                .post(gson.toJson(post).toRequestBody(jsonType))
//                .url("${BASE_URL}/api/slow/posts/$id/likes")
//                .build()
//
//            val requestCheck: Request = Request.Builder()
//                .url("${BASE_URL}/api/slow/posts/$id")
//                .build()


//            var result:Post = client.newCall(requestFalse)
//                .execute()
//                .let { it.body?.string() ?: throw RuntimeException("body is null") }
//                .let {
//                    gson.fromJson(it, Post::class.java)
//                }
            //Log.d("MyLog", "result ${result.toString()}")
//            val callFalse = client.newCall(requestFalse)
//            val responseFalse = callFalse.execute()
//            val responseStringFalse = responseFalse.body?.string() ?: error("no body")

//            Log.d("MyLog", "responseStringFalse post/false ${responseStringFalse}")

//            post = gson.fromJson(responseStringFalse, Post::class.java)
//            responseFalse.close()
//
//            val callCheck = client.newCall(requestCheck)
//            val responseCheck = callCheck.execute()
//            val responseStringCheck = responseCheck.body?.string() ?: error("no body")
//
//            Log.d("MyLog", "responseStringCheck false ${responseStringCheck}")
//
//            post = gson.fromJson(responseStringCheck, Post::class.java)
//            responseCheck.close()

//        }
    }

    override fun shareById(id: Long) {

    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }



    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}