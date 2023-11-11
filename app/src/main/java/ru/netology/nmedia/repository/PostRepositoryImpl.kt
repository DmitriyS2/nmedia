package ru.netology.nmedia.repository

import android.util.Log
import androidx.lifecycle.map
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import okhttp3.Call
//import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.Response
import ru.netology.nmedia.dto.Post
import okhttp3.*
import ru.netology.nmedia.api.PostsApi
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.MyUnknownError
import ru.netology.nmedia.error.NetworkError

//class PostRepositoryImpl : PostRepository {
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .build()
//    private val gson = Gson()
//    private val typeToken = object : TypeToken<List<Post>>() {}
//
//    companion object {
//        private const val BASE_URL = "http://192.168.1.10:9999"
//        private val jsonType = "application/json".toMediaType()
//    }
class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data = dao.getAll().map(List<PostEntity>::toDto)

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            dao.isEmpty()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw MyUnknownError
        }
    }

  //  suspend fun checkIsEmpty() = dao.isEmpty()

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw MyUnknownError
        }
    }

    override suspend fun removeById(post: Post) {
        dao.removeById(post.id)
        try {
            val response = PostsApi.service.removeById(post.id)
            if (!response.isSuccessful) {
                dao.insert(PostEntity.fromDto(post))
                throw ApiError(response.code(), response.message())
            }
           // val body = response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            dao.insert(PostEntity.fromDto(post))
            throw NetworkError
        } catch (e: Exception) {
            dao.insert(PostEntity.fromDto(post))
            throw MyUnknownError
        }
    }

    override suspend fun likeById(post: Post) {
      dao.likeById(post.id)
        try {
            val response = if(!post.likedByMe) PostsApi.service.likeById(post.id) else PostsApi.service.dislikeById(post.id)
            if (!response.isSuccessful) {
                dao.likeById(post.id)
                throw ApiError(response.code(), response.message())
            }
           //  val body = response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            dao.likeById(post.id)
            throw NetworkError
        } catch (e: Exception) {
            dao.likeById(post.id)
            throw MyUnknownError
        }
    }
//    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
//
//        PostsApi.retrofitService.getAll()
//            .enqueue(object : Callback<List<Post>> {
//                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                    if (!response.isSuccessful) {
//                        callback.onError(RuntimeException(response.message()))
//                        return
//                    }
//                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
//                }
//
//                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//    }

//    override fun getAll(): List<Post> {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        return client.newCall(request)
//            .execute()
//            .let { it.body?.string() ?: throw RuntimeException("body is null") }
//            .let {
//                gson.fromJson(it, typeToken.type)
//            }
//    }

//    override fun getAllAsync(callback: PostRepository.RepositoryCallback<List<Post>>) {
//        val request: Request = Request.Builder()
//            .url("${BASE_URL}/api/posts")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string() ?: throw RuntimeException("body is null")
//                    try {
//                        callback.onSuccess(gson.fromJson(body, typeToken.type))
//
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//            })
//    }


//    override fun likeById(post: Post):Post {
//        //Log.d("MyLog", "post из repo ${post.toString()}")
//
//        val request: Request = if (!post.likedByMe) {
//            Request.Builder()
//                .post("".toRequestBody())
//                .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
//                .build()
//        } else {
//            Request.Builder()
//                .delete()
//                .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
//                .build()
//        }
//
//        val postAnswer: Post = client.newCall(request)
//            .execute()
//            .let { it.body?.string() ?: throw RuntimeException("body is null") }
//            .let {
//                gson.fromJson(it, Post::class.java)
//            }
//        //Log.d("MyLog", "postAnswer ${postAnswer.toString()}")
//        return postAnswer
//
//    }

//    override fun likeByIdAsync(post: Post, callback: PostRepository.RepositoryCallback<Post>) {
//
//        val request: Request = if (!post.likedByMe) {
//            Request.Builder()
//                .post("".toRequestBody())
//                .url("${BASE_URL}/api/posts/${post.id}/likes")
//                .build()
//        } else {
//            Request.Builder()
//                .delete()
//                .url("${BASE_URL}/api/posts/${post.id}/likes")
//                .build()
//        }
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    val body = response.body?.string() ?: throw RuntimeException("body is null")
//                    try {
//                        callback.onSuccess(gson.fromJson(body, Post::class.java))
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//            })
//    }

//    override fun likeByIdAsync(post: Post, callback: PostRepository.Callback<Post>) {
//        val request = if (post.likedByMe) {
//            PostsApi.retrofitService.dislikeById(post.id)
//        } else {
//            PostsApi.retrofitService.likeById(post.id)
//        }
//            request.enqueue(object : Callback<Post> {
//                override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                    if (response.isSuccessful) {
//                        callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
//                    } else {
//                        callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
//                    }
//                }
//
//                override fun onFailure(call: Call<Post>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//    }

    override fun shareById(post: Post) {

    }

//    override fun save(post: Post) {
//        val request: Request = Request.Builder()
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        client.newCall(request)
//            .execute()
//            .close()
//    }

//    override fun saveAsync(post: Post, callback: PostRepository.Callback<Unit>) {
//        PostsApi.retrofitService.save(post)
//            .enqueue(object : Callback<Post> {
//                override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                    if (response.isSuccessful) {
//                        callback.onSuccess(Unit)
//                    } else {
//                        callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
//                    }
//                }
//
//                override fun onFailure(call: Call<Post>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//    }

//    override fun removeById(id: Long) {
//        val request: Request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}/api/slow/posts/$id")
//            .build()
//
//        client.newCall(request)
//            .execute()
//            .close()
//    }

//    override fun removeByIdAsync(id: Long, callback: PostRepository.RepositoryCallback<Long>) {
//        val request: Request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}/api/posts/$id")
//            .build()
//
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
////                    val body = response.body?.string() ?: throw RuntimeException("body is null")
////                    Log.d("MyLog", "id $body")
//                    try {
//                        callback.onSuccess(id)
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//            })
//    }

//    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
//        PostsApi.retrofitService.removeById(id)
//            .enqueue(object : Callback<Unit> {
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                    if (response.isSuccessful) {
//                        callback.onSuccess(Unit)
//                    } else {
//                        callback.onError(RuntimeException("error code: ${response.code()} with ${response.message()}"))
//                    }
//                }
//
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    callback.onError(Exception(t))
//                }
//            })
//    }


}