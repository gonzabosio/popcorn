package com.popcorn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.popcorn.ui.auth.RegisterScreen
import com.popcorn.ui.inn.HomeScreen
import com.popcorn.ui.theme.PopcornTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm by viewModels<MoviesViewModel>()
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var registerScreen: RegisterScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            PopcornTheme(darkTheme = true) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    Navigator(HomeScreen(vm, registerScreen))
                }
                else {
                    Navigator(registerScreen)
                }
            }
        }
    }
}