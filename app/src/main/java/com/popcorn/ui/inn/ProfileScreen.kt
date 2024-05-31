package com.popcorn.ui.inn

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.api.MovieItem
import com.popcorn.ui.auth.RegisterScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileScreen @Inject constructor(
    private val registerScreen: RegisterScreen,
) : Screen {
    @SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
    @Composable
    override fun Content() {
        val sharedVM: MoviesViewModel = hiltViewModel()
        val nav = LocalNavigator.current
        val modifier: Modifier = Modifier
        val favorites by remember { mutableStateOf<List<MovieItem>>(emptyList()) }
        val gridState = rememberLazyGridState()
        var selectedMovie by remember {
            mutableIntStateOf(-1) }
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
                            .height(60.dp)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.big_pop), contentDescription = null, tint = colorResource(id = R.color.btn))
                        Spacer(modifier.width(8.dp))
                        Text(text = "Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = modifier
                            .weight(1f))
                        IconButton(onClick = {
                            Firebase.auth.signOut()
                            nav?.popAll()
                            nav?.push(registerScreen)
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier.size(25.dp))
                        }
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth(0.9f).height(1.dp)
                        .background(
                            color = colorResource(
                                id = R.color.section2
                            )
                        ).align(Alignment.CenterHorizontally)
                    )
                    Text(text = "Hello ${Firebase.auth.currentUser?.displayName}", fontStyle = FontStyle.Italic, fontSize = 20.sp ,
                        modifier = Modifier.padding(start = 20.dp, bottom = 16.dp, top = 16.dp))
                }
            },
            content = {
                BackHandler {
                    nav?.pop()
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
                    ) {
                        Text(text = "Favorites", modifier = modifier.padding(start = 24.dp, top = 12.dp, bottom = 12.dp, end = 6.dp), fontSize = 18.sp)
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = modifier.size(18.dp),
                            tint = colorResource(id = R.color.high)
                        )
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        //Get favorite list
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