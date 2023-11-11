package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
  //  fun getNewerCount(id: Long): Flow<Int>

    fun getNewerCount(): Flow<Int>
    suspend fun save(post: Post)

    suspend fun removeById(post: Post)
    suspend fun likeById(post: Post)

    suspend fun syncPost(list: List<Post>)
    suspend fun syncOnePost(post: Post)
    suspend fun changeHidden()
    suspend fun getCount():Long

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



//    fun getAllAsync(callback: Callback<List<Post>>)
//    fun saveAsync(post: Post, callback: Callback<Unit>)
//    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
//    fun likeByIdAsync(post: Post, callback: Callback<Post>)
//
//    interface Callback<T> {
//        fun onSuccess(result: T) {}
//        fun onError(e: Exception) {}
//    }

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