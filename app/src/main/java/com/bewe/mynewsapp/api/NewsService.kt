package com.bewe.mynewsapp.api

import com.bewe.mynewsapp.model.NewsResponse
import com.bewe.mynewsapp.util.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("/v2/top-headlines?country=us&totalResults=3&apiKey=$API_KEY")
    fun getTopHeadlines() : Call<NewsResponse>

    @GET("/v2/everything?q=finance&sortBy=publishedAt&apiKey=$API_KEY")
    fun getAll() : Call<NewsResponse>
}