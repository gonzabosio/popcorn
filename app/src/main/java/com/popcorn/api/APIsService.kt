package com.popcorn.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIsService {
    @GET("movie/popular")
    suspend fun getPopularMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): ApiResponse

    @GET("movie/top_rated")
    suspend fun getTopMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): ApiResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesI(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): ApiResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieByIdI(
        @Path("movie_id") id: Int,
        @Query("language") language: String = "en-US",
    ): MovieDetails

    @GET("search/movie")
    suspend fun getQueryMoviesI(
        @Query("query") name: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): ApiResponse

    @GET("authentication/token/new")
    suspend fun getNewTokenI(): TokenResponse
}