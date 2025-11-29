package com.hivian.randomusers.homefeature.data.services

import com.hivian.randomusers.core.data.ServiceResult
import com.hivian.randomusers.core.data.local.dao.IRandomUsersDao
import com.hivian.randomusers.core.data.remote.ErrorType
import com.hivian.randomusers.core.data.remote.HttpResult
import com.hivian.randomusers.core.domain.services.IApiService
import com.hivian.randomusers.core.domain.services.IDatabaseService
import com.hivian.randomusers.core.presentation.services.UserFilterPreferences
import com.hivian.randomusers.homefeature.data.mappers.ImageSize
import com.hivian.randomusers.homefeature.data.mappers.mapToRandomUser
import com.hivian.randomusers.homefeature.data.mappers.mapToRandomUsers
import com.hivian.randomusers.homefeature.domain.models.RandomUser
import com.hivian.randomusers.homefeature.domain.services.IRandomUsersService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RandomUsersService(
    private val randomUsersDao: IRandomUsersDao,
    private val httpClient: IApiService,
) : IRandomUsersService {
    override suspend fun deleteUser(userId: Int) {
        randomUsersDao.deleteUserById(userId)
    }


    override val usersFlow: Flow<List<RandomUser>> = randomUsersDao.getAllUsersFlow()
        .map { dtos ->

            dtos.map { it.mapToRandomUser(ImageSize.MEDIUM) }
        }


    override suspend fun fetchAndSaveNewUser(
        pageIndex: Int,
        pageSize: Int,
        gender: String?,
        nat: String?
    ): ServiceResult<Unit> {

        val httpUsersResult = httpClient.fetchRandomUsers(
            page = pageIndex,
            results = pageSize,
            gender = gender,
            nat = nat
        )

        return when (httpUsersResult) {
            is HttpResult.Success -> {
                val users = httpUsersResult.data.results
                if (users.isEmpty()) {
                    ServiceResult.Error(ErrorType.NO_RESULT)
                } else {
                    randomUsersDao.upsert(users)
                    ServiceResult.Success(Unit)
                }
            }

            is HttpResult.Error -> {
                ServiceResult.Error(httpUsersResult.errorType)
            }
        }
    }


    override suspend fun getUserById(userId: Int, imageSize: ImageSize): ServiceResult<RandomUser> {
        return try {
            val userDto = randomUsersDao.getRandomUserById(userId)

            ServiceResult.Success(userDto.mapToRandomUser(imageSize))
        } catch (e: Exception) {
            ServiceResult.Error(ErrorType.DATABASE_ERROR)
        }
    }
}
