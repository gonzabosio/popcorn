package com.popcorn.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.ui.inn.HomeScreen

class LoginScreen(
    private val sharedVM: MoviesViewModel
) : Screen {
    @Composable
    override fun Content() {
        val nav = LocalNavigator.current
        val modifier: Modifier = Modifier
        var email by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val emailMessage by sharedVM.emailError.collectAsState()
        val usernameMessage by sharedVM.usernameError.collectAsState()
        val passwordMessage by sharedVM.passwordError.collectAsState()
        val systemUiController = rememberSystemUiController()
        Surface(
            color = colorResource(id = R.color.bgDM),
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clapperboard1),
                    contentDescription = null,
                    modifier.size(230.dp)
                )
                Spacer(modifier.height(36.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = { Text(text = "Email or username") }
                )
                Spacer(modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text(text = "Password") }
                )
                Spacer(modifier.height(36.dp))
                Button(
                    onClick = {
                        nav?.push(HomeScreen(sharedVM))
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.btn),
                        contentColor = colorResource(id = R.color.letter)),
                    modifier = modifier
                        .width(280.dp)
                        .height(50.dp)
                        .shadow(elevation = 8.dp, spotColor = colorResource(id = R.color.btn), shape = RoundedCornerShape(80.dp))
                ) {
                    Text(text = "Login", fontSize = 18.sp)
                }
                Spacer(modifier.height(8.dp))
                TextButton(
                    onClick = {
                        nav?.push(RegisterScreen(sharedVM))
                    },
                ) {
                    Text(text = "Register", color = colorResource(id = R.color.btn))
                }
            }
        }
    }

}