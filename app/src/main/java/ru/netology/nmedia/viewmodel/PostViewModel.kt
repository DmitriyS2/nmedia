package ru.netology.nmedia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import androidx.lifecycle.*
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    watches = 0,
    videoUrl = "no_video"
)

//fun getEmpty(): Post {
//    return empty
//}

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            // Начинаем загрузку
            _data.postValue(FeedModel(loading = true))
            try {
                // Данные успешно получены
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                // Получена ошибка
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.postValue(empty)
    }

//    fun saveNew() {
//        thread {
//            edited.value?.let { edited->
//                val post = repository.save(edited)
//                val value = _data.value
//
//                val updatedPosts = value?.posts?.map {
//                    if (it.id==edited.id) {
//                        post
//                    } else {
//                        it
//                    }
//                }.orEmpty()
//
//                val result = if (value?.posts==updatedPosts) {
//                    listOf(post)+updatedPosts
//                } else {
//                    updatedPosts
//                }
//                _data.postValue(value?.copy(posts = result))
//            }
//            edited.postValue(empty)
//            _postCreated.postValue(Unit)
//        }
//    }

    fun edit(post: Post) {
        edited.postValue(post)
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        thread {
            Log.d("MyLog", "viewModel До ${_data.value?.posts?.filter { it.id == id }.toString()}")


            // показывает без счетчика(обновляется если зайти в создание поста и выйти), но в _data изменения видны
//            _data.value?.posts = _data.value?.posts?.map {
//                if (it.id != id) it else it.copy(
//                    likedByMe = !it.likedByMe,
//                    likes = if (it.likedByMe) (it.likes - 1) else (it.likes + 1)
//                )
//            }.orEmpty()


            // показывает в приложении правильно, но в _data по логу без изменений
            _data.value?.let {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts?.map { post->
                        if (post.id != id) {
                            post
                        } else {
                            post.copy(
                                likedByMe = !post.likedByMe,
                                likes = if (post.likedByMe) (post.likes - 1) else (post.likes + 1)
                            )
                        }
                    }.orEmpty()))
            }

            // показывает в приложении правильно, но в _data по логу без изменений
//            _data.postValue(
//                  _data.value?.posts?.map {
//                if (it.id != id) it else it.copy(
//                    likes = if (it.likedByMe) (it.likes - 1) else (it.likes + 1),
//                    likedByMe = !it.likedByMe
//                )
//            }?.let { _data.value?.copy(posts = it) })

            Log.d("MyLog", "viewModel После ${_data.value?.posts?.filter { it.id==id }.toString()}")


            repository.save(_data.value?.posts?.filter { it.id==id }?.first()!!)
            repository.likeById(id)
        }
    }

    fun share(id: Long) {
        thread {
            repository.shareById(id)
        }
    }

    fun removeById(id: Long) {
        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }


//    val data = repository.getAll()
//
//    val edited = MutableLiveData(empty)
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun changeContentAndSave(content: String) {
//
//        val dateTime = LocalDateTime.now()
//            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a"))
//
//        edited.value?.let {
//            val text = content.trim()
//            if (it.content != text) {
//                repository.save(it.copy(content = text, published = dateTime))
//            }
//        }
//        edited.value = empty
//    }
//
//    fun like(id: Long) = repository.likeById(id)
//    fun share(id: Long) = repository.shareById(id)
//    fun removeById(id: Long) = repository.removeById(id)
//    fun edit(post: Post?) {
//        edited.value = post
//    }
}