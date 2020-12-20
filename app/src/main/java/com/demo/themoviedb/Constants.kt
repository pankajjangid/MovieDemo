package com.demo.themoviedb

class Constants {

    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

        val API_KEY = "3da1cfe6ac68c39d9bc9b5cde25053b2"

        val POPULAR_MOVIES = "movie/popular"
        val TOP_RATED_MOVIES = "movie/top_rated"
        val UPCOMING_MOVIES = "movie/upcoming"



        fun getCompleteURL(endpoint:String,pageCount:Int):String{
            return "$BASE_URL$endpoint?api_key=$API_KEY&language=en-US&page=$pageCount"
        }

    }
}