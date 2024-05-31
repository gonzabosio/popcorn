package com.popcorn

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.popcorn.api.Fetch
import com.popcorn.api.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor() : ViewModel() {

    private val _tab = MutableStateFlow(0)
    val tab: StateFlow<Int> = _tab
    fun saveTab(tab: Int) {
        _tab.value = tab
    }

    private val fetch = Fetch()
    private val _movieDetails = MutableStateFlow(MovieDetails())
    val movieDetails: StateFlow<MovieDetails> = _movieDetails.asStateFlow()
    suspend fun loadMovie(movieId: Int) {
        _movieDetails.value = fetch.getMovieById(movieId)
    }

    private val _emailError = MutableStateFlow("")
    val emailError: StateFlow<String> = _emailError
    private val _passwordError = MutableStateFlow("")
    val passwordError: StateFlow<String> = _passwordError

    private val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    private val numberRegex = Pattern.compile("(?=.*[0-9])")
    private val lowerCaseRegex = Pattern.compile("(?=.*[a-z])")
    private val upperCaseRegex = Pattern.compile("(?=.*[A-Z])")
    private val noSpaceRegex = Pattern.compile("(?=\\S+$)")
    private val lengthRegex= Pattern.compile(".{4,}$")

    private val validEmail = mutableStateOf(false)
    private val validPw = mutableStateOf(false)
    fun isValidEmail(email: String): Boolean {
        if (email.matches(emailRegex.toRegex())) {
            _emailError.value = ""
            validEmail.value = true
        }
        else {
            _emailError.value = "Invalid email"
            validEmail.value = false
        }
        return validEmail.value
    }
    fun isValidPassword(password: String): Boolean {
        _passwordError.value = when{
            !numberRegex.matcher(password).find() -> "Password must contain at least one number"
            !lowerCaseRegex.matcher(password).find()-> "Password must contain at least one lower case"
            !upperCaseRegex.matcher(password).find() -> "Password must contain at least one upper case"
            !noSpaceRegex.matcher(password).find() -> "Password must not contain spaces"
            !lengthRegex.matcher(password).find() -> "Password must contain at least 4 characters"
            else -> ""
        }
        return validPw.value
    }
}