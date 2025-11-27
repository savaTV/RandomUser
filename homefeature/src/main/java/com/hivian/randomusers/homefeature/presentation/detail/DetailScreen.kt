package com.hivian.randomusers.homefeature.presentation.detail

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.hivian.randomusers.core.presentation.navigation.NavigationAction
import com.hivian.randomusers.homefeature.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    userId: Int,
    viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(userId) }),
    onNavigateBack: () -> Unit
) {
    viewModel.initialize()

    val navigationEventState = viewModel.navigationEvent.collectAsState(initial = null)

    LaunchedEffect(navigationEventState.value) {
        when (navigationEventState.value) {
            is NavigationAction.Back -> onNavigateBack()
            else -> Unit
        }
    }

    DetailContent(
        DetailViewModelArg(
            picture = viewModel.picture,
            name = viewModel.name,
            email = viewModel.email,
            cell = viewModel.cell,
            phone = viewModel.phone,
            city = viewModel.city,
            country = viewModel.country,
            latitude = viewModel.latitude,
            longitude = viewModel.longitude,
            navigateBack = { viewModel.navigateBack() },
            age = viewModel.age,
            data = viewModel.data,
            nat = viewModel.nat
        )
    )
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    @PreviewParameter(DetailViewModelArgProvider::class) viewModelArg: DetailViewModelArg
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val titles = listOf("TAB 1", "TAB 2", "TAB 3", )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = viewModelArg.name.value) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModelArg.navigateBack()
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backIcon"
                        )
                    }
                },
            )
        }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                titles.forEachIndexed { index, title ->
                    FancyTab(
                        title = title,
                        onClick = { selectedTabIndex = index },
                        selected = (index == selectedTabIndex)
                    )
                }
            }
            when (selectedTabIndex) {
                1 -> {// данные пользователя
                    ProfileTab(viewModelArg)
                }
                2 -> {
                    ContactTab(viewModelArg)
                }
                3 -> {LocationTab( latitude = viewModelArg.latitude.value, longitude = viewModelArg.longitude.value, city = viewModelArg.city.value, country = viewModelArg.country.value )  }


            }






        }
    }
}


@Composable
fun FancyTab(
    title: String,
    onClick: () -> Unit,
    selected: Boolean
) {// основная таблица с информацией
    Tab(
        selected = selected,
        onClick = onClick,
        text = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
        }
    )


}


@Composable
fun ImageDetail(imageUrlPath: String) {// отображение аватарки
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrlPath)
            .crossfade(true)
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
}


@Composable
fun ProfileTab(viewModelArg: DetailViewModelArg) { // first name, last name, gender (male), age, date of birth
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = "Age: ${viewModelArg.age.value}")
        Text(text = "Date of birth: ${viewModelArg.data.value}")
        Text(text = "Nationality: ${viewModelArg.nat.value}")
    }

    /*   UserInfoItem(drawableStart = R.drawable.ic_email_24dp, text = email)
       UserInfoItem(drawableStart = R.drawable.ic_local_phone_24dp, text = phone)
       UserInfoItem(drawableStart = R.drawable.ic_cell_24dp, text = cell)*/

}

@Composable
fun ContactTab(viewModelArg: DetailViewModelArg) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                UserInfoItem(
                    drawableStart = R.drawable.ic_email_24dp,
                    text = viewModelArg.email.value
                )
                UserInfoItem(
                    drawableStart = R.drawable.ic_local_phone_24dp,
                    text = viewModelArg.phone.value
                )
                UserInfoItem(
                    drawableStart = R.drawable.ic_cell_24dp,
                    text = viewModelArg.cell.value
                )

            }
        }


    }
}


@Composable
fun UserInfoItem(@DrawableRes drawableStart: Int, text: String) { // иконки и текст для профиля
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = drawableStart), contentDescription = null)
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun LocationTab(
    latitude: Double, longitude: Double, city: String, country: String
) {
    val location = LatLng(latitude, longitude)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        GoogleMap(
            cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(location, 8f)
            ),
            modifier = Modifier.fillMaxSize(),
            uiSettings = MapUiSettings(
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                scrollGesturesEnabled = false,
            )
        ) {
            Marker(
                state = MarkerState(position = location),
                title = city,
                snippet = country
            )
        }
    }

}
