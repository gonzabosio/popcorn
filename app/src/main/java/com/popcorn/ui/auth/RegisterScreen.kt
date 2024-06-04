package com.popcorn.ui.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.popcorn.MoviesViewModel
import com.popcorn.R
import com.popcorn.fire.AuthProcess
import com.popcorn.ui.inn.HomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterScreen @Inject constructor(
    private val authProcess: AuthProcess,
): Screen {
    @Composable
    override fun Content() {
        val sharedVM: MoviesViewModel = hiltViewModel()
        val nav = LocalNavigator.current
        val modifier: Modifier = Modifier
        var email by remember { mutableStateOf("")}
        var username by remember { mutableStateOf("")}
        var password by remember { mutableStateOf("")}
        var showPassword by remember { mutableStateOf(false) }
        val emailMessage by sharedVM.emailError.collectAsState()
        val passwordMessage by sharedVM.passwordError.collectAsState()
        val context = LocalContext.current
        val activity = context as? Activity
        BackHandler {
            activity?.finish()
        }
        Surface(
            color = colorResource(id = R.color.bgDM),
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clapperboard1),
                    contentDescription = null,
                    modifier.size(230.dp)
                )
                Spacer(modifier.height(36.dp))
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            sharedVM.isValidEmail(email)
                        },
                        label = { Text(text = "Email")},
                        singleLine = true
                    )
                    Text(text = emailMessage, color = Color.Red.copy(alpha = 0.8f), modifier = modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp))
                }
                Spacer(modifier.height(10.dp))
                Column {
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        label = { Text(text = "Username")},
                        singleLine = true
                    )
                }
                Spacer(modifier.height(34.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            sharedVM.isValidPassword(password)
                        },
                        label = { Text(text = "Password")},
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(painter = if (showPassword) painterResource(id = R.drawable.baseline_visibility_24)
                                else painterResource(id = R.drawable.baseline_visibility_off_24),
                                    contentDescription = null)
                            }
                        },
                        singleLine = true,
                    )
                    Text(text = passwordMessage, color = Color.Red.copy(alpha = 0.8f))
                }

                Spacer(modifier.height(36.dp))
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            authProcess.signUp(email, password, username) { success, error->
                                if (success) {
                                    Log.d("firebase_tag","Register Success")
                                    nav?.push(HomeScreen(sharedVM,this@RegisterScreen))
                                }
                                else {
                                    Log.d("firebase_tag","Register Failure: $error")
                                }
                            }
                        }
                    },
                    enabled = email != "" && username != "" && password != "" && emailMessage == "" && passwordMessage == "",
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.btn),
                        contentColor = colorResource(id = R.color.letter)),
                    modifier = modifier
                        .width(280.dp)
                        .height(50.dp)
                        .shadow(
                            elevation = 8.dp,
                            spotColor = colorResource(id = R.color.btn),
                            shape = RoundedCornerShape(80.dp)
                        )
                ) {
                    Text(text = "Sign Up", fontSize = 18.sp)
                }
                Spacer(modifier.height(8.dp))
                TextButton(
                    onClick = {
                        nav?.push(LoginScreen(authProcess,this@RegisterScreen))
                    },
                ) {
                    Text(text = "I have an account", color = colorResource(id = R.color.btn))
                }
            }
        }
    }
}