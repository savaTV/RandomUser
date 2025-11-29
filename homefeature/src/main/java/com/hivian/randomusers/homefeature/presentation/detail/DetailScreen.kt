package com.hivian.randomusers.homefeature.presentation.detail

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            ondelete = { viewModel.deleteUser() },
            age = viewModel.age,
            data = viewModel.data,
            nat = viewModel.nat,
            gender = viewModel.gender
        )
    )
}

@Composable
fun DetailContent(
    @PreviewParameter(DetailViewModelArgProvider::class) viewModelArg: DetailViewModelArg
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    // Custom Colors
    val headerBlue = Color(0xFF42A5F5)
    val headerDark = Color(0xFF1976D2)
    val actionBarColor = Color(0xFF1A237E) // Dark Blue
    val backgroundColor = Color(0xFFF5F5F5)

    Scaffold(
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(headerBlue, headerDark)
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = { viewModelArg.navigateBack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }


                IconButton(
                    onClick = { viewModelArg.ondelete() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete User",
                        tint = Color.White
                    )
                }
                // ------------------------------------

                // Avatar and Text (Overlapping the background)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 110.dp) // Push down to overlap
                ) {
                    // Avatar
                    Surface(
                        shape = CircleShape,
                        shadowElevation = 10.dp,
                        border = androidx.compose.foundation.BorderStroke(3.dp, Color.White),
                        modifier = Modifier.size(130.dp)
                    ) {
                        ImageDetail(imageUrlPath = viewModelArg.picture.value)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Greeting Text
                    Text(
                        text = "Hi, how are you today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.8f)
                    )

                    Text(
                        text = "I'm",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.7f)
                    )

                    Text(
                        text = viewModelArg.name.value,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  2. Action Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp), // Fully rounded
                colors = CardDefaults.cardColors(containerColor = actionBarColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ActionBarItem(
                        icon = Icons.Default.Person,
                        isSelected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    ActionBarItem(
                        icon = Icons.Default.Phone,
                        isSelected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                    ActionBarItem(
                        icon = Icons.Default.Email,
                        isSelected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 }
                    )
                    ActionBarItem(
                        icon = Icons.Default.LocationOn,
                        isSelected = selectedTabIndex == 3,
                        onClick = { selectedTabIndex = 3 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //3. Details Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> ProfileInfoTab(viewModelArg)
                    1 -> PhoneTab(viewModelArg)
                    2 -> EmailTab(viewModelArg)
                    3 -> LocationTab(
                        latitude = viewModelArg.latitude.value,
                        longitude = viewModelArg.longitude.value,
                        city = viewModelArg.city.value,
                        country = viewModelArg.country.value
                    )
                }
            }

            // Bottom padding for scrolling
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ActionBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF42A5F5) else Color.White, // Light Blue if selected, White if not
        animationSpec = tween(300),
        label = "IconColorAnimation"
    )

    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

// --- Tab 1: Profile Details ---
@Composable
fun ProfileInfoTab(viewModelArg: DetailViewModelArg) {
    // Split name for display purposes
    val fullName = viewModelArg.name.value
    val nameParts = fullName.split(" ")
    val firstName = nameParts.firstOrNull() ?: ""
    val lastName = nameParts.drop(1).joinToString(" ")

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(label = "First name", value = firstName)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

            DetailRow(label = "Last name", value = lastName.ifEmpty { "-" })
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

            DetailRow(label = "Gender", value = viewModelArg.gender.value)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

            DetailRow(label = "Age", value = viewModelArg.age.value.toString())
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))

            DetailRow(
                label = "Date of birth",
                value = viewModelArg.data.value.substringBefore("T")
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.width(120.dp) // Fixed width for alignment
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        )
    }
}

// --- Tab 2: Phone ---
@Composable
fun PhoneTab(viewModelArg: DetailViewModelArg) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(label = "Home Phone", value = viewModelArg.phone.value)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
            DetailRow(label = "Cell Phone", value = viewModelArg.cell.value)
        }
    }
}

// --- Tab 3: Email ---
@Composable
fun EmailTab(viewModelArg: DetailViewModelArg) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            DetailRow(label = "Email", value = viewModelArg.email.value)
        }
    }
}

// --- Tab 4: Location ---
@Composable
fun LocationTab(
    latitude: Double, longitude: Double, city: String, country: String
) {
    val location = LatLng(latitude, longitude)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailRow(label = "Country", value = country)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                DetailRow(label = "City", value = city)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            GoogleMap(
                cameraPositionState = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(location, 4f)
                ),
                modifier = Modifier.fillMaxSize(),
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = city,
                    snippet = country
                )
            }
        }
    }
}

@Composable
fun ImageDetail(imageUrlPath: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrlPath)
            .crossfade(true)
            .build()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(name = "Light mode", showBackground = true)
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailContentPreview(
    @PreviewParameter(DetailViewModelArgProvider::class) viewModelArg: DetailViewModelArg
) {
    DetailContent(viewModelArg)
}