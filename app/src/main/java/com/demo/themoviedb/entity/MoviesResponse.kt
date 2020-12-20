package com.demo.themoviedb.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.themoviedb.data.model.MovieType
import kotlinx.android.parcel.Parcelize

data class MoviesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
{


    @Entity(tableName = "popular_movies_table")
    @Parcelize
    data class Result(
        val adult: Boolean,
        var backdrop_path: String,
        @PrimaryKey(autoGenerate = false)
        var id: String,
        val original_language: String,
        val original_title: String,
        val overview: String,
        val popularity: Double,
        var poster_path: String,
        val release_date: String,
        val title: String,
        val video: Boolean,
        val vote_average: Double,
        val vote_count: Int,
        var movieType:MovieType
    ): Parcelable
}