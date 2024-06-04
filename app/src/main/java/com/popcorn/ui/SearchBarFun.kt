package com.popcorn.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.ui.auth.RegisterScreen
import com.popcorn.ui.inn.ProfileScreen
import com.popcorn.ui.inn.QueryScreen
import javax.inject.Inject

class SearchBarFun @Inject constructor(
    private val registerScreen: RegisterScreen,
    private val sharedVM: MoviesViewModel
) {
    @Composable
    fun ExpandableSearchView(
        searchDisplay: String,
        onSearchDisplayChanged: (String) -> Unit,
        onSearchDisplayClosed: () -> Unit,
        modifier: Modifier = Modifier,
        expandedInitially: Boolean = false,
        tint: Color = MaterialTheme.colorScheme.onPrimary
    ) {
        val (expanded, onExpandedChanged) = remember {
            mutableStateOf(expandedInitially)
        }
        Crossfade(targetState = expanded, label = "") { isSearchFieldVisible ->
            when (isSearchFieldVisible) {
                true -> ExpandedSearchView(
                    searchDisplay = searchDisplay,
                    onSearchDisplayChanged = onSearchDisplayChanged,
                    onSearchDisplayClosed = onSearchDisplayClosed,
                    onExpandedChanged = onExpandedChanged,
                    modifier = modifier,
                    tint = tint
                )

                false -> CollapsedSearchView(
                    onExpandedChanged = onExpandedChanged,
                    modifier = modifier,
                    tint = tint,
                )
            }
        }
    }
    @Composable
    fun CollapsedSearchView(
        onExpandedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        tint: Color = colorResource(id = R.color.letter)
    ) {
        val nav = LocalNavigator.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(color = colorResource(id = R.color.section))
                .height(80.dp)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(painter = painterResource(id = R.drawable.big_pop),
                contentDescription = null,
                tint = colorResource(id = R.color.btn)
            )
            Text(text = "Popcorn", fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = modifier.padding(8.dp))
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onExpandedChanged(true) },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(
                    onClick = { nav?.push(ProfileScreen(registerScreen)) },
                    modifier = modifier
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier.size(30.dp))
                }
            }
        }
    }
    @Composable
    fun ExpandedSearchView(
        searchDisplay: String,
        onSearchDisplayChanged: (String) -> Unit,
        onSearchDisplayClosed: () -> Unit,
        onExpandedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        tint: Color = MaterialTheme.colorScheme.secondary,
    ) {
        val nav = LocalNavigator.current
        BackHandler {
            onExpandedChanged(false)
            onSearchDisplayClosed()
        }
        val focusManager = LocalFocusManager.current
        val textFieldFocusRequester = remember { FocusRequester() }

        SideEffect {
            textFieldFocusRequester.requestFocus()
        }

        var textFieldValue by remember { mutableStateOf(TextFieldValue(searchDisplay, TextRange(searchDisplay.length))) }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(onClick = {
                onExpandedChanged(false)
                onSearchDisplayClosed()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back icon",
                    tint = tint
                )
            }
            TextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    onSearchDisplayChanged(it.text)
                },
                trailingIcon = {
                    SearchIcon(iconTint = tint) {
                        nav?.push(QueryScreen(sharedVM, registerScreen, searchDisplay))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(textFieldFocusRequester),
                label = {
                    Text(text = "Search movie", color = tint)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        nav?.push(QueryScreen(sharedVM, registerScreen, searchDisplay))
                    }
                ),
                singleLine = true
            )
        }
    }
    @Composable
    fun SearchIcon(iconTint: Color, click: ()-> Unit) {
        IconButton(onClick = { click() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}