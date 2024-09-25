package com.example.moviesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import com.example.moviesearchapp.ui.theme.MovieSearchAppTheme
import retrofit2.create

class MainActivity : ComponentActivity() {
    private val apiKey = "e6061991a39ea18117d1813e60ebe2e5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieSearchAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    MovieScreen(apiKey)
                }
            }
        }
    }
}

@Composable
fun MovieScreen(apiKey: String){
    var movieTitle by remember{ mutableStateOf("Loading...")}
    var movieOverview by remember { mutableStateOf(" ")}

    //Initializing Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    val movieApi = retrofit.create(TMDBApi::class.java)

    LaunchedEffect(Unit){
        //query movie name here
        movieApi.searchMovies("Titanic", apiKey)
            .enqueue(object : retrofit2.Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: retrofit2.Response<MovieResponse>) {
                    if(response.isSuccessful){
                        val movies = response.body()?.results
                        if (movies != null && movies.isNotEmpty()) {
                            movieTitle = movies[0].title
                            movieOverview = movies[0].overview
                        } else {
                            movieTitle = "No results found"
                        }
                    } else {
                        movieTitle = "Error fetching data"
                    }
                }
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    movieTitle = "Network error"
                }
            })
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Title: $movieTitle", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Overview: $movieOverview", style = MaterialTheme.typography.bodyMedium)
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieSearchAppTheme {

    }
}