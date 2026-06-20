package com.example.youtubesearch.model

import com.google.gson.annotations.SerializedName

// Top-level response from the API
data class SearchResponse(
    val items: List<VideoResult>
)

// Each video result in the list
data class VideoResult(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    @SerializedName("videoId")
    val videoId: String
)

data class Snippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    val thumbnails: Thumbnails
)

data class Thumbnails(
    val medium: ThumbnailInfo
)

data class ThumbnailInfo(
    val url: String
)

// Clean model used in the UI
data class VideoItem(
    val videoId: String,
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    val thumbnailUrl: String
)
