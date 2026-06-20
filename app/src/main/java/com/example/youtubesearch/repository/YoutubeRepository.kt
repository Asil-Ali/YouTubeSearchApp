package com.example.youtubesearch.repository

import com.example.youtubesearch.model.VideoItem
import com.example.youtubesearch.network.RetrofitClient
import com.example.youtubesearch.util.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class YoutubeRepository {

    suspend fun search(query: String, sortOrder: String = "relevance"): Result<List<VideoItem>> {
        return try {
            val response = RetrofitClient.api.searchVideos(
                query = query,
                maxResults = Constants.MAX_RESULTS,
                order = sortOrder,
                apiKey = Constants.YOUTUBE_API_KEY
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.items.isNotEmpty()) {
                    val videos = body.items.map { result ->
                        VideoItem(
                            videoId = result.id.videoId,
                            title = result.snippet.title,
                            description = result.snippet.description,
                            publishedAt = formatDate(result.snippet.publishedAt),
                            channelTitle = result.snippet.channelTitle,
                            thumbnailUrl = result.snippet.thumbnails.medium.url
                        )
                    }
                    Result.success(videos)
                } else {
                    Result.failure(Exception("NO_RESULTS"))
                }
            } else {
                val errorCode = response.code()
                when (errorCode) {
                    400, 401, 403 -> Result.failure(Exception("INVALID_KEY"))
                    404 -> Result.failure(Exception("NO_RESULTS"))
                    in 500..599 -> Result.failure(Exception("SERVER_ERROR"))
                    else -> Result.failure(Exception("UNKNOWN_ERROR"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("NETWORK_ERROR"))
        }
    }

    private fun formatDate(raw: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(raw)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            raw
        }
    }
}
