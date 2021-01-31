package com.aleksbsc.tinkoffintechdevlife.network

import com.aleksbsc.tinkoffintechdevlife.network.helpers.Resource
import com.aleksbsc.tinkoffintechdevlife.network.models.MemesServerResponse
import com.aleksbsc.tinkoffintechdevlife.network.models.ResponseList
import retrofit2.http.GET
import retrofit2.http.Path

interface DevLifeService {

    @GET("latest/{page}?json=true")
    suspend fun getNewLatest(@Path("page") page: Int): Resource<ResponseList>

    @GET("random?json=true")
    suspend fun getNewRandom(): Resource<MemesServerResponse>

    @GET("top/{page}?json=true")
    suspend fun getNewTop(@Path("page") page: Int): Resource<ResponseList>

}