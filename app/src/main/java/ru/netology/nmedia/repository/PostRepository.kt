package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
 //   fun likeById(post: Post):Post
    fun shareById(post: Post)
  //  fun removeById(id: Long)
    //fun save(post: Post)

    //fun getAllAsync(callback: RepositoryCallback<List<Post>>)

//    interface GetAllCallback {
//        fun onSuccess(posts: List<Post>) {}
//        fun onError(e: Exception) {}
//    }

    //fun likeByIdAsync(post: Post, callback:RepositoryCallback<Post>)

//    interface LikeCallback {
//        fun onSuccess(post:Post) {}
//        fun onError(e: Exception) {}
//    }

    //fun removeByIdAsync(id: Long, callback: RepositoryCallback<Long>)

//    interface RemoveCallback {
//        fun onSuccess (id: Long) {}
//        fun onError(e: Exception) {}
//    }

    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, callback: Callback<Unit>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
    fun likeByIdAsync(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(result: T) {}
        fun onError(e: Exception) {}
    }

//    interface SaveCallback {
//        fun onSuccess(value:Unit) {}
//        fun onError(e:Exception) {}
//    }


//    interface RepositoryCallback<T> {
//        fun onSuccess(result:T) {}
//        fun onError(e: Exception) {}
//
//    }
}