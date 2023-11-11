package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val authorAvatar:String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val watches: Int = 0,
    val videoUrl:String? = null,
    var unSaved:Boolean = true,
    var hidden:Boolean = false
  //  val attachment: Attachment? = null
) {
    fun toDto() = Post(id, author, content, authorAvatar, published, likedByMe, likes,shares,watches, videoUrl, unSaved, hidden)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.authorAvatar, dto.published, dto.likedByMe, dto.likes, dto.shares, dto.watches, dto.videoUrl, dto.unSaved, dto.hidden)

    }
}
fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)