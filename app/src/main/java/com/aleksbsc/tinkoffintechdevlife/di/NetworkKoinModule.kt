package com.aleksbsc.tinkoffintechdevlife.di

import com.aleksbsc.tinkoffintechdevlife.BuildConfig
import com.aleksbsc.tinkoffintechdevlife.network.DevLifeService
import com.aleksbsc.tinkoffintechdevlife.network.ResultAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkKoinModule: Module = module {

    single { provideHttpClient() }

    single {
        provideRetrofit(httpClient = get())
    }

    single {
        provideDevelopersLifeService(retrofit = get())
    } bind DevLifeService::class
}

fun provideDevelopersLifeService(retrofit: Retrofit): DevLifeService {
    return retrofit.create(DevLifeService::class.java)
}

fun provideRetrofit(
    httpClient: OkHttpClient
): Retrofit {

    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }

    return Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(ResultAdapterFactory())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

fun provideHttpClient(): OkHttpClient {

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient()
        .newBuilder()
        .addNetworkInterceptor(httpLoggingInterceptor)
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
}