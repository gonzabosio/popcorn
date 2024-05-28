package com.popcorn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import cafe.adriel.voyager.navigator.Navigator
import com.popcorn.ui.auth.RegisterScreen
import com.popcorn.ui.theme.PopcornTheme

class MainActivity : ComponentActivity() {

    private val vm by viewModels<MoviesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PopcornTheme(darkTheme = true) {
                Navigator(screen = RegisterScreen(vm))
            }
        }
    }
}