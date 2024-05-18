package com.popcorn.api.movies

import androidx.lifecycle.ViewModel
import com.popcorn.api.Fetch

class MoviesViewModel: ViewModel() {
    val fetch = Fetch()
}