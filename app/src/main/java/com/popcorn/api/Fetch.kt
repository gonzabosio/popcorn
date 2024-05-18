package com.popcorn.api

import com.popcorn.api.movies.ApiResponse
import com.popcorn.api.movies.TokenResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Fetch {
    suspend fun getPopularMovies(): ApiResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getPopularMoviesI()
    }
    suspend fun getTopMovies(): ApiResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getTopMoviesI()
    }
    suspend fun getUpcomingMovies(): ApiResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getUpcomingMoviesI()
    }
    suspend fun getQueryMovies(): ApiResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val response = retrofit.create(APIsService::class.java)

        return response.getQueryMoviesI("Iron Man")
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