package ru.netology.nmedia.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeContentAndSave(content: String) {

        val dateTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a"))

        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text, published = dateTime))
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