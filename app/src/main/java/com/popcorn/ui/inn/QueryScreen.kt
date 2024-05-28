package com.popcorn.ui.inn

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.api.Fetch
import com.popcorn.api.MovieItem
import com.popcorn.ui.SearchBarFun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QueryScreen(
    private val sharedVM: MoviesViewModel,
    private val movie: String
): Screen {
    @Composable
    override fun Content() {
        val modifier: Modifier = Modifier
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }
        var searchDisplay by remember { mutableStateOf("") }
        val search = SearchBarFun(sharedVM)
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

                    MovieList(sharedVM = sharedVM, movie = movie)
                }
            },
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun MovieList(sharedVM: MoviesViewModel, movie: String) {
    var movies by remember { mutableStateOf<List<MovieItem>>(emptyList()) }
    var page by remember { mutableIntStateOf(1) }
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val fetch = Fetch()
    CoroutineScope(Dispatchers.IO).launch {
        movies = fetch.getQueryMovies(movie,page)
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        state = gridState,
        content = {
            itemsIndexed(movies) {index, movie ->
                ClickOnSearchedMovie(movie, sharedVM)
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
                        movies = movies + fetch.getQueryMovies(movie, page)
                    }
                }
            }
    }
}
@Composable
fun ClickOnSearchedMovie(movie: MovieItem, sharedVM: MoviesViewModel) {
    val imgUrl = "https://image.tmdb.org/t/p/original"
    val nav = LocalNavigator.current
    Column(
        Modifier
            .clickable {
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