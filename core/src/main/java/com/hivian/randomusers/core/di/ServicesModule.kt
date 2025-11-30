package com.hivian.randomusers.core.di

import android.app.Application
import com.hivian.randomusers.core.domain.services.ILocalizationService
import com.hivian.randomusers.core.domain.services.IUserInteractionService
import com.hivian.randomusers.core.presentation.services.LocalizationService
import com.hivian.randomusers.core.presentation.services.UserInteractionService
import org.koin.dsl.module

fun provideLocalizationService(applicationContext: Application): ILocalizationService {
    return LocalizationService(applicationContext)
}

fun provideUserInteractionService(): IUserInteractionService {
    return UserInteractionService()
}




