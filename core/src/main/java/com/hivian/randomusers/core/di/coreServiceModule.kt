package com.hivian.randomusers.core.di

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hivian.randomusers.core.domain.services.ILocalizationService
import com.hivian.randomusers.core.domain.services.IUserInteractionService
import com.hivian.randomusers.core.presentation.services.UserFilter
import com.hivian.randomusers.core.presentation.services.UserFilterPreferences
import org.koin.dsl.module

val coreServiceModule = module {
    single<ILocalizationService> { provideLocalizationService(get()) }
    single<IUserInteractionService> { provideUserInteractionService() }
    single { UserFilterPreferences() }
}

