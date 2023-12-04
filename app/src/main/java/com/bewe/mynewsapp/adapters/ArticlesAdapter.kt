package com.bewe.mynewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bewe.mynewsapp.R
import com.bewe.mynewsapp.model.Article
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ArticlesAdapter (private val onClick: (Article) -> Unit)
    : ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(ArticleCallBack) {

    inner class ArticleViewHolder(itemView: View, val onClick: (Article) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.iv_articleThumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_articleTitle)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tv_articleAuthor)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_articleDate)

        private var currentArticle: Article? = null

        init {
            itemView.setOnClickListener{
                currentArticle?.let{
                    onClick(it)
                }
            }
        }

        fun bind(article: Article) {
            currentArticle = article

            Glide.with(itemView).load(article.urlToImage ?: R.drawable.image_not_available).centerCrop().into(ivThumbnail)
            tvTitle.text = article.title ?: "-"
            tvAuthor.text = article.author ?: "-"

            val inputDate = article.publishedAt
            val outputDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.US)

            try {
                // Coba parsing dengan format pertama
                val inputDateFormat1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val outputDate1 = inputDateFormat1.parse(inputDate)
                val formattedDate1 = outputDate1?.let { outputDateFormat.format(it) }
                tvDate.text = formattedDate1
            } catch (e: ParseException) {
                try {
                    // Coba parsing dengan format kedua
                    val inputDateFormat2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.US)
                    val outputDate2 = inputDateFormat2.parse(inputDate)
                    val formattedDate2 = outputDate2?.let { outputDateFormat.format(it) }
                    tvDate.text = formattedDate2
                } catch (e: ParseException) {
                    // Jika tidak dapat di-parse dengan format manapun, tangani secara sesuai dengan kebutuhan aplikasi Anda
                    tvDate.text = "-"
                }
            }
            // 2023-11-23T16:00:56Z
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_article, parent, false)
        return ArticleViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }
}

object ArticleCallBack : DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}