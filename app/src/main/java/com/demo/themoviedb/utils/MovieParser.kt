package com.demo.themoviedb.utils

import com.demo.themoviedb.data.model.MovieType

class MovieParser {
   companion object{

       val POPULAR:String="POPULAR"
       val TOP_RATED:String="TOP_RATED"
       val UPCOMING:String="UPCOMING"

       fun parseMovieType(priority: String): MovieType {

           return when (priority) {

               "POPULAR" -> MovieType.POPULAR
               "TOP_RATED" -> MovieType.TOP_RATED
               "UPCOMING" -> MovieType.UPCOMING
               else -> MovieType.UPCOMING

           }
       }

       fun parseMovieTypeToInt(priority: MovieType): Int {
           return when (priority) {
               MovieType.POPULAR -> 0
               MovieType.TOP_RATED -> 1
               MovieType.UPCOMING -> 2
           }
       }
   }

}