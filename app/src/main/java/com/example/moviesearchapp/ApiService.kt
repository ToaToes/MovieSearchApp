package com.example.moviesearchapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class MovieResponse (val results: List<Movie>)
data class Movie(val title: String, val overview: String)

// Retrofit interface
interface TMDBApi {
    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>
}
