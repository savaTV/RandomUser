package com.hivian.randomusers.homefeature.di

import com.hivian.randomusers.core.data.local.dao.IRandomUsersDao
import com.hivian.randomusers.core.domain.services.IApiService

import com.hivian.randomusers.homefeature.data.services.RandomUsersService
import com.hivian.randomusers.homefeature.domain.services.IRandomUsersService
import org.koin.dsl.module

private fun provideRandomUsersRepository(
    randomUsersDao: IRandomUsersDao,
    httpClient: IApiService,

): IRandomUsersService {
    return RandomUsersService(randomUsersDao, httpClient)
}

val repositoryModule = module {
    single { provideRandomUsersRepository(get(), get()) }
}