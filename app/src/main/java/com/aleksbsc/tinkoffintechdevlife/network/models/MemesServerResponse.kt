package com.aleksbsc.tinkoffintechdevlife.network.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class MemesServerResponse(
    @SerialName("id")
    val id: Int, // 9555
    @SerialName("description")
    val description: String, // Команда дружна и даже херней будет заниматься вместе :)
    @SerialName("votes")
    val votes: Int, // 179
    @SerialName("author")
    val author: String, // Fire
    @SerialName("date")
    val date: String, // Feb 18, 2014 7:49:03 PM
    @SerialName("gifURL")
    val gifURL: String, // http://static.devli.ru/public/images/gifs/201402/f66c3260-1a54-4c7b-93f0-3446180f63cd.gif
    @SerialName("gifSize")
    val gifSize: Int, // 2226408
    @SerialName("previewURL")
    val previewURL: String, // https://static.devli.ru/public/images/previews/201402/8e5701b4-089d-4662-afd4-59dadf4c4c7c.jpg
    @SerialName("videoURL")
    val videoURL: String, // http://static.devli.ru/public/images/v/201402/01f7e3a6-201f-4f24-ac3a-944617cc004e.mp4
    @SerialName("videoPath")
    val videoPath: String, // /public/images/v/201402/01f7e3a6-201f-4f24-ac3a-944617cc004e.mp4
    @SerialName("videoSize")
    val videoSize: Int, // 576059
    @SerialName("type")
    val type: String, // gif
    @SerialName("width")
    val width: String, // 400
    @SerialName("height")
    val height: String, // 221
    @SerialName("commentsCount")
    val commentsCount: Int, // 4
    @SerialName("fileSize")
    val fileSize: Int, // 2226408
    @SerialName("canVote")
    val canVote: Boolean // false
)