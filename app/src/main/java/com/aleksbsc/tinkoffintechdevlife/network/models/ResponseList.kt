package com.aleksbsc.tinkoffintechdevlife.network.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ResponseList(
    @SerialName("result")
    val networkMemesList: List<MemesServerResponse>,
    @SerialName("totalCount")
    val totalCount: Int // 12922
)