package com.hivian.randomusers.core.data.remote

import com.hivian.randomusers.core.data.models.Results
import com.hivian.randomusers.core.domain.services.IApiService

internal class ApiService(
    private val service: IHttpClient
): IApiService {

    override suspend fun fetchRandomUsers(
        page: Int,
        results: Int,
        gender: String?,
        nat: String?
    ): HttpResult<Results> {
        return safeApiCall {
            service.fetchRandomUsers(
                gender = gender,
                nat = nat,
                page = page,
                results = results,
                seed = null // <--- ОБЯЗАТЕЛЬНО NULL
            )
        }
    }
}
