package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.dto.Post
import retrofit2.Response
import ru.netology.nmedia.BuildConfig



private const val BASE_URL = "http://192.168.1.10:9999/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>
}

//interface PostsApiService {
//    @GET("posts")
//    fun getAll(): Call<List<Post>>
//
//    @GET("posts/{id}")
//    fun getById(@Path("id") id: Long): Call<Post>
//
//    @POST("posts")
//    fun save(@Body post: Post): Call<Post>
//
//    @DELETE("posts/{id}")
//    fun removeById(@Path("id") id: Long): Call<Unit>
//
//    @POST("posts/{id}/likes")
//    fun likeById(@Path("id") id: Long): Call<Post>
//
//    @DELETE("posts/{id}/likes")
//    fun dislikeById(@Path("id") id: Long): Call<Post>
//}

object PostsApi {
    val service: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }

//    val retrofitService: PostsApiService by lazy {
//        retrofit.create(PostsApiService::class.java)
//    }
}