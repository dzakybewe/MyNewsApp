package com.bewe.mynewsapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bewe.mynewsapp.adapters.ArticlesAdapter
import com.bewe.mynewsapp.adapters.HeadlinesAdapter
import com.bewe.mynewsapp.api.ApiClient
import com.bewe.mynewsapp.model.Article
import com.bewe.mynewsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvAllArticles: RecyclerView
    private lateinit var callArticles: Call<NewsResponse>
    private lateinit var articlesAdapter: ArticlesAdapter

    private lateinit var rvHeadlines: RecyclerView
    private lateinit var callHeadlines: Call<NewsResponse>
    private lateinit var headlinesAdapter: HeadlinesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvAllArticles = findViewById(R.id.rv_allArticles)

        articlesAdapter = ArticlesAdapter { article -> articleOnClick(article) }
        rvAllArticles.adapter = articlesAdapter
        rvAllArticles.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        rvHeadlines = findViewById(R.id.rv_headlines)

        headlinesAdapter = HeadlinesAdapter { article -> articleOnClick(article) }
        rvHeadlines.adapter = headlinesAdapter
        rvHeadlines.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        getData()
    }

    private fun articleOnClick(article: Article) {
        Toast.makeText(applicationContext, article.title ?: "-", Toast.LENGTH_SHORT).show()
    }

    private fun getData(){
        callArticles = ApiClient.newsService.getAll()
        callArticles.enqueue(object : Callback<NewsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    articlesAdapter.submitList(response.body()?.articles)
                    articlesAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })

        callHeadlines = ApiClient.newsService.getTopHeadlines()
        callHeadlines.enqueue(object : Callback<NewsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    headlinesAdapter.submitList(response.body()?.articles)
                    headlinesAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}