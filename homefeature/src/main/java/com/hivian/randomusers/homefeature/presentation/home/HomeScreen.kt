package com.hivian.randomusers.homefeature.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hivian.randomusers.core.domain.base.ViewModelVisualState
import com.hivian.randomusers.core.presentation.navigation.NavigationAction
import com.hivian.randomusers.homefeature.domain.models.RandomUser
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToFilter: () -> Unit
) {
    viewModel.initialize()

    val navigationEventState = viewModel.navigationEvent.collectAsState(initial = null)
    val usersState by viewModel.items.collectAsState()
    val visualState = viewModel.viewModelVisualState.collectAsState()



    LaunchedEffect(navigationEventState.value) {
        when (val event = navigationEventState.value) {
            is NavigationAction.ToDetailScreen -> {
                onNavigateToDetail(event.userId)
            }

            else -> Unit
        }
    }

    HomeContent(
        HomeViewModelArg(
            randomUsers = usersState,
            isListLoading = remember { mutableStateOf(false) },
            title = viewModel.title,
            errorMessage = viewModel.errorMessage,
            retryMessage = viewModel.retryMessage,
            refresh = { viewModel.refresh() },
            loadNext = { },
            openDetail = { userId -> viewModel.openRandomUserDetail(userId) },
            visualState = visualState,
            onFilterClick = onNavigateToFilter,
            onDelete = { userId -> viewModel.deleteUser(userId) }
        )
    )
}

@Preview(name = "Light mode")
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    @PreviewParameter(HomeViewModelArgProvider::class) viewModelArg: HomeViewModelArg
) {
    val primaryDarkBlue = Color(0xFF1A237E)

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModelArg.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModelArg.onFilterClick() },
                containerColor = primaryDarkBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add User")
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (viewModelArg.visualState.value) {
                is ViewModelVisualState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryDarkBlue)
                    }
                }

                is ViewModelVisualState.Success -> {
                    InitUserList(
                        randomUsers = viewModelArg.randomUsers,
                        isLoadingMore = viewModelArg.isListLoading.value,
                        onItemClick = { userId -> viewModelArg.openDetail(userId) },
                        onDelete = viewModelArg.onDelete,


                        )
                }

                is ViewModelVisualState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        InitErrorView(
                            errorViewArgs = InitErrorViewArg(
                                viewModelArg.errorMessage,
                                viewModelArg.retryMessage
                            ),
                            onRetry = { viewModelArg.refresh() }
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
fun InitUserList(
    randomUsers: List<RandomUser>,
    isLoadingMore: Boolean,
    onItemClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = randomUsers,
            key = { it.id }
        ) { user ->
            SwipeToDeleteContainer(
                item = user,
                onDelete = { onDelete(user.id) }
            ) {
                UserListItem(user = user, onItemClick = onItemClick)
            }
        }
        if (isLoadingMore) {
            item { UserListLoadingItem() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    val currentItem by rememberUpdatedState(item)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete(currentItem)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Transparent
                }, label = "ColorAnimation"
            )

            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.2f else 0.8f,
                label = "ScaleAnimation"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color,
                        RoundedCornerShape(16.dp)
                    ) // Скругляем фон так же, как карточку
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.scale(scale),
                    tint = Color.White
                )
            }
        },
        content = { content(item) },
        enableDismissFromStartToEnd = false
    )
}

@Composable
fun UserListItem(user: RandomUser, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(user.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            UserImage(imageUrlPath = user.picture)

            Spacer(modifier = Modifier.width(16.dp))

            // Информация
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Имя
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E), // Темно-синий для контраста
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Доп. инфо (Телефон или Email)
                Text(
                    text = user.phone.ifEmpty { user.email }, // Предпочитаем телефон, если есть
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    val flagEmoji = countryCodeToEmoji(user.nat)

                    Text(
                        text = flagEmoji,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = user.nat.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

fun countryCodeToEmoji(countryCode: String): String {
    if (countryCode.length != 2) return "🏳️"
    val firstLetter = Character.codePointAt(countryCode.uppercase(), 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode.uppercase(), 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

@Preview(widthDp = 50, heightDp = 50)
@Composable
private fun UserImage(imageUrlPath: String = "") {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrlPath)
            .crossfade(true)
            .build()
    )

    Surface(
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        shadowElevation = 4.dp,
        color = Color.LightGray
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            }
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitErrorView(
    @PreviewParameter(InitErrorViewArgProvider::class)
    errorViewArgs: InitErrorViewArg,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Add,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorViewArgs.errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { onRetry() },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A237E)
            )
        ) {
            Text(text = errorViewArgs.retryMessage)
        }
    }
}

@Preview(widthDp = 50, heightDp = 50)
@Composable
fun UserListLoadingItem() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF1A237E))
    }
}