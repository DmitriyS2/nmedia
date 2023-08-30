package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    watches = 0
)

fun getEmpty(): Post {
    return empty
}

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao()
    )
    val data = repository.getAll()

    val edited = MutableLiveData(empty)

    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = empty
    }

    fun like(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun edit(post: Post?) {
        edited.value = post
    }
}