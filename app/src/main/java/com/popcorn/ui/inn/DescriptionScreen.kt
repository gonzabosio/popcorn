package com.popcorn.ui.inn

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.popcorn.MoviesViewModel
import com.popcorn.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class DescriptionScreen(
    private val sharedVM: MoviesViewModel
) : Screen {
    @Composable
    override fun Content() {
        val nav = LocalNavigator.current
        BackHandler {
            nav?.pop()
        }
        val movie by sharedVM.movieDetails.collectAsState()
        val imgUrl = "https://image.tmdb.org/t/p/original"
        val modifier: Modifier = Modifier
        var isFavorite by remember { mutableStateOf(false) }
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }
        Surface(
            color = colorResource(id = R.color.bgDM),
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier.padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                AsyncImage(model = imgUrl + movie.backdropPath, contentDescription = null, modifier.fillMaxWidth())
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    colorResource(id = R.color.bgDM)
                                ),
                                startY = 400f,
                                endY = 700f
                            )
                        )
                        .zIndex(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                //Add to favorite
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .shadow(elevation = 16.dp, shape = RoundedCornerShape(90.dp))
                        ) {
                            Icon(imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = null,
                                tint = colorResource(id = R.color.letter), modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(0.21f))
                    Box(
                        modifier.padding(4.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.background(color = colorResource(id = R.color.bgDM))
                        ) {
                            item {
                                Text(
                                    text = movie.title,
                                    color = colorResource(id = R.color.letter),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(16.dp),
                                    lineHeight = 32.sp
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier
                                        .height(1.dp)
                                        .background(Color.White)
                                        .fillMaxWidth(0.9f)
                                    )
                                    Text(
                                        text = movie.overview,
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    Spacer(modifier = Modifier
                                        .height(1.dp)
                                        .background(Color.White)
                                        .fillMaxWidth(0.9f)
                                    )
                                }
                                Row(
                                    modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Genres: ",
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 16.dp)
                                    )
                                    Text(
                                        text = movie.genres.joinToString(", ") { it.name },
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 16.dp)
                                    )
                                }
                                Text(
                                    text = "Duration: ${movie.runtime}m",
                                    color = colorResource(id = R.color.letter),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(start = 18.dp, bottom = 8.dp)
                                )
                                Text(
                                    text = "Release: ${movie.releaseDate.slice(0..3)}",
                                    color = colorResource(id = R.color.letter),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(start = 18.dp, bottom = 8.dp)
                                )
                                Row {
                                    Text(
                                        text = "Review: ${movie.voteAverage.toString().slice(0..2)}",
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(start = 18.dp, bottom = 8.dp)
                                    )
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = colorResource(id = R.color.high))
                                    Text(
                                        text = "${movie.voteCount} Vote",
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                    )
                                }
                                Text(
                                    text = "Popularity: ${movie.popularity}",
                                    color = colorResource(id = R.color.letter),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(start = 18.dp, bottom = 8.dp)
                                )
                                Row {
                                    Text(
                                        text = "Production: ",
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(start = 18.dp, bottom = 8.dp)
                                    )
                                    Text(
                                        text = movie.productionCompanies.joinToString(", ") { it.name },
                                        color = colorResource(id = R.color.letter),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}