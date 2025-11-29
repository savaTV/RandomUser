// core/presentation/services/UserFilterPreferences.kt
package com.hivian.randomusers.core.presentation.services

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.sql.Timestamp

data class UserFilter(
    val gender: String? = null , // "male", "female", null = all
    val nat: String? = null,
    val timestamp: Long = System.currentTimeMillis()// "us,fr,gb", null = all
)

class UserFilterPreferences {
    private val _currentFilter = MutableStateFlow(UserFilter())
    val currentFilter: StateFlow<UserFilter> = _currentFilter.asStateFlow()

    fun updateFilter(filter: UserFilter) {
        _currentFilter.value = filter
    }
}