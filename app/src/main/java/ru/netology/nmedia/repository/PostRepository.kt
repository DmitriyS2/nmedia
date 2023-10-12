package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
 //   fun likeById(post: Post):Post
    fun shareById(post: Post)
  //  fun removeById(id: Long)
    fun save(post: Post)

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    fun likeByIdAsync(post: Post, callback:LikeCallback)

    interface LikeCallback {
        fun onSuccess(post:Post) {}
        fun onError(e: Exception) {}
    }

    fun removeByIdAsync(id: Long, callback: RemoveCallback)

    interface RemoveCallback {
        fun onSuccess (id: Long) {}
        fun onError(e: Exception) {}
    }

    interface RepositoryCallbac<T> {
        fun onSuccess(result:T) {}
        fun onError(e: Exception) {}

    }
}