package com.hivian.randomusers.homefeature.domain.usecases

import com.hivian.randomusers.core.data.ServiceResult
import com.hivian.randomusers.homefeature.domain.models.RandomUser
import com.hivian.randomusers.homefeature.domain.services.IRandomUsersService


class GetRandomUsersUseCase(
    private val service: IRandomUsersService
) {

    val usersFlow = service.usersFlow

    suspend fun fetchAndSaveNewUser(pageIndex: Int, pageSize: Int, gender: String?, nat: String?) =
        service.fetchAndSaveNewUser(pageIndex, pageSize, gender, nat)
}