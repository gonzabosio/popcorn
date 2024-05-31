package com.popcorn.ui.inn

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.popcorn.R
import com.popcorn.api.Fetch
import com.popcorn.api.MovieItem
import com.popcorn.MoviesViewModel
import com.popcorn.ui.SearchBarFun
import com.popcorn.ui.auth.RegisterScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeScreen @Inject constructor(
    private val sharedVM: MoviesViewModel,
    private val registerScreen: RegisterScreen,
): Screen {
    @Composable
    override fun Content() {
        val user = Firebase.auth.currentUser

        Log.d("firebase_tag","${user?.email}")
        val modifier: Modifier = Modifier
        val tab by sharedVM.tab.collectAsState()
        val context = LocalContext.current
        val activity = context as? Activity
        BackHandler {
            activity?.finish()
        }
        var searchDisplay by remember { mutableStateOf("") }
        val search = SearchBarFun(registerScreen, sharedVM)
        Scaffold(
            containerColor = colorResource(id = R.color.section),
            contentColor = colorResource(id = R.color.letter),
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                Column(
                    Modifier.padding(WindowInsets.statusBars.asPaddingValues())
                ) {
                    search.ExpandableSearchView(
                        searchDisplay = searchDisplay,
                        onSearchDisplayChanged = {newSearchDisplay-> searchDisplay = newSearchDisplay},
                        onSearchDisplayClosed = {},
                        expandedInitially = false,
                        tint = colorResource(id = R.color.letter)
                    )
                }
            },
            content = {
                Column(
                    modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = colorResource(id = R.color.bgDM))
                ) {
                    TextTabs(tab, sharedVM)
                }
            },
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTabs(tab: Int, sharedVM: MoviesViewModel) {
    val fetch = Fetch()
    var movies by remember { mutableStateOf<List<MovieItem>>(emptyList()) }
    var page by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(tab) }
    val titles = listOf("Popular", "Top", "Upcoming")
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    var selectedMovie by remember {
        mutableIntStateOf(-1)
    }
    SecondaryTabRow(selectedTabIndex = selectedTab, containerColor = colorResource(id = R.color.section2)) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = {
                    movies = emptyList()
                    page = 1
                    selectedTab = index
                    sharedVM.saveTab(selectedTab)
                },
                text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis,
                    color = colorResource(id = R.color.letter)) }
            )
        }
    }
    when(selectedTab) {
        0 -> {
            CoroutineScope(Dispatchers.IO).launch {
                movies = fetch.getPopularMovies(page)
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                state = gridState,
                content = {
                    itemsIndexed(movies) {index, movie ->
                        ClickOnMovie(movie, index, sharedVM) { i->
                            sharedVM.saveTab(selectedTab)
                            selectedMovie = i
                        }

                    }
                }
            )
            LaunchedEffect(gridState) {
                snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        val totalItems = gridState.layoutInfo.totalItemsCount
                        val lastVisibleItem = visibleItems.lastOrNull()
                        if (lastVisibleItem != null && lastVisibleItem.index == totalItems - 1 && page <= 500) {
                            coroutineScope.launch {
                                page++
                                movies = movies + fetch.getPopularMovies(page)
                            }
                        }
                    }
            }
        }
        1 -> {
            CoroutineScope(Dispatchers.IO).launch {
                movies = fetch.getTopMovies(page)
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                state = gridState,
                content = {
                    itemsIndexed(movies) {index, movie ->
                        ClickOnMovie(movie, index, sharedVM) { i->
                            sharedVM.saveTab(selectedTab)
                            selectedMovie = i
                        }

                    }
                }
            )
            LaunchedEffect(gridState) {
                snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        val totalItems = gridState.layoutInfo.totalItemsCount
                        val lastVisibleItem = visibleItems.lastOrNull()
                        if (lastVisibleItem != null && lastVisibleItem.index == totalItems - 1 && page <= 500) {
                            coroutineScope.launch {
                                page++
                                movies = movies + fetch.getTopMovies(page)
                            }
                        }
                    }
            }
        }
        2 -> {
            CoroutineScope(Dispatchers.IO).launch {
                movies = fetch.getUpcomingMovies(page)
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                state = gridState,
                content = {
                    itemsIndexed(movies) {index, movie ->
                        ClickOnMovie(movie, index, sharedVM) { i->
                            sharedVM.saveTab(selectedTab)
                            selectedMovie = i
                        }

                    }
                }
            )
            LaunchedEffect(gridState) {
                snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        val totalItems = gridState.layoutInfo.totalItemsCount
                        val lastVisibleItem = visibleItems.lastOrNull()
                        if (lastVisibleItem != null && lastVisibleItem.index == totalItems - 1 && page <= 500) {
                            coroutineScope.launch {
                                page++
                                movies = movies + fetch.getUpcomingMovies(page)
                            }
                        }
                    }
            }
        }
    }
}
@Composable
fun ClickOnMovie(movie: MovieItem, index: Int, sharedVM: MoviesViewModel, onClick : (Int) -> Unit) {
    val imgUrl = "https://image.tmdb.org/t/p/original"
    val nav = LocalNavigator.current
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    Column(
        Modifier
            .clickable {
                onClick(index)
                CoroutineScope(Dispatchers.Main).launch {
                    sharedVM.loadMovie(movie.id)
                    nav?.push(DescriptionScreen(sharedVM))
                }
            }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = imgUrl+movie.posterPath,
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(180.dp)
        )
        Text(text = movie.title,
            Modifier
                .padding(top = 4.dp)
                .width(120.dp)
        )
    }
}
