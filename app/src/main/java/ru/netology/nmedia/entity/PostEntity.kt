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
    val authorAvatar:String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val watches: Int = 0,
    val videoUrl:String? = null,
    val unSaved:Boolean = true
  //  val attachment: Attachment? = null
) {
    fun toDto() = Post(id, author, authorAvatar,content, published, likedByMe, likes,shares,watches, videoUrl, unSaved)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe, dto.likes, dto.shares, dto.watches, dto.videoUrl, dto.unSaved)

    }
}
fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)