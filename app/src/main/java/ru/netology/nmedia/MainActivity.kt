package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            1,
            "Нетология. Университет интернет-профессий будущего",
            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            "21 мая в 18:36",
            true,
            999999
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            if (post.likedByMe) {
                like?.setImageResource(R.drawable.ic_liked_24)
            }

            likeCount?.text = createCount(post.likes)

            like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--

                likeCount?.text = createCount(post.likes)
            }

            shareCount?.text = post.share.toString()
            share.setOnClickListener {
                post.share++

                shareCount?.text = createCount(post.share)
            }

            watchCount.text = createCount(post.watch)
        }
    }

    fun createCount(count: Int): String {
        var first: Int = 0
        var second: Int = 0
        var symbol: String = ""

        when {
            (count < 1000) -> first = count
            (count in 1000..9999) -> {
                first = count / 1000
                second = (count / 100) - (first * 10)
                symbol = "K"
            }

            (count in 10000..999_999) -> {
                first = count / 1000
                symbol = "K"
            }

            (count in 1_000_000..9_999_999) -> {
                first = count / 1_000_000
                second = (count / 100_000) - (first * 10)
                symbol = "M"
            }

            (count >= 10_000_000) -> {
                first = count / 1_000_000
                symbol = "M"
            }
        }

        return first.toString() + if (second != 0) ".$second$symbol" else symbol
    }
}