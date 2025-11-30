package com.hivian.randomusers.homefeature.domain.services

import com.hivian.randomusers.core.data.ServiceResult
import com.hivian.randomusers.homefeature.data.mappers.ImageSize
import com.hivian.randomusers.homefeature.domain.models.RandomUser
import kotlinx.coroutines.flow.Flow

interface IRandomUsersService {
    val usersFlow: Flow<List<RandomUser>>
    suspend fun fetchAndSaveNewUser(
        pageIndex: Int,
        pageSize: Int,
        gender: String?,
        nat: String?
    ): ServiceResult<Unit>

    suspend fun getUserById(userId: Int, imageSize: ImageSize): ServiceResult<RandomUser>
    suspend fun deleteUser(userId: Int)
}