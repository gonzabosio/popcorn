package com.popcorn.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fetch {
    suspend fun getPopularMovies(page: Int): List<MovieItem> {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getPopularMoviesI(page = page).results
    }
    suspend fun getTopMovies(page: Int): List<MovieItem> {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getTopMoviesI(page = page).results
    }
    suspend fun getUpcomingMovies(page: Int): List<MovieItem> {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getUpcomingMoviesI(page = page).results
    }
    suspend fun getMovieById(id: Int): MovieDetails {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getMovieByIdI(id)
    }
    suspend fun getQueryMovies(movie: String, page: Int): List<MovieItem> {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getQueryMoviesI(movie, page = page).results
    }

    suspend fun getNewToken(): TokenResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getNewTokenI()
    }
}