package com.popcorn.api

import com.popcorn.api.movies.ApiResponse
import com.popcorn.api.movies.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIsService {
    @GET("movie/popular")
    suspend fun getPopularMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): ApiResponse

    @GET("movie/top_rated")
    suspend fun getTopMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): ApiResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): ApiResponse

    @GET("search/movie")
    suspend fun getQueryMoviesI(
        @Query("query") name: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): ApiResponse

    @GET("authentication/token/new")
    suspend fun getNewTokenI(): TokenResponse
}