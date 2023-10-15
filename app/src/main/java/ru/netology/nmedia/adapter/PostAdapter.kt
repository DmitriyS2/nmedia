package ru.netology.nmedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.CounterView.createCount
import ru.netology.nmedia.dto.Post


interface OnInteractionListener {
    fun like(post: Post)
    fun share(post: Post)
    fun remove(post: Post)
    fun edit(post: Post)
    fun showVideo(post: Post)
    fun goToPost(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = createCount(post.likes)
            share.text = createCount(post.shares)
            Glide.with (avatar)
                .load("http://192.168.1.10:9999/avatars/${post.authorAvatar}")
                .placeholder(R.drawable.baseline_emoji_emotions_24)
                .error(R.drawable.remove_red_eye_24)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)

            if (post.videoUrl!=null) {
                binding.groupVideo.visibility = View.VISIBLE
            } else{
                binding.groupVideo.visibility = View.GONE
            }

            if(post.attachment!=null) {
                Glide.with (attachmentImage)
                    .load("http://192.168.1.10:9999/images/${post.attachment.url}")
                    .placeholder(R.drawable.baseline_autorenew_24)
                    .error(R.drawable.ic_cancel_48)
                    .timeout(10_000)
                    .into(attachmentImage)
                attachmentImage.visibility = View.VISIBLE
            } else {
                attachmentImage.visibility = View.GONE
            }

            videoView.setOnClickListener {
                onInteractionListener.showVideo(post)
            }
            like.setOnClickListener {
                onInteractionListener.like(post)
            }
            share.setOnClickListener {
                onInteractionListener.share(post)
            }
            groupPost.setAllOnClickListener {
                Log.d("MyLog", "groupPost ${post.id}")
                onInteractionListener.goToPost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.remove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.edit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            watchCount.text = post.watches.toString()
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}