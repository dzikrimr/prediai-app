package com.example.prediai.data.remote.youtube

data class YoutubeVideoDetailsResponse(
    val items: List<YoutubeVideoItem>
)

data class YoutubeVideoItem(
    val id: String,
    val snippet: VideoSnippet, // Menggunakan snippet khusus untuk detail
    val statistics: VideoStatistics
)

data class VideoSnippet(
    val title: String,
    val channelTitle: String,
    val thumbnails: Thumbnails, // Bisa pakai 'Thumbnails' yang sudah ada
    val tags: List<String>?
)

data class VideoStatistics(
    val viewCount: String
)