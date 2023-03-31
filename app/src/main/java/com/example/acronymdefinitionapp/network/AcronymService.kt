package com.example.acronymdefinitionapp.network

import com.example.acronymdefinitionapp.model.Definition
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymService {

    @GET(ENDPOINT)
    suspend fun getAcronymDefinition(
        @Query("sf") acronym: String
    ): Response<List<Definition>>

    companion object {
        private const val BASE_URL = "http://www.nactem.ac.uk/software/acromine/"
        private const val ENDPOINT = "dictionary.py"

        val acronymService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        )
                        .build()
                )
                .build()
                .create(AcronymService::class.java)
    }
}