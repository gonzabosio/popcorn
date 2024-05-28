package com.popcorn.ui.inn

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.api.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileScreen(
    private val sharedVM: MoviesViewModel,
) : Screen {
    @SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
    @Composable
    override fun Content() {
        val nav = LocalNavigator.current
        val modifier: Modifier = Modifier
        val favorites by remember { mutableStateOf<List<MovieItem>>(emptyList()) }
        val gridState = rememberLazyGridState()
        var selectedMovie by remember {
            mutableIntStateOf(-1) }
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }
        Scaffold(
            containerColor = colorResource(id = R.color.section),
            contentColor = colorResource(id = R.color.letter),
            topBar = {
                Column(
                    modifier.padding(WindowInsets.statusBars.asPaddingValues())
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.section))
                            .height(80.dp)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.big_pop), contentDescription = null, tint = colorResource(id = R.color.btn))
                        Spacer(modifier.width(8.dp))
                        Text(text = "Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = modifier
                            .weight(1f))
                        IconButton(onClick = {
                            //Log out
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier.size(25.dp))
                        }
                    }
                }
            },
            content = {
                BackHandler {
                    nav?.push(HomeScreen(sharedVM))
                }
                Column(
                    modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.bgDM))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.section2))
                            .padding(start = 8.dp)
                    ) {
                        Text(text = "Favorites", modifier = modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 6.dp), fontSize = 18.sp)
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = modifier.size(18.dp),
                            tint = colorResource(id = R.color.high)
                        )
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        //Get Favorite Movies
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        state = gridState,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        content = {
                            itemsIndexed(favorites) { index, movie ->
                                ClickOnMovie(movie, index, sharedVM) { i->
                                    selectedMovie = i
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}