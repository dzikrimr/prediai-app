package com.example.prediai.data.remote.youtube

// Ini adalah data class yang SANGAT disederhanakan
// agar sesuai dengan apa yang kita butuhkan saja
data class YoutubeSearchResponse(
    val items: List<YoutubeSearchItem>
)

data class YoutubeSearchItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String
)

data class Snippet(
    val title: String,
    val channelTitle: String, // Ini akan jadi 'source'
    val thumbnails: Thumbnails,
    val description: String
)

data class Thumbnails(
    val medium: Thumbnail // Kita ambil gambar kualitas medium
)

data class Thumbnail(
    val url: String
)

data class YoutubeCaptionsListResponse(
    val items: List<YoutubeCaptionItem>? // Bisa null jika tidak ada caption
)

data class YoutubeCaptionItem(
    val id: String, // ID caption track
    val snippet: CaptionSnippet
)

data class CaptionSnippet(
    val language: String, // Kode bahasa (misal: "en", "id")
    val name: String, // Nama track (seringkali kosong)
    val trackKind: String // "ASR" (otomatis), "standard" (manual)
)