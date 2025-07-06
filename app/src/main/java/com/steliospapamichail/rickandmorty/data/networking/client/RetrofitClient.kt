package com.steliospapamichail.rickandmorty.data.networking.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://rickandmortyapi.com/api/"
private const val READ_TIMEOUT = 45L
private const val CONNECT_TIMEOUT = 45L

//todo:sp look into the builder pattern or something similar
class RetrofitClient {
    private lateinit var httpClient: OkHttpClient
    var retrofitClient: Retrofit

    init {
        setupOkHttpClient()
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            explicitNulls = false
            isLenient = true
        }
        retrofitClient = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                jsonConfig.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .client(httpClient)
            .build()
    }

    private fun setupOkHttpClient() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder
            .addInterceptor(logging)
            .build()
        httpClient = httpClientBuilder.build()
    }
}