package com.hivian.randomusers.homefeature.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivian.randomusers.core.R
import com.hivian.randomusers.core.data.ServiceResult
import com.hivian.randomusers.core.domain.base.ViewModelVisualState
import com.hivian.randomusers.core.domain.extensions.toErrorMessage
import com.hivian.randomusers.core.domain.services.ILocalizationService
import com.hivian.randomusers.core.domain.usecases.ShowAppMessageUseCase
import com.hivian.randomusers.core.presentation.navigation.NavigationAction
import com.hivian.randomusers.core.presentation.services.UserFilter
import com.hivian.randomusers.core.presentation.services.UserFilterPreferences
import com.hivian.randomusers.homefeature.domain.usecases.GetRandomUsersUseCase
import com.hivian.randomusers.homefeature.domain.services.IRandomUsersService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random


class HomeViewModel(
    private val localizationService: ILocalizationService,
    private val getRandomUsersUseCase: GetRandomUsersUseCase,
    private val showAppMessageUseCase: ShowAppMessageUseCase,
    private val filterPreferences: UserFilterPreferences,
    private val randomUsersService: IRandomUsersService
) : ViewModel() {

    var viewModelVisualState = MutableStateFlow<ViewModelVisualState>(ViewModelVisualState.Loading)
        private set

    var title: String = localizationService.localizedString(R.string.home_title)

    val items = getRandomUsersUseCase.usersFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _navigationEvent = MutableSharedFlow<NavigationAction>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _isLoadingNewUser = MutableStateFlow(false)


    private val isInitialized = mutableStateOf(false)

    val errorMessage: String
        get() = when (val state = viewModelVisualState.value) {
            is ViewModelVisualState.Error -> {
                localizationService.localizedString(state.errorType.toErrorMessage())
            }

            else -> ""
        }

    val retryMessage: String = localizationService.localizedString(R.string.retry_message)
    private val _forceRefreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    fun initialize() {
        if (isInitialized.value) return

        viewModelScope.launch {
            merge(
                filterPreferences.currentFilter.map { it to "filter_changed" },
                _forceRefreshTrigger.map { filterPreferences.currentFilter.value to "force_refresh" }
            ).collect { (filter, source) ->
                println("DEBUG: Loading triggered by $source")
                loadNewUser(filter)
            }
        }
        isInitialized.value = true
    }


    private fun loadNewUser(filter: UserFilter? = null) {
        _isLoadingNewUser.value = true
        _isLoadingNewUser.value = false
        viewModelScope.launch {
            if (items.value.isEmpty()) {
                viewModelVisualState.value = ViewModelVisualState.Loading
            }

            val currentFilter = filter ?: filterPreferences.currentFilter.value
            val randomPage = Random.nextInt(1, 5000)


            //ПОЛ
            val rawGender = currentFilter.gender?.trim()?.lowercase() ?: ""
            val genderParam = when {
                rawGender.contains("female") -> "female"
                rawGender.contains("male") -> "male"
                else -> null
            }

            //НАЦИОНАЛЬНОСТЬ
            val rawNat = currentFilter.nat?.trim()?.lowercase()
            val natParam = if (rawNat == "all" || rawNat.isNullOrEmpty()) null else rawNat

            println("DEBUG_API_CALL: Page=$randomPage, Gender=$genderParam, Nat=$natParam")

            val result = getRandomUsersUseCase.fetchAndSaveNewUser(
                pageIndex = randomPage,
                pageSize = 1,
                gender = genderParam,
                nat = natParam
            )

            when (result) {
                is ServiceResult.Success -> {
                    viewModelVisualState.value = ViewModelVisualState.Success
                }

                is ServiceResult.Error -> {
                    if (items.value.isNotEmpty()) {
                        viewModelVisualState.value = ViewModelVisualState.Success
                        showAppMessageUseCase(
                            localizationService.localizedString(result.errorType.toErrorMessage())
                        )
                    } else {
                        viewModelVisualState.value = ViewModelVisualState.Error(result.errorType)
                    }
                }
            }
        }
    }

    fun openRandomUserDetail(userId: Int) {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationAction.ToDetailScreen(userId))
        }
    }

    fun refresh() {
        loadNewUser()
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            randomUsersService.deleteUser(userId)
        }
    }
}