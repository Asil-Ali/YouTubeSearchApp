package com.example.youtubesearch.network

import com.example.youtubesearch.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("order") order: String = "relevance",
        @Query("key") apiKey: String
    ): Response<SearchResponse>
}
