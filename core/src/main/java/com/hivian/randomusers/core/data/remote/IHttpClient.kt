package com.hivian.randomusers.core.data.remote

import com.hivian.randomusers.core.data.models.Results
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IHttpClient {

    @Throws(Exception::class)
    @Headers("Cache-Control: no-cache") // Отключаем кэш
    @GET("api/1.4") // Используем версию 1.4
    suspend fun fetchRandomUsers(
        // ВАЖНО: Ставим фильтры ПЕРВЫМИ аргументами
        @Query("gender") gender: String? = null,
        @Query("nat") nat: String? = null, // Строго "nat"

        @Query("page") page: Int,
        @Query("results") results: Int = 1,
        @Query("seed") seed: String? = null
    ): Results
}
