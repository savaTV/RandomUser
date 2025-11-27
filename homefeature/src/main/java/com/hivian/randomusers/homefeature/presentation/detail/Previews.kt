package com.hivian.randomusers.homefeature.presentation.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class DetailViewModelArg(
    val picture : MutableState<String>,
    val name : MutableState<String>,
    val email : MutableState<String>,
    val cell : MutableState<String>,
    val phone : MutableState<String>,
    val city : MutableState<String>,
    val country : MutableState<String>,
    val latitude : MutableState<Double>,
    val longitude : MutableState<Double>,
    val navigateBack: () -> Unit = {},
    val age : MutableState<Int>,
    val data : MutableState<String>,
    val nat : MutableState<String>
)

class DetailViewModelArgProvider: PreviewParameterProvider<DetailViewModelArg> {
    override val values: Sequence<DetailViewModelArg> = sequenceOf(
        DetailViewModelArg(
            picture = mutableStateOf(""),
            name = mutableStateOf("John Doe"),
            email = mutableStateOf("john.doe@mail.com"),
            cell = mutableStateOf("0606060606"),
            phone = mutableStateOf("0606060606"),
            city = mutableStateOf("Paris"),
            country = mutableStateOf("France"),
            latitude = mutableStateOf(0.0),
            longitude = mutableStateOf(0.0),
            age = mutableStateOf(14),
            data = mutableStateOf("2007-07-09T05:51:59.390Z"),
            nat = mutableStateOf("US")


        )
    )
}
