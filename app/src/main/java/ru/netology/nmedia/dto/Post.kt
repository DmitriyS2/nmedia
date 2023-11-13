package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar:String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val watches: Int = 0,
    val videoUrl:String? = null,
    var unSaved:Boolean = true,
    var hidden:Boolean = false
  //  val attachment: Attachment? = null
)

data class Attachment(
    val url:String,
    val description:String,
    val type:String
)

